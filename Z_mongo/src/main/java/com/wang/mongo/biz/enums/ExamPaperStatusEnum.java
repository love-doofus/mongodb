package com.wang.mongo.biz.enums;
/**
 * @author wxe
 * @since 1.0.0
 */
public enum ExamPaperStatusEnum {
	UNPUBLISHED(0, "未发布"), 
	PUBLISHED(1, "已发布"), 
	DELETED(2, " 已删除"), 
	UPDATED(3, "已更新");

	private int key;
	private String name;
	
	private ExamPaperStatusEnum(int key, String name) {
		this.key = key;
		this.name = name;
	}

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

}
