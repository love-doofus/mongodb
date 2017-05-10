package com.wang.mongo.biz.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.wang.mongo.base.ResponseCode;
import com.wang.mongo.base.service.BaseMongoService;
import com.wang.mongo.biz.enums.ExamPaperProblemEnum;
import com.wang.mongo.biz.enums.ExamPaperStatusEnum;
import com.wang.mongo.biz.service.ExamPaperService;
import com.wang.mongo.commons.utils.ExcelUtils;
import com.wang.mongo.commons.utils.JsonUtils;
import com.wang.mongo.domain.ExamPaper;
import com.wang.mongo.domain.ExamPaperOption;
import com.wang.mongo.domain.ExamPaperPart;
import com.wang.mongo.domain.ExamPaperProblem;

/**
 * 读取试卷并保存到mongodb中
 * @author wxe
 * @since 1.0.0
 */
@Service
@Slf4j
public class ExamPaperServiceImpl implements ExamPaperService {
	
	@Autowired
	private BaseMongoService mongoService;
	
	private static ThreadLocal<Map<String, Object>> localThread = new ThreadLocal<>();

	@SuppressWarnings("unchecked")
	@Override
	public Page<ExamPaper> findPage(ExamPaper page, Pageable pageAble) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		
		if (StringUtils.isNoneBlank(page.getBizType().getName())) {
			criteria.and("bizType").is(page.getBizType().getName());
		}
		if (StringUtils.isNotBlank(page.getCourseNo())) {
			criteria.and("courseNo").is(page.getCourseNo());
		}
		if (StringUtils.isNotBlank(page.getCourseName())) {
			criteria.and("courseName").is(page.getCourseName());
		}
		if (StringUtils.isNotBlank(page.getStatus().getName())) {
			criteria.and("status").is(page.getStatus().getName());
		}
		
		query.addCriteria(criteria);
		query.with(new Sort(Direction.DESC, "createTime"));//根据创建时间
		
		Page<ExamPaper> result = mongoService.findPage(pageAble, query, ExamPaper.class);
		if (result == null ) {
			return new PageImpl<ExamPaper>(Collections.EMPTY_LIST);
		}
		
		return result;
	}

	@Override
	public ExamPaper importExcel(ExamPaper paper, InputStream inputStream) {
		Map<String, Object> maps = new HashMap<>();
		//excel转换为list
		List<List<String>> list = Collections.unmodifiableList(excelToList(inputStream,maps));
		
		paper = createExamPaper(list, paper);
		//保存
		ExamPaper saved = save(paper);
		if (saved != null) {
			maps.put("success", ResponseCode.SUCCESS);
		} else {
			maps.put("failure", ResponseCode.FAILURE);
		}
		
		localThread.set(maps);
		return saved;
	}

	@Override
	public ExamPaper save(ExamPaper page) {
		return mongoService.save(page, ExamPaper.class);
	}
	
	private List<List<String>> excelToList(InputStream inputStream,Map<String, Object> maps){
		Workbook workbook = ExcelUtils.getWorkbook(inputStream);
		
		if (workbook == null) {
			maps.put("false", ResponseCode.FAILURE);
			localThread.set(maps);
			throw new RuntimeException("import excel error.");
		}
		
		List<List<String>> result = ExcelUtils.getExcelToList(workbook, 0, 0);//只有一个sheet
		if (result == null) {
			maps.put("false", ResponseCode.FAILURE);
			localThread.set(maps);
		}
		
		log.info("excel to list：{}",JsonUtils.toJson(result));
		return result;
	}
	/**
	 * list  to ExamPaper
	 * @param list
	 * @param paper
	 * @return
	 */
	private ExamPaper createExamPaper(List<List<String>> list,ExamPaper paper){
		String title = list.get(0).get(0);
		List<ExamPaper> paperList = findByTitle(title);
		
		for (ExamPaper paperObject : paperList) {
			paperObject.setStatus(ExamPaperStatusEnum.UPDATED);
			mongoService.updateById(paperObject.getId(), paperObject, ExamPaper.class);
		}
	
		paper.setVersion(paperList.size());//版本号
		paper.setTitle(title);
		//解析标题类似AT 003 <<公司介绍>> 试题，主要解析课程编号
		String[] strs = title.split("《");
		log.info("试卷标题：{}",JsonUtils.toJson(strs));
		paper.setCourseNo(strs[0]);
		paper.setCourseName(strs[1].split("》")[0]);
		
		ExamPaperProblemEnum type = ExamPaperProblemEnum.RADIO;
		List<ExamPaperPart> tempParts = Lists.newArrayList();
		List<ExamPaperProblem> tempProblems = Lists.newArrayList();
		List<ExamPaperOption> tempOptions = Lists.newArrayList();
		ExamPaperPart part = new ExamPaperPart();
		ExamPaperProblem problem = new ExamPaperProblem();
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal score = BigDecimal.valueOf(0);
		for (int i = 1; i < list.size(); i++) {
			
			List<String> row = list.get(i);//一行数据
			String firstTable = row.get(0);//第一格
			//如果区分试卷的构成部分，当无法获取到第一部分等描述时，跳出循环，也就是结束本次构成部分结束。
			if (StringUtils.isBlank(firstTable)) {
				continue;
			}
			
			boolean b=".".equals(firstTable.substring(1,2));
			String lastTable = row.get(5);//第六格
			//除去空的
			row.removeAll(Collections.singleton(null));
			row.removeAll(Collections.singleton(""));
			
			int len = row.size();
			for (int j = 0; j < len; j++) {
				if (len == 1 && !b) {
					
					if (tempOptions.size() != 0) {
						problem.setOptions(tempOptions);
						tempProblems.add(problem);
						problem = new ExamPaperProblem();
						tempOptions = Lists.newArrayList();
					}
					
					if (tempProblems.size() != 0) {
						part.setProblems(tempProblems);
						tempParts.add(part);
						part = new ExamPaperPart();
						tempOptions = Lists.newArrayList();
						tempProblems = Lists.newArrayList();
					}
					String partName = row.get(0);
					part.setName(partName);
					
					//识别构成部分的构成的类型，比如单选，多选，但是文中没有具体给出，只能匹配
					type = ExamPaperProblemEnum.get(partName);
					Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]+([0-9]*.?[0-9])[\u4e00-\u9fa5]+").matcher(partName);
					matcher.find();
					part.setProblemNumber(Integer.valueOf(matcher.group(1)));//题目个数
					matcher.find();
					score =  new BigDecimal(matcher.group(1));//每题分数
					if (score != null ) {
						part.setScore(score);//每题分数
					}
					matcher.find();
					BigDecimal totalPoints = new BigDecimal(matcher.group(1));
					part.setTotalPoints(totalPoints);//总分数
					total = total.add(totalPoints);
					
					i++;//跳过一行为描述性文字
				} else if (len == 2 && StringUtils.isNotBlank(lastTable)) {
					if (tempOptions.size() != 0) {
						problem.setOptions(tempOptions);
						tempProblems.add(problem);
						problem = new ExamPaperProblem();
						tempOptions = Lists.newArrayList();
					}
					//开始读题目
					problem.setTitle(row.get(0));
					problem.setType(type);
					problem.setAnswer(row.get(1));
					problem.setScore(score);
				} else {
					//开始读取选项
					for (String str : row) {
						ExamPaperOption option = new ExamPaperOption();
						option.setContent(str);
						int index = str.indexOf('.');
						if (index != -1) {
							option.setOptionNo(str.substring(0,index));
						}
						tempOptions.add(option);
//						option.setScore();//选项分数，暂时保留
					}
				}
			}
			
			if (tempProblems.size() > 0) {
				problem.setOptions(tempOptions);
				tempProblems.add(problem);
				part.setProblems(tempProblems);
				tempParts.add(part);
				part = new ExamPaperPart();
				tempOptions = Lists.newArrayList();
				tempProblems = Lists.newArrayList();
			}
			
			paper.setParts(tempParts);
			paper.setCreateTime(new Date());
			paper.setTotalPoints(total);
			
		}
		
		log.info("the last paper:{}",JsonUtils.toJson(paper));
		
		return paper;
	}

	@Override
	public List<ExamPaper> findByTitle(String title) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("title").is(title);
		query.addCriteria(criteria);
		return mongoService.findByQuery(query, ExamPaper.class);
	}

	@Override
	public ExamPaper findById(String id) {
		return mongoService.findById(id,ExamPaper.class);
	}
	

}
