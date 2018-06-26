package org.com.cay.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Created by Cay on 2018/6/7.
 */
public class JdbcRealmTest {

	DruidDataSource dataSource = new DruidDataSource();
	{
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql:///test");
		dataSource.setUsername("root");
		dataSource.setPassword("admin");
	}

	@Test
	public void testAuthentication(){

		/**
		 *  使用JdbcRealm的时候，操作的数据库中至少需要三张表：分别是users,user_roles,roles_permissions
		 *  在验证的时候需要通过查询数据库来验证用户信息
		 */
		JdbcRealm realm = new JdbcRealm();
		realm.setPermissionsLookupEnabled(true);//默认为false，即用户角色对应的权限不会去查询
		realm.setDataSource(dataSource);

		//设置sql验证
		String sql1 = "select password from test_user where user_name = ?";
		realm.setAuthenticationQuery(sql1);

		String sql2 = "select role_name from test_user_role where user_name = ?";
		realm.setUserRolesQuery(sql2);

		String sql3 = "select permission_name from test_role_permission where role_name = (select role_name from test_user_role where user_name= = ?)";
		realm.setPermissionsQuery(sql3);

		//1、构建SecurityManager环境
		DefaultSecurityManager manager = new DefaultSecurityManager();
		manager.setRealm(realm);

		//2、主体提交认证请求
		SecurityUtils.setSecurityManager(manager);
		Subject subject = SecurityUtils.getSubject();

		AuthenticationToken token = new UsernamePasswordToken("caychen", "123456");
		subject.login(token);

		System.out.println("是否认证：" + subject.isAuthenticated());

		subject.logout();

		System.out.println("是否认证：" + subject.isAuthenticated());
	}
}
