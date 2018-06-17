package com.xml.binding;

import java.io.IOException;
import java.util.List;

import org.jibx.binding.generator.BindGen;
import org.jibx.binding.generator.BindGenCommandLine;
import org.jibx.binding.model.BindingHolder;
import org.jibx.runtime.JiBXException;
import org.jibx.schema.generator.SchemaGen;

public class Binder {
	String cmd = "java -cp ..\\lib\\jibx-tools.jar;bin org.jibx.binding.generator.BindGen -s src org.jibx.starter.Order";

	public static void main(String[] args) throws JiBXException, IOException {
		BindGenCommandLine parms = new BindGenCommandLine();
		if ((args.length > 0) && (parms.processArgs(args))) {

			BindGen gen = new BindGen(parms.getGlobal());
			gen.generate(parms.getAbstract(), parms.getExtraArgs());
			BindingHolder root = gen.finish(parms.getBindingName());
			List bindings = gen.validateFiles(parms.getGeneratePath(), parms.getLocator(), root);
			if (!parms.isBindingOnly()) {

				SchemaGen sgen = new SchemaGen(parms.getLocator(), parms.getGlobal(), parms.getUriNames());
				List schemas = sgen.generate(bindings);
				SchemaGen.writeSchemas(parms.getGeneratePath(), schemas);
			}
		} else {
			if (args.length > 0) {
				System.err.println("Terminating due to command line errors");
			} else {
				parms.printUsage();
			}
			System.exit(1);
		}

	}
}
