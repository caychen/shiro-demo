package org.com.cay.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Cay on 2018/6/6.
 */
public class AuthenticationTest {

	SimpleAccountRealm realm = new SimpleAccountRealm();

	@Before
	public void before(){
		realm.addAccount("caychen", "123456", "admin");
	}

	@Test
	public void testAuthentication(){
		//1、构建SecurityManager环境
		DefaultSecurityManager manager = new DefaultSecurityManager();
		manager.setRealm(realm);

		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(manager);
		Subject subject = SecurityUtils.getSubject();

		AuthenticationToken token = new UsernamePasswordToken("caychen", "123456");
		subject.login(token);

		System.out.println("是否认证：" + subject.isAuthenticated());

		subject.checkRole("admin");

		subject.logout();

		System.out.println("是否认证：" + subject.isAuthenticated());
	}
}
