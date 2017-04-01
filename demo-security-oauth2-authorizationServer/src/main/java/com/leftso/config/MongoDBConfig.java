package com.leftso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * mongodb 配置类
 * 
 * @author leftso
 *
 */
@Configuration
public class MongoDBConfig {
	@Autowired
	MongoProperty mongoProperties;

	/**
	 * 注入mongodb的工厂类
	 * 
	 * @return
	 */
	@Bean
	public MongoDbFactory mongoDbFactory() {
		// uri格式mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
		String mongoURI = "mongodb://" + mongoProperties.getHost();
		if (!StringUtils.isEmpty(mongoProperties.getUser())) {
			mongoURI = "mongodb://" + mongoProperties.getUser() + ":" + mongoProperties.getPwd() + "@"
					+ mongoProperties.getHost();
		}
		// 为了方便实现mongodb多数据库和数据库的负债均衡这里使用url方式创建工厂
		MongoClientURI mongoClientURI = new MongoClientURI(mongoURI);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongoClient, mongoProperties.getName());
		// 注意:以下构造函数已经弃用:
		// SimpleMongoDbFactory(com.mongodb.Mongo mongo, String databaseName);
		// 弃用版本1.7
		// SimpleMongoDbFactory(com.mongodb.Mongo mongo, String databaseName,
		// UserCredentials credentials);弃用版本1.7
		// SimpleMongoDbFactory(com.mongodb.Mongo mongo, String databaseName,
		// UserCredentials credentials, String
		// authenticationDatabaseName);弃用版本1.7
		// SimpleMongoDbFactory(com.mongodb.MongoURI uri);弃用版本1.7
		return mongoDbFactory;
	}

	/**
	 * 获取操作实例
	 * 
	 * @param mongoDbFactory
	 * @return
	 */
	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
		return new MongoTemplate(mongoDbFactory);
	}

}
