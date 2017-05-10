package com.wang.mongo.domain;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;
import com.wang.mongo.base.BaseModel;

/**
 * 结果统计
 * @author wxe
 * @since 1.0.0
 */
@SuppressWarnings("serial")
@Document(collection = "tb_exam_answer_statistics")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ExamAnswerStatistics extends BaseModel {
	@Id
	private Object id;
	
	private Integer totalCount;//总答题人数
	
	private Integer passCount;//通过人数
	
	private Integer unPassCount;//未通过人数
	
	private String paperId;//问卷id
	
	private Integer paperVersion;//问卷版本
	
	private String paperTitle;//问卷名称
	
	private List<ExamAnswerFailUser> failUserList = Lists.newArrayList();
}
