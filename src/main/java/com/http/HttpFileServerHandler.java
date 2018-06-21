package com.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private final String url;

	private static final Pattern IN_SECURE_URI = Pattern.compile(".*[<>&\"].*");

	private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[a-zA-Z0-9][-_a-zA-Z0-9\\.]*");

	public HttpFileServerHandler(String url) {
		super();
		this.url = url;
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus code) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, code,
				Unpooled.copiedBuffer("Failure:" + code.toString(), CharsetUtil.UTF_8));

		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF-8");

		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public static void setCotentTypeHeader(HttpResponse resp, File f) {
		javax.activation.MimetypesFileTypeMap m = new javax.activation.MimetypesFileTypeMap();
		resp.headers().set(Names.CONTENT_TYPE, "");
	}

	private String sanitizeUri(String uri) {
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error();
			}
		}
		if (!uri.startsWith(url)) {
			return null;
		}
		uri = uri.replace('/', File.separatorChar);
		if (uri.contains(File.separator + ".") || uri.contains("." + File.separator) || uri.startsWith(".")
				|| uri.endsWith(".") || IN_SECURE_URI.matcher(uri).matches()) {
			return null;
		}
		return System.getProperty("user.dir") + File.separator + uri;
	}

	private void sendListing(ChannelHandlerContext ctx, File dir) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		// 键 值
		response.headers().set("Content-Type", "text/html;charset=UTF-8");
		StringBuilder buf = new StringBuilder();
		String dirPath = dir.getPath();
		buf.append("<!DOCTYPE html>\r\n");
		buf.append("<html><head><title>");
		buf.append(dirPath);
		buf.append("目录：");
		buf.append("</title></head><body>\r\n");
		buf.append("<h3>\r\n");
		buf.append(dirPath).append("目录：").append("</h3>\r\n");

		buf.append("<ul><li>上级目录：<a href=\"../\">..</a></li>\r\n");
		for (File f : dir.listFiles()) {
			if (f.isHidden() || !f.canRead()) {
				continue;
			}
			String name = f.getName();
			if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
				continue;
			}
			buf.append("<li><a href=\"").append(name).append("\">").append(name).append("</a></li>\r\n");
		}
		buf.append("</ul></body></html>\r\n");
		
		ByteBuf bbuf = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
		response.content().writeBytes(bbuf);
		bbuf.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private void sendRedirect(ChannelHandlerContext ctx, String url) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaders.Names.LOCATION, url);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public String getUrl() {
		return url;
	}

	@Override
	protected void messageReceived(final ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {

		if (!request.getDecoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if (request.getMethod() != HttpMethod.GET) {
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}
		final String uri = request.getUri();
		final String path = sanitizeUri(uri);

		if (path == null) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		File file = new File(path);
		if (file.isHidden() || !file.exists()) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		if (file.isDirectory()) {
			if (path.endsWith(File.separator)) {
				sendListing(ctx, file);
			} else {
				sendRedirect(ctx, uri + '/');
			}
			return;
		}
		if (!file.isFile()) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
		}
		
		RandomAccessFile rfile = null;
		try {
			rfile = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		
		long flength = rfile.length();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		
		HttpHeaders.setContentLength(response, flength);
		setCotentTypeHeader(response, file);
		
		if (HttpHeaders.isKeepAlive(request)) {
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		ctx.write(response);
		
		ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(rfile, 0, flength, 8192), ctx.newProgressivePromise());

		sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

			@Override
			public void operationProgressed(ChannelProgressiveFuture arg0, long progress, long total)
					throws Exception {
				if (total < 0) {// total is unknown
					System.out.println("Transfer progress:" + progress);
				} else {
					System.out.println("Total:" + total + "/" + progress + " transferd");
				}
			}

			@Override
			public void operationComplete(ChannelProgressiveFuture arg0) throws Exception {
				System.out.println("Transfer complete.");
			}
		});
		
		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		
		if (!HttpHeaders.isKeepAlive(request)) {
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
