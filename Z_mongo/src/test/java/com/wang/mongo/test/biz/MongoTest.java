package com.wang.mongo.test.biz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.wang.mongo.commons.utils.JsonUtils;
import com.wang.mongo.domain.Person;
import com.wang.mongo.test.SpringTester;

/**
 * mongdb的增删改查测试
 * 
 * @author wxe
 * @since 1.0.0
 */
public class MongoTest extends SpringTester {

	@Resource
	private MongoTemplate mongoTemplate;
	
	/**
	 * 添加集合
	 */
	@Test
	public void testAddDoc() {
		Person p = new Person("Joe", 34);
		Person p2 = new Person("Joe", 34);
		Person p11 = new Person("zhangsan", 20);
		Person p12 = new Person("zhangsan2", 21);
		Person p13 = new Person("zhangsan3", 23);
		List<Person> list = new ArrayList<Person>();
		list.add(p11);
		list.add(p12);
		list.add(p13);
		this.mongoTemplate.insert(p);// 默认保存在person集合中(与类名称一致)
		this.mongoTemplate.insert(p2, "person2");// 指定保存在person2集合中
		this.mongoTemplate.insertAll(list);// 默认保存在person集合中(与类名称一致)
	}
	/**
	 * 条件查找
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testFindDoc(){
		Person person = this.mongoTemplate.findById("58f5a9f2bc1cacdaf88fca59", Person.class);
		System.out.println("根据id查找的person---------->:"+person.toString());
		
		Query query = new Query(new Criteria().where("id").in("58f5a9f2bc1cacdaf88fca59"));
		Person findByQuery = this.mongoTemplate.findOne(query, Person.class);
		System.out.println("根据query条件查询的person:----------->"+findByQuery.toString());
		
		query = new Query(new Criteria().where("age").lt(23));
		List<Person> personList = this.mongoTemplate.find(query, Person.class);
		System.out.println(" 小于23岁的文档：" + JsonUtils.toJson(personList));
	}
	
	/**
	 * 更新文档
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testUpdateDoc(){
//		Query query = new Query(new Criteria().where("id").in("58f5a9f2bc1cacdaf88fca59"));
//		Update update = new Update().set("name", "doofus");
//		Person person = this.mongoTemplate.findAndModify(query, update, Person.class);//返回修改之前的文档
//		System.out.println("返回修改之前的person:---------->"+person.toString());
//		
//		Person personAfter = this.mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Person.class);//返回修改之后的文档
//		System.out.println("返回修改之后的personAfter:--------->"+personAfter.toString());
		
		Query lessThanQuery = new Query(new Criteria().where("age").lt(25));
		List<Person> personListBefore = this.mongoTemplate.find(lessThanQuery, Person.class);
		System.out.println(" 未修改之前：" + JsonUtils.toJson(personListBefore));
//		Update updateOnd = new Update().inc("age", 1);
//		WriteResult result = this.mongoTemplate.updateFirst(lessThanQuery, updateOnd, Person.class);
//		System.out.println("多条文档，只修改第一条之后："+JsonUtils.toJson(result));
		
		// 修改符合条件时如果不存在则添加
		Update updateOnd = new Update().inc("age", 1);
		WriteResult result = this.mongoTemplate.updateMulti(lessThanQuery, updateOnd, Person.class);
		System.out.println("多条文档，修改所有文档之后："+JsonUtils.toJson(result));
		
		
		
		
	}
	/**
	 * 删除集合
	 */
	@Test
	public void testDeleteDoc(){
		this.mongoTemplate.dropCollection("person2");
	}
	
}
