package com.leftso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leftso.entity.Account;
import com.leftso.repository.AccountRepository;

@SpringBootApplication()
@EnableAutoConfiguration()
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	//-----------------下面代码处理初始化一个用户-------------
	//用户名:leftso 用户密码:111aaa 用户角色:ROLE_USER
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	public void init(){
		try {
			Account account=new Account();
			account.setName("leftso");
			account.setPassword("111aaa");
			account.setRoles(new String []{"ROLE_USER"});
			accountRepository.deleteAll();
			accountRepository.save(account);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
