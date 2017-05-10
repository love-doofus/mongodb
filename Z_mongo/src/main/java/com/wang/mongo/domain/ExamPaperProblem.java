package com.wang.mongo.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.google.common.collect.Lists;
import com.wang.mongo.base.BaseModel;
import com.wang.mongo.biz.enums.ExamPaperProblemEnum;

/**
 * 问题
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ExamPaperProblem extends BaseModel{
	/**
	 * 题目
	 */
	private String title;
	/**
	 * 正确答案
	 */
	private String answer;
	/**
	 * 题目分数
	 */
	private BigDecimal score;
	/**
	 * 选项
	 */
	private List<ExamPaperOption> options = Lists.newArrayList();
	/**
	 * 题目类型，多选，单选等
	 */
	private ExamPaperProblemEnum type;

}
