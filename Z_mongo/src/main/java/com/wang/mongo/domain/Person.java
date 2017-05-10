package com.wang.mongo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wxe
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class Person {

	private String id;
	private String name;
	private int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public Person(String id,String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
