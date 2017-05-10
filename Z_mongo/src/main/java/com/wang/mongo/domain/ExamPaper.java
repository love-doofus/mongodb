package com.wang.mongo.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;
import com.wang.mongo.base.BaseModel;
import com.wang.mongo.biz.enums.ExamPaperBizEnum;
import com.wang.mongo.biz.enums.ExamPaperStatusEnum;

/**
 * 问卷
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Document(collection = "tb_exam_paper")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ExamPaper extends BaseModel {
	@Id
	private String id;
	/**
	 * 业务类型
	 */
	private ExamPaperBizEnum bizType;
	/**
	 * 课程编号
	 */
	private String courseNo;
	/**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 试卷标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 总分
	 */
	private BigDecimal totalPoints;
	/**
	 * 题目总数
	 */
	private int totalScore;
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 创建人登录名
	 */
	private String loginName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 版本号
	 */
	private Integer version;
	/**
	 * 试卷状态
	 */
	private ExamPaperStatusEnum status;
	/**
	 * 试卷构成部分
	 */
	private List<ExamPaperPart> parts = Lists.newArrayList();
}
