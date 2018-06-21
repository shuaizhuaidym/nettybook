package com.xml.mashlling;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import com.xml.bean.Order;

public class Marshller {
	private IBindingFactory factory = null;
	private StringWriter writer = null;
	private StringReader reader = null;

	private final static String CHARSET_NAME = "UTF-8";

	/**
	 * 序列化到文件
	 * @param order
	 * @param file
	 * @return
	 * @throws JiBXException
	 * @throws IOException
	 */
	public String encode2File(Order order, String file) throws JiBXException, IOException {

		factory = BindingDirectory.getFactory(Order.class);
		OutputStream writer = new FileOutputStream(file);

		IMarshallingContext mctx = factory.createMarshallingContext();
		mctx.setIndent(4);
		mctx.marshalDocument(order, CHARSET_NAME, null, writer);
		writer.close();

		System.out.println(file);

		return writer.toString();

	}

	/**
	 * 序列化为XML
	 * @param order
	 * @return
	 * @throws JiBXException
	 * @throws IOException
	 */
	public String encode2Xml(Order order) throws JiBXException, IOException {
		factory = BindingDirectory.getFactory(Order.class);
		writer = new StringWriter();

		IMarshallingContext mctx = factory.createMarshallingContext();
		mctx.setIndent(2);
		mctx.marshalDocument(order, CHARSET_NAME, null, writer);
		String xmlStr = writer.toString();
		writer.close();

		System.out.println(xmlStr.toString());

		return xmlStr;
	}

	/**
	 * 反序列化为对象
	 * @param xmlBody
	 * @return
	 * @throws JiBXException
	 */
	public Order decode2Order(String xmlBody) throws JiBXException {
		factory = BindingDirectory.getFactory(Order.class);
		reader = new StringReader(xmlBody);

		IUnmarshallingContext uctx = factory.createUnmarshallingContext();
		Order order = (Order) uctx.unmarshalDocument(reader);
		return order;
	}
}
