package com.coding;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeCFactory {

	// static final MarshallerFactory factory =
	// Marshalling.getProvidedMarshallerFactory("serial");
	// static final MarshallingConfiguration config = new
	// MarshallingConfiguration();
	// static {
	// config.setVersion(5);
	// }

	public static MarshallingDecoder buildMarshallingDecoder() {
		final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, config);
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);
		return decoder;
	}

	public static MarshallingEncoder buildMarshallingEncoder() {
		final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(factory, config);
		MarshallingEncoder encoder = new MarshallingEncoder(provider);

		return encoder;
	}
}
