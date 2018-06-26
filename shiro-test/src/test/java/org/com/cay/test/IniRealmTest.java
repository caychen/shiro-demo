package org.com.cay.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by Cay on 2018/6/6.
 */
public class IniRealmTest {

	@Test
	public void testAuthentication(){

		IniRealm realm = new IniRealm("classpath:user.ini");

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

		subject.checkPermission("user:delete");

		subject.logout();

		System.out.println("是否认证：" + subject.isAuthenticated());
	}
}
