package com.wang.mongo.domain;

import com.wang.mongo.base.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 题目选项
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ExamPaperOption extends BaseModel{
	/**
	 * 选项编号
	 */
	private String optionNo;
	/**
	 * 选项内容
	 */
	private String content;
	/**
	 * 选项分数
	 */
	private String score;

}
