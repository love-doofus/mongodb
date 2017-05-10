package com.wang.mongo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.wang.mongo.base.BaseModel;

/**
 * 未通过的人
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class ExamAnswerFailUser extends BaseModel{
	/**
	 * 失败用户id
	 */
	private String userId;
	/**
	 * 失败用户姓名
	 */
	private String userName;
	/**
	 * 失败用户登录名
	 */
	private String usrLoginName;
}
