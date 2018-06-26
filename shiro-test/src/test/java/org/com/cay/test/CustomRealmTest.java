package org.com.cay.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.com.cay.shiro.realm.CustomRealm;
import org.junit.Test;

/**
 * Created by Cay on 2018/6/7.
 */
public class CustomRealmTest {

	@Test
	public void testAuthentication(){
		CustomRealm realm = new CustomRealm();

		//1、构建SecurityManager环境
		DefaultSecurityManager manager = new DefaultSecurityManager();
		manager.setRealm(realm);

		//加密算法
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher("md5");
		matcher.setHashIterations(5);
		realm.setCredentialsMatcher(matcher);

		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(manager);
		Subject subject = SecurityUtils.getSubject();

		AuthenticationToken token = new UsernamePasswordToken("caychen", "123456");
//		AuthenticationToken token = new UsernamePasswordToken("admin", "111111");
		subject.login(token);

		System.out.println("是否认证：" + subject.isAuthenticated());

		//subject.checkRole("admin");
		subject.checkRole("admin");

		subject.checkPermission("admin:delete");

		subject.logout();

		System.out.println("是否认证：" + subject.isAuthenticated());
	}
}
