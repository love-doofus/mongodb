package com.wang.mongo.base.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

/**
 * 
 * @author wxe
 * @since 1.0.0
 */
public interface BaseMongoService {
	/**
	 * 根据id查找文档
	 * @param id
	 * @param documentClass
	 * @return
	 */
	<T> T findById(Object id,Class<T> documentClass);
	/**
	 * 根据id到指定集合中查找文档
	 * @param id
	 * @param collectionName
	 * @param documentClass
	 * @return
	 */
	<T> T findById(Object id,String collectionName,Class<T> documentClass);
	/**
	 * 查找所有文档
	 * @param documentClass
	 * @return
	 */
	<T> List<T> findAll(Class<T> documentClass);
	/**
	 * 查找指定集合中的所有文档，集合相当于表
	 * @param collectionName
	 * @param documentClass
	 * @return
	 */
	<T> List<T> findAll(String collectionName,Class<T> documentClass);
	/**
	 * 根据条件查询单个文档
	 * @param query
	 * @param documentClass
	 * @return
	 */
	<T> T findOne(Query query,Class<T> documentClass);
	/**
	 * 根据条件查询文档列表
	 * @param query
	 * @param documentClass
	 * @return
	 */
	<T> List<T> findByQuery(Query query,Class<T> documentClass);
	/**
	 * 分页查询
	 * @param page
	 * @param query
	 * @param documentClass
	 * @return
	 */
	<T> Page<T> findPage(Pageable pageable,Query query,Class<T> documentClass);
	/**
	 * 查询总量
	 * @param query
	 * @param documentClass
	 * @return
	 */
	<T> Long queryCount(Query query,Class<T> documentClass);
	/**
	 * 查询总量
	 * @param query
	 * @param documentClass
	 * @return
	 */
	<T> Long queryCount(Query query,String collectionName);
	/**
	 * 保存文档
	 * @param document
	 * @param documentClass
	 * @return
	 */
	<T> T save(T document,Class<T> documentClass);
	/**
	 * 删除文档
	 * @param document
	 * @param documentClass
	 */
	<T> void delete(T document,Class<T> documentClass);
	/**
	 * 根据条件删除
	 * @param query
	 * @param documentClass
	 */
	<T> void deleteByQuery(Query query,Class<T> documentClass);
	/**
	 * 根据条件更新,默认实现返回跟新之后数据
	 * @param query
	 * @param update
	 * @param documentClass
	 * @return
	 */
	<T> WriteResult update(Query query, Update update,Class<T> documentClass);
	/**
	 * 更新查询sort后的第一条记录
	 * @param query
	 * @param update
	 * @param documentClass
	 * @return
	 */
	<T> T updateFirst(Query query, Update update,Class<T> documentClass);
	/**
	 * 通过id更新文档
	 * @param id
	 * @param document
	 * @param documentClass
	 * @return
	 */
	<T> T updateById(String id,T document,Class<T> documentClass);

}
