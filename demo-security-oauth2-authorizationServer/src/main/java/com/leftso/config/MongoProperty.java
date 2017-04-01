package com.leftso.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * mongodb的配置文件载入
 * 
 * @author leftso
 *
 */
@Component
@ConfigurationProperties(prefix = "db")
@PropertySource("classpath:mongo.properties")
public class MongoProperty {
	/** 主机地址IP+端口 **/
	private String host;
	/** 数据库名称 ***/
	private String name;
	/** 数据库用户（非必须） ***/
	private String user;
	/** 数据库密码 **/
	private String pwd;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
