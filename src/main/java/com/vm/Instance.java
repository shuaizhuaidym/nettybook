package com.vm;
/**
 * 测试每次 new的示例的hashcode是否一致
 * @author yanming_dai
 * @date 2016年10月21日
 */
public class Instance {
	public static void main(String[] args) {
		Instance i = new Instance();
		System.out.println(i);

		Instance j = new Instance();
		System.out.println(j);
		
		Instance k = new Instance();
		System.out.println(k.toString());
	}
}