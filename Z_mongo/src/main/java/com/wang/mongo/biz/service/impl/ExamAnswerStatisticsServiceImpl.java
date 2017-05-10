package com.wang.mongo.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.wang.mongo.base.service.BaseMongoService;
import com.wang.mongo.biz.service.ExamAnswerStatisticsService;
import com.wang.mongo.domain.ExamAnswerStatistics;

/**
 * 结果统计service
 * @author wxe
 * @since 1.0.0
 */
@Service
public class ExamAnswerStatisticsServiceImpl implements ExamAnswerStatisticsService {
	@Autowired
	private BaseMongoService baseMongoService;

	@Override
	public Page<ExamAnswerStatistics> findPage(ExamAnswerStatistics answerStatistics,Pageable pageable) {
		Query query = new Query();
		Page<ExamAnswerStatistics> statistics = baseMongoService.findPage(pageable, query, ExamAnswerStatistics.class);
		return statistics;
	}

	@Override
	public ExamAnswerStatistics save(ExamAnswerStatistics answerStatistics) {
		return baseMongoService.save(answerStatistics, ExamAnswerStatistics.class);
	}


}
