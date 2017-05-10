package com.wang.mongo.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;
import com.wang.mongo.base.BaseModel;
import com.wang.mongo.biz.enums.ExamResultEnum;

/**
 * 历史答案
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Document(collection = "tb_exam_result_history")
@Data
@EqualsAndHashCode(callSuper=false)
public class ExamResultHistory extends BaseModel {
	@Id
	private ObjectId id;
	/**
	 * 试卷
	 */
	private String paperId;
	/**
	 * 试卷标题
	 */
	private String paperTitle;
	/**
	 * 问卷历史版本号
	 */
	private String version;
	/**
	 * 得分
	 */
	private BigDecimal points;
	/**
	 * 测评结果：通过与未通过
	 */
	private ExamResultEnum result;
	/**
	 * 基数分数线
	 */
	private int basePoint;
	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户登录名
	 */
	private String userLoginName;
	/**
	 * 答题时间
	 */
	private Date createTime;
	/**
	 * 答题总次数
	 */
	private Integer totalCount;
	/**
	 * 本月答题次数
	 */
	private Integer monthTotalCount;
	/**
	 * 本月答题限制次数
	 */
	private Integer monthLimitCount;
	/**
	 * 答案
	 */
	List<ExamAnswer> answerList = Lists.newArrayList();
}
