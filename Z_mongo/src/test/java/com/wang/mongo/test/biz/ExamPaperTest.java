package com.wang.mongo.test.biz;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wang.mongo.biz.enums.ExamPaperBizEnum;
import com.wang.mongo.biz.enums.ExamPaperStatusEnum;
import com.wang.mongo.biz.service.ExamPaperService;
import com.wang.mongo.commons.utils.JsonUtils;
import com.wang.mongo.domain.ExamPaper;
import com.wang.mongo.test.SpringTester;

/**
 * 试卷测试
 * @author wxe
 * @since 1.0.0
 */
@Slf4j
public class ExamPaperTest extends SpringTester {
	@Autowired
	private ExamPaperService examPaperService;
	
	@Test
	public void testExcel() throws FileNotFoundException{
		File[] file = new File("e:/exam/").listFiles();
		assertNotNull(file);
		
		if (file.length == 1) {
			File f=file[0];
			ExamPaper paper = new ExamPaper();
			paper.setStatus(ExamPaperStatusEnum.UNPUBLISHED);
			paper.setCreateTime(new Date());
			paper.setBizType(ExamPaperBizEnum.CMS);
			paper.setLoginName("wxe");
			InputStream uploadedInputStream = new FileInputStream(f);
			examPaperService.importExcel(paper, uploadedInputStream);
		}
	}
	
	@Test
	public void testFindById(){
		String id = "58f81d7dcb3a305f3f636d38";
		ExamPaper paper = examPaperService.findById(id);
		log.info("find paper by id :{}",JsonUtils.toJson(paper));
	}

}
