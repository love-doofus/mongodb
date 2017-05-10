package com.wang.mongo.test.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.wang.mongo.biz.modle.ExamResultModel;
import com.wang.mongo.biz.service.ExamResultService;
import com.wang.mongo.test.SpringTester;

/**
 * @author wxe
 * @since 1.0.0
 */
public class ExamAnswerTest extends SpringTester {
	@Autowired
	private ExamResultService examResultService;
	
	@Test
	public void testProcess() throws IllegalAccessException, InvocationTargetException{
		ExamResultModel model = new ExamResultModel();
		model.setPaperId("58f880edcb3a661509286906");
		model.setUserId("1");
		model.setUserName("张三");
		model.setUserLoginName("zhangsan");
		
		List<String> radio = Lists.newArrayList("B","D","D","A","D","D","A","B","C","B","A","C");
		List<String> mult = Lists.newArrayList("A,B,C,D,E,F,G,H","B,D","A,B,C","B,C","A,D,E","A,B,C,D,E,F","B,D,H");
		model.getExamRadio().addAll(radio);
		model.getExamMultiple().addAll(mult);
		
		examResultService.process(model);
		
		
	}

}
