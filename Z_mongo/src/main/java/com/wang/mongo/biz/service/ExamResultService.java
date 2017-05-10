package com.wang.mongo.biz.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wang.mongo.biz.modle.ExamResultModel;
import com.wang.mongo.domain.ExamAnswer;
import com.wang.mongo.domain.ExamPaperProblem;
import com.wang.mongo.domain.ExamResult;

/**
 * @author wxe
 * @since 1.0.0
 */
public interface ExamResultService {
	/**
	 * 保存用户答题结果
	 * @param answerResult
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	Map<String, Object> process(ExamResultModel resultModel) throws IllegalAccessException, InvocationTargetException;
	/**
	 * 查询分页
	 * @param answerResult
	 * @param pageAble
	 * @return
	 */
	Page<ExamResult> findPage(ExamResult answerResult,Pageable pageAble);
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	ExamResult findById(String id);
	/**
	 * 分析结果
	 * @param answerList
	 * @param problemList
	 * @param list
	 * @param result
	 */
	void analysisResult(List<ExamAnswer> answerList,List<ExamPaperProblem> problemList,List<String> list,ExamResult result);
	/**
	 * 统计用户答题结果
	 * @param result
	 * @param resultModel
	 * @return
	 */
	ExamResult save(ExamResult result);

}
