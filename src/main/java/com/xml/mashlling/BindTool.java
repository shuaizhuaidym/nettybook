package com.xml.mashlling;

import java.io.IOException;

import org.jibx.binding.Compile;
import org.jibx.binding.generator.BindGen;
import org.jibx.runtime.JiBXException;

/**
 * Usage: java org.jibx.binding.Compile [-b] [-l] [-v] binding1 binding2 ...
 * where: -b turns on BCEL verification (debug option), -l turns on test loading
 * of modified or generated classes for validation, and -v turns on verbose
 * output The bindingn files are different bindings to be compiled.
 * 
 * 
 * Usage: java org.jibx.binding.generator.BindGen [options] class1 class2 ...
 * where options are: -a force abstract mappings for specified classes -b name
 * generated root binding name (default is 'binding.xml') -c path input
 * customizations file -m force concrete mappings for specified classes -n
 * uri=name,... schema namespace URI and file name pairs (default generates file
 * names from URIs) -o binding generation only flag, skip schema generation -p
 * path,... class loading paths -s path,... source paths -t path target
 * directory for generated output (default is current directory) -v verbose
 * output flag -w wipe all existing files from generation directory (ignored if
 * current directory) The class# files are different classes to be included in
 * the binding (references from these classes will also be included).
 * 
 * @author yanming_dai
 * @date 2018年6月20日
 */
public class BindTool {
	public static void main(String[] args) throws JiBXException, IOException {

		genBindFiles();
		compile();
	}

	private static void compile() {
		String[] args = new String[2];

		// 打印生成过程的详细信息。可选
		args[0] = "-v";

		// 指定 binding 和 schema 文件的路径。必须
		args[1] = "./src/main/java/com/xml/binding/binding.xml";

		Compile.main(args);
	}

	private static void genBindFiles() throws JiBXException, IOException {
		String[] args = new String[9];

		// 指定pojo源码路径（指定父包也是可以的）。必须
		args[0] = "-s";
		args[1] = "src";

		// 自定义生成的binding文件名，默认文件名binding.xml。可选
		args[2] = "-b";
		args[3] = "binding.xml";

		// 打印生成过程的一些信息。可选
		args[4] = "-v";

		// 如果目录已经存在，就删除目录。可选
		args[5] = "-w";

		// 指定输出路径。默认路径 .（当前目录,即根目录）。可选
		args[6] = "-t";
		args[7] = "./src/main/java/com/xml/binding";

		// 告诉 BindGen 使用下面的类作为 root 生成 binding 和 schema。必须
		args[8] = "com.xml.bean.Order";

		BindGen.main(args);
	}

}
