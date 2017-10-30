package com.coding;

import java.io.Serializable;
/**
 * http://blog.csdn.net/kingsonyoung/article/details/50550412
 * @author yanming_dai
 * @date 2017年4月19日
 */
public class Student implements Serializable {
	private static final long serialVersionUID = 5214624260363659622L;
	String name;
	String classs;
	int age;

	@Override
	public String toString() {
		return "Student [name=" + name + ", classs=" + classs + ", age=" + age + "]";
	}

	public Student(String name, String classs, int age) {
		super();
		this.name = name;
		this.classs = classs;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClasss() {
		return classs;
	}

	public void setClasss(String classs) {
		this.classs = classs;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
