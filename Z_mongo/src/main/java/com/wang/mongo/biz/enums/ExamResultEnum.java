package com.wang.mongo.biz.enums;
/**
 * 测评结果
 * @author wxe
 * @since 1.0.0
 */
public enum ExamResultEnum {
	PASS(1,"通过"),
	UNPASS(2,"未通过");
	
	private int key;
	private String name;
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private ExamResultEnum(int key, String name) {
		this.key = key;
		this.name = name;
	}

}
