package com.wang.mongo.biz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wang.mongo.domain.ExamAnswerStatistics;

/**
 * @author wxe
 * @since 1.0.0
 */
public interface ExamAnswerStatisticsService {
	/**
	 * 分页查找
	 * @param answerStatistics
	 * @param pageable
	 * @return
	 */
	Page<ExamAnswerStatistics> findPage(ExamAnswerStatistics answerStatistics,Pageable pageable);
	
	/**
	 * 保存结果统计
	 * @param answerStatistics
	 * @return
	 */
	ExamAnswerStatistics save(ExamAnswerStatistics answerStatistics);

}
