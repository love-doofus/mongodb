package com.wang.mongo.biz.enums;
/**
 * 该问卷的业务类型
 * @author wxe
 * @since 1.0.0
 */
public enum ExamPaperBizEnum {
	CMS(1,"CMS管理"),
	AGENT(2,"代理商管理");
	
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
	private ExamPaperBizEnum(int key, String name) {
		this.key = key;
		this.name = name;
	}
	
}
