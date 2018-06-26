package org.com.cay.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.com.cay.shiro.dao.IUserDao;
import org.com.cay.shiro.vo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by Cay on 2018/6/7.
 */
public class CustomRealm extends AuthorizingRealm {

	@Autowired
	private IUserDao userDao;


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();

		//模拟从数据库中获取角色和权限
		Set<String> roles = getRolesByUsername(username);
		Set<String> permissions = new HashSet<>();
		roles.stream().forEach(role -> permissions.addAll(getPermissionsByUsername(role)));

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(roles);
		info.setStringPermissions(permissions);

		return info;
	}

	private Set<String> getPermissionsByUsername(String rolename) {
		List<String> pers = userDao.getPermissionsByUsername(rolename);
		return new HashSet<>(pers);
	}

	private Set<String> getRolesByUsername(String username) {
		System.out.println("从数据库中获取数据...");
		List<String> roles = userDao.getRolesByUsername(username);
		return new HashSet<>(roles);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		//从主体传过来的认证信息中，获取用户名
		String username = (String) token.getPrincipal();

		//获取密码
		String password = getPasswordByUsername(username);
		if (password == null) {
			return null;
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());
		info.setCredentialsSalt(ByteSource.Util.bytes(username));
		return info;
	}

	private String getPasswordByUsername(String username) {
		User user = userDao.getPasswordByUsername(username);
		if(user != null){
			return user.getPassword();
		}
		return null;
	}

	public static void main(String[] args) {
		Md5Hash md5Hash = new Md5Hash("123456", "user", 5);
		System.out.println(md5Hash.toString());

	}
}
