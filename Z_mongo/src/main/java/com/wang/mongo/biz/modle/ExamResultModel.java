package com.wang.mongo.biz.modle;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 答题结果
 * @author wxe
 * @since 1.0.0
 */
@Data
public class ExamResultModel {
	/**
	 * 问卷id
	 */
	private String paperId;
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
	 * 单选答案
	 */
	private List<String> examRadio=new ArrayList<String>();
	
	/**
	 * 多选答案
	 */
	private List<String> examMultiple=new ArrayList<String>();

}
