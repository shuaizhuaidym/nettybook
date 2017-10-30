package com.section9;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeFactory {

	public static MarshallingDecoder buildMarshallingDecoder() {
		MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);

		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, config);
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);
		System.out.println("create decoder");
		return decoder;
	}

	public static MarshallingEncoder buildMarshallingEncoder() {
		MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration config = new MarshallingConfiguration();
		config.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(factory, config);
		MarshallingEncoder encoder = new MarshallingEncoder(provider);
		System.out.println("create encoder");
		return encoder;
	}
}
