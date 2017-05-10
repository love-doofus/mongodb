package com.wang.mongo.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.mongodb.WriteResult;
import com.wang.mongo.base.service.BaseMongoService;

/**
 * mongodb的基本操作：增删改查
 * @author wxe
 * @since 1.0.0
 */
@Component
public class BaseMongoServiceImpl implements BaseMongoService {
	
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public <T> T findById(Object id, Class<T> documentClass) {
		return mongoOperations.findById(id, documentClass);
	}

	@Override
	public <T> T findById(Object id, String collectionName,Class<T> documentClass) {
		return mongoOperations.findById(id, documentClass, collectionName);
	}

	@Override
	public <T> List<T> findAll(Class<T> documentClass) {
		return mongoOperations.findAll(documentClass);
	}

	@Override
	public <T> List<T> findAll(String collectionName, Class<T> documentClass) {
		return mongoOperations.findAll(documentClass, collectionName);
	}

	@Override
	public <T> T findOne(Query query, Class<T> documentClass) {
		return mongoOperations.findOne(query, documentClass);
	}

	@Override
	public <T> List<T> findByQuery(Query query, Class<T> documentClass) {
		return mongoOperations.find(query, documentClass);
	}

	@Override
	public <T> Page<T> findPage(Pageable pageable, Query query,Class<T> documentClass) {
		Long total = queryCount(query,getCollectionName(documentClass));
		List<T> list = findByQuery(query.with(pageable), documentClass);
		
		return new PageImpl<T>(list, pageable, total);
	}

	@Override
	public <T> Long queryCount(Query query, Class<T> documentClass) {
		return mongoOperations.count(query, documentClass);
	}
	
	@Override
	public <T> Long queryCount(Query query, String collectionName) {
		return mongoOperations.count(query, collectionName);
	}

	@Override
	public <T> T save(T document, Class<T> documentClass) {
		mongoOperations.insert(document);
		return document;
	}

	@Override
	public <T> void delete(T document, Class<T> documentClass) {
		mongoOperations.remove(document);
	}

	@Override
	public <T> void deleteByQuery(Query query, Class<T> documentClass) {
		mongoOperations.remove(query, documentClass);
	}

	@Override
	public <T> WriteResult update(Query query, Update update,Class<T> documentClass) {
		Assert.isNull("update must be not empty!");
		return mongoOperations.updateMulti(query, update, documentClass);
	}

	@Override
	public <T> T updateFirst(Query query, Update update, Class<T> documentClass) {
		Assert.isNull("update must be not empty!");
		return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), documentClass);
	}

	@Override
	public <T> T updateById(String id, T document, Class<T> documentClass) {
		T oldDocument  = findById(id, documentClass);
		delete(oldDocument, documentClass);
		return save(document, documentClass);
	}

	private <T> String getCollectionName(Class<T> resultClass) {
		return ClassUtils.getShortNameAsProperty(resultClass);
	}
}
