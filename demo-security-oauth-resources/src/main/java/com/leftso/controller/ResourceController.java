package com.leftso.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/***
 * 受保护的资源服务
 * 
 * @author leftso
 *
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {
	/**
	 * 需要用户角色权限
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public String helloUser() {
		return "hello user";
	}

	/**
	 * 需要管理角色权限
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "admin", method = RequestMethod.GET)
	public String helloAdmin() {
		return "hello admin";
	}

	/**
	 * 需要客户端权限
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_CLIENT')")
	@RequestMapping(value = "client", method = RequestMethod.GET)
	public String helloClient() {
		return "hello user authenticated by normal client";
	}

	/**
	 * 需要受信任的客户端权限
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_TRUSTED_CLIENT')")
	@RequestMapping(value = "trusted_client", method = RequestMethod.GET)
	public String helloTrustedClient() {
		return "hello user authenticated by trusted client";
	}

	/**
	 * 返回当前用户的信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "principal", method = RequestMethod.GET)
	public Object getPrincipal() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal;
	}

	/**
	 * 返回用户的用户名
	 * 
	 * @return
	 */
	@RequestMapping(value = "roles", method = RequestMethod.GET)
	public Object getRoles() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}

}
