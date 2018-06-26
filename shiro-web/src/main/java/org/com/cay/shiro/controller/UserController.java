package org.com.cay.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.com.cay.shiro.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Cay on 2018/6/7.
 */
@Controller
public class UserController {

	@PostMapping(value = "/formLogin", produces = "application/json;charset=UTF-8")
	public String login(User user) {

		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		try {
			token.setRememberMe(user.getRememberMe());
			subject.login(token);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return "redirect:/main";
	}

	@RequestMapping("/main")
	public String main(){
		return "main";
	}


	@RequiresRoles("admin")//必须具备admin角色才能去访问
	@RequestMapping("/testAdmin")
	@ResponseBody
	public String testAdmin(){
		return "test admin";
	}

	@RequiresRoles("user")//必须具备admin角色才能去访问
	@RequestMapping("/testUser")
	@ResponseBody
	public String testUser(){
		return "test user";
	}

	@RequestMapping("/testRole")
	@ResponseBody
	public String testRole(){
		return "test role";
	}

	@RequestMapping("/testRoleOrFilter")
	@ResponseBody
	public String testRoleOrFilter(){
		return "test rolesOrFilter";
	}

	@RequestMapping("/testRole1")
	@ResponseBody
	public String testRole1(){
		return "test role1";
	}
}
