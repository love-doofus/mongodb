package com.wang.mongo.test.biz;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.wang.mongo.base.service.BaseMongoService;
import com.wang.mongo.commons.utils.JsonUtils;
import com.wang.mongo.domain.Person;
import com.wang.mongo.test.SpringTester;

/**
 * @author wxe
 * @since 1.0.0
 */
public class MongoServiceTest extends SpringTester {
	
	@Autowired
	private BaseMongoService mongoService;
	
	@Test
	public void testFindById(){
		Person person = mongoService.findById("58f5a9f2bc1cacdaf88fca57", Person.class);
		System.out.println("findById----"+JsonUtils.toJson(person));
		//结果：findById----{"age":34,"id":"58f5a9f2bc1cacdaf88fca57","name":"Joe"}
		
	}
	
	@Test
	public void testSave(){
		for (int i = 0; i < 11; i++) {
			Person person = new Person(""+i,"wang"+i,32+i);
			mongoService.save(person, Person.class);
		}
	}
	
	@Test
	public void testFindAll(){
		List<Person> list = mongoService.findAll(Person.class);
		System.out.println("findAll--------"+JsonUtils.toJson(list));
		//findAll--------[{"age":32,"id":"0","name":"wang0"},{"age":33,"id":"1","name":"wang1"},{"age":34,"id":"2","name":"wang2"},
		//{"age":35,"id":"3","name":"wang3"},{"age":36,"id":"4","name":"wang4"},{"age":37,"id":"5","name":"wang5"},{"age":38,"id":"6","name":"wang6"},
		//{"age":39,"id":"7","name":"wang7"},{"age":40,"id":"8","name":"wang8"},{"age":41,"id":"9","name":"wang9"},{"age":42,"id":"10","name":"wang10"}]
	}
	
	@Test
	public void testFindAllByCollection(){
		List<Person> list = mongoService.findAll("person", Person.class);
		System.out.println("findAllByCollection--------"+JsonUtils.toJson(list));
	}
	
	@Test
	public void testFindOne(){
		Criteria criteria = new Criteria();
		Query query = new Query().addCriteria(criteria.and("name"));
		Person person = mongoService.findOne(query, Person.class);
		System.out.println("findOne---------"+JsonUtils.toJson(person));
		//结果：findOne---------{"age":33,"id":"1","name":"wang1"}
	}
	
	@Test
	@SuppressWarnings("static-access")
	public void testFindByQuery(){
		Criteria criteria = new Criteria();
		Query query = new Query().addCriteria(criteria.where("name").regex("wang"));
		List<Person> list = mongoService.findByQuery(query, Person.class);
		System.out.println("findOne---------"+JsonUtils.toJson(list));
	}
	
	@Test
	public void testFindPage(){
		Pageable pageable = new PageRequest(0, 10);
		Query query = new Query();
		Page<Person> personPage = mongoService.findPage(pageable, query, Person.class);
		System.out.println("findPage---------"+JsonUtils.toJson(personPage));
	}
	
	@Test
	public void testQueryCount(){
		Query query = new Query();
		Long total = mongoService.queryCount(query, Person.class);
		System.out.println("queryCount----------"+total);
		//结果queryCount----------11
	}
	
	@Test
	public void testDelete(){
		Person person = mongoService.findById("15", Person.class);
		System.out.println("原始数据：--------"+JsonUtils.toJson(person));
		mongoService.delete(person, Person.class);
		person = mongoService.findById("15", Person.class);
		System.out.println("删除后数据---------"+JsonUtils.toJson(person));
		//结果：原始数据：--------null   删除后数据---------null
		//从结果上来看，就算删除的数据不存在也不会抛异常。
		Person exitPerson = mongoService.findById("9", Person.class);
		System.out.println("原始数据：--------"+JsonUtils.toJson(exitPerson));
		mongoService.delete(exitPerson, Person.class);
		exitPerson = mongoService.findById("9", Person.class);
		System.out.println("删除后数据---------"+JsonUtils.toJson(exitPerson));
		//结果 原始数据：--------{"age":41,"id":"9","name":"wang9"}  删除后数据---------null
	}
	
	

}
