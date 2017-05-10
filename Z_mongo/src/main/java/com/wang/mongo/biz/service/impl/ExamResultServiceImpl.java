package com.wang.mongo.biz.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wang.mongo.base.Constants;
import com.wang.mongo.base.ResponseCode;
import com.wang.mongo.base.service.BaseMongoService;
import com.wang.mongo.biz.enums.ExamResultEnum;
import com.wang.mongo.biz.modle.ExamResultModel;
import com.wang.mongo.biz.service.ExamAnswerStatisticsService;
import com.wang.mongo.biz.service.ExamPaperService;
import com.wang.mongo.biz.service.ExamResultService;
import com.wang.mongo.commons.utils.JsonUtils;
import com.wang.mongo.domain.ExamAnswer;
import com.wang.mongo.domain.ExamAnswerFailUser;
import com.wang.mongo.domain.ExamAnswerStatistics;
import com.wang.mongo.domain.ExamPaper;
import com.wang.mongo.domain.ExamPaperPart;
import com.wang.mongo.domain.ExamPaperProblem;
import com.wang.mongo.domain.ExamResult;
import com.wang.mongo.domain.ExamResultHistory;

/**
 * 答题结果处理
 * @author wxe
 * @since 1.0.0
 */
@Service
@Slf4j
public class ExamResultServiceImpl implements ExamResultService {
	@Autowired
	private ExamPaperService examPaperService;
	
	@Autowired
	private BaseMongoService baseMongoService;
	
	@Autowired 
	private ExamAnswerStatisticsService statisticsService;
	
	private volatile int count = 1; //答题次数
	private volatile int monthTotalCount = 1;//月答题次数

	@Override
	public Map<String, Object> process(ExamResultModel resultModel) throws IllegalAccessException,InvocationTargetException{
		Map<String, Object> maps = new HashMap<String, Object>();
		ExamPaper examPaper = examPaperService.findById(resultModel.getPaperId());
		
		List<ExamResult> examedResults = queryExits(resultModel);//用户针对此问卷是否有测试记录
		log.info("exits exam result contains {}",JsonUtils.toJson(examedResults));
		int examedSize = examedResults.size();
		ExamResultEnum resultEnum = null;
		if (examedSize > 0 ) {
			//已经答题过，将其作为历史答题记录
			ExamResult examedResult = examedResults.get(0);
			resultEnum = examedResult.getResult();
			ExamResultHistory resultHistory = new ExamResultHistory();
			BeanUtils.copyProperties(resultHistory, examedResult);
			resultHistory.setId(null);
			count = examedResult.getTotalCount() + 1;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMM");
			//和当月比较，判断本月测试次数
			if(sdf.format(examedResult.getCreateTime()).equals(sdf.format(new Date()))){
				monthTotalCount = examedResult.getMonthTotalCount() + 1;
			}
			
			if (monthTotalCount > Constants.EXAM_MONTH_LIMIT_COUNT) {
				maps.put(ResponseCode.EXAM_OUT_COUNT.getCode(), ResponseCode.EXAM_OUT_COUNT.getDesc());
				return maps;
			}
			
//			baseMongoService.save(resultHistory, ExamResultHistory.class);//添加历史记录
//			baseMongoService.delete(examedResult, ExamResult.class);//删除最新记录表
			
		}
		ExamResult newResult = new ExamResult();
		
		//即将保存的答题结果
		newResult.setCreateTime(new Date());
		newResult.setPaperId(resultModel.getPaperId());
		newResult.setUserId(resultModel.getUserId());
		newResult.setUserName(resultModel.getUserName());
		newResult.setUserName(resultModel.getUserLoginName());
		newResult.setTotalCount(count);
		newResult.setMonthTotalCount(monthTotalCount);
		newResult.setPaperId(resultModel.getPaperId());
		newResult.setBasePoint(Constants.EXAM_LIMT_PASS_POINT);
		newResult.setMonthLimitCount(Constants.EXAM_MONTH_LIMIT_COUNT);
		newResult.setPaperVersion(examPaper.getVersion());
		
		List<ExamAnswer> answerList = Lists.newArrayList();
		List<ExamPaperPart> partList = examPaper.getParts();
		log.info("paper part is {}",JsonUtils.toJson(partList));
		for (int i = 0; i < partList.size(); i++) {
			switch (i) {
			case 0:
				//单项选择题
				analysisResult(answerList, partList.get(i).getProblems(), resultModel.getExamRadio(), newResult);
				break;
			case 1:
				//多项选择题
				analysisResult(answerList, partList.get(i).getProblems(), resultModel.getExamMultiple(), newResult);
				break;
			default:
				break;
			}
		}
		
		newResult.getAnswerList().addAll(answerList);
		if (newResult.getPoints().compareTo(new BigDecimal(Constants.EXAM_LIMT_PASS_POINT)) >= 0) {
			resultEnum = ExamResultEnum.PASS;
		} else {
			resultEnum = ExamResultEnum.UNPASS;
		}
		
		newResult.setResult(resultEnum);
		log.info("new exam result :{}",JsonUtils.toJson(newResult));
//		save(newResult);
		
		ExamAnswerStatistics  answerStatistics = createAnswerStatistics(newResult, resultModel);
		
		
		return null;
	}
	
	
	@SuppressWarnings("static-access")
	private List<ExamResult> queryExits(ExamResultModel resultModel){
		Query query = new Query();
		Criteria criteria = new Criteria().where("userId").is(resultModel.getUserId()).and("paperId").is(resultModel.getPaperId());
		query.addCriteria(criteria);
		query.with(new Sort(Direction.DESC, "createTime"));
		return baseMongoService.findByQuery(query, ExamResult.class);
	}

	@Override
	public Page<ExamResult> findPage(ExamResult answerResult,Pageable pageAble) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExamResult findById(String id) {
		return baseMongoService.findById(id, ExamResult.class);
	}


	@Override
	public void analysisResult(List<ExamAnswer> answerList,List<ExamPaperProblem> problemList, List<String> list,ExamResult result) {
		int size = problemList.size();
		for (int j = 0; j < size; j++) {
			ExamAnswer answer = new ExamAnswer();
			String rightAnswer = answer.getRightAnswer();
			ExamPaperProblem problem = problemList.get(j);
			String userAnswer = list.get(j);//用户答案
			answer.setUserAnswer(userAnswer);
			answer.setRightAnswer(rightAnswer);
			answer.setPoint(BigDecimal.ZERO);
			result.setPoints(BigDecimal.ZERO);
			if (userAnswer.equals(userAnswer)) {
				answer.setRighFlag(true);
				BigDecimal point = answer.getPoint().add(problem.getScore());//总分
				result.setPoints(result.getPoints().add(point));//给页面展示的总分
			} else {
				answer.setRighFlag(false);
			}
			
			answerList.add(answer);
		}
	}
	//答案统计
	public ExamAnswerStatistics createAnswerStatistics(ExamResult newResult,ExamResultModel resultModel){
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("paperId").is(newResult.getPaperId());
		criteria.and("paperVersion").is(newResult.getPaperVersion());
		query.addCriteria(criteria);
		ExamAnswerStatistics answerStatistics = baseMongoService.findOne(query, ExamAnswerStatistics.class);//统计里面一个版本的问卷只有一个文档
		ExamAnswerFailUser failUser = new ExamAnswerFailUser();
		failUser.setUserId(resultModel.getUserId());
		failUser.setUserName(resultModel.getUserName());
		failUser.setUsrLoginName(resultModel.getUserLoginName());
		if (answerStatistics == null) {
			answerStatistics = new ExamAnswerStatistics();
			answerStatistics.setPaperId(newResult.getPaperId());
			answerStatistics.setPaperTitle(newResult.getPaperTitle());
			answerStatistics.setPaperVersion(newResult.getPaperVersion());
			answerStatistics.setTotalCount(1);
			if (ExamResultEnum.PASS.equals(newResult.getResult())) {
				answerStatistics.setPassCount(1);
				answerStatistics.setUnPassCount(0);
			} else {
				answerStatistics.setPassCount(0);
				answerStatistics.setUnPassCount(1);
				answerStatistics.getFailUserList().add(failUser);
			}
//			statisticsService.save(answerStatistics);
		} else {
			
		}
		
		
		return null;
	}

	@Override
	public ExamResult save(ExamResult result) {
		return baseMongoService.save(result, ExamResult.class);
	}
	
}
