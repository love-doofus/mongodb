package com.wang.mongo.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;
import com.wang.mongo.base.BaseModel;

/**
 * 试卷部分
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Document
@Data
@EqualsAndHashCode(callSuper=false)
public class ExamPaperPart extends BaseModel{
	/**
	 * 标题（比如：第一部分）
	 */
	private String name;
	/**
	 * 题目个数
	 */
	private Integer problemNumber;
	/**
	 * 总的选项
	 */
	private BigDecimal score;
	/**
	 * 总分
	 */
	private BigDecimal totalPoints;
	/**
	 * 题目
	 */
	private List<ExamPaperProblem> problems = Lists.newArrayList();
	

}
