package com.wang.mongo.biz.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wang.mongo.domain.ExamPaper;

/**
 * @author wxe
 * @since 1.0.0
 */
public interface ExamPaperService {
	/**
	 * 分页查询
	 * @param page
	 * @param pageAble
	 * @return
	 */
	Page<ExamPaper> findPage(ExamPaper page,Pageable pageAble);
	/**
	 * 读取excel
	 * @param page
	 * @param uploadedInputStream
	 * @return
	 */
	ExamPaper importExcel(ExamPaper page,InputStream uploadedInputStream);
	/**
	 * 保存试卷
	 * @param page
	 * @return
	 */
	ExamPaper save(ExamPaper page);
	/**
	 * 检查标题
	 * @param title
	 * @return
	 */
	List<ExamPaper> findByTitle(String title);
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	ExamPaper findById(String id);

}
