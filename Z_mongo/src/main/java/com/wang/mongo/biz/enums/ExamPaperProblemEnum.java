package com.wang.mongo.biz.enums;


/**
 * 题目类型：多选，单选，填空
 * @author wxe
 * @since 1.0.0
 */
public enum ExamPaperProblemEnum {
	RADIO(0, "单项"), 
	MULTIPLE(1, "不定项"), 
	FILL(2, " 填空"), 
	SHORTANSWER(3, "简答");
	
	private int key;
	private String name;
	
	private ExamPaperProblemEnum(int key, String name){
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
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static ExamPaperProblemEnum get(String name){
		ExamPaperProblemEnum[] enums = ExamPaperProblemEnum.values();
		for (ExamPaperProblemEnum examPaperProblemEnum : enums) {
			if(name.contains(examPaperProblemEnum.getName())){
				return examPaperProblemEnum;
			}
		}
		return null;
	}
	
}

