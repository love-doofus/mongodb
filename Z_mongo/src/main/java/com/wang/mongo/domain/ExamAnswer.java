package com.wang.mongo.domain;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.wang.mongo.base.BaseModel;

/**
 * 为用户反馈正确答案
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ExamAnswer extends BaseModel {
	/**
	 * 用户答案
	 */
	private String userAnswer;
	/**
	 * 是否正确
	 */
	private Boolean righFlag;
	/**
	 * 得分
	 */
	private BigDecimal point;
	/**
	 * 正确答案
	 */
	private String rightAnswer;
}
