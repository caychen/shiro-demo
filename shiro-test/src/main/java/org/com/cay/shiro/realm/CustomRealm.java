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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Cay on 2018/6/7.
 */
public class CustomRealm extends AuthorizingRealm {

	Map<String, String> userMap = new HashMap<String, String>();

	{
		userMap.put("caychen", "217ca76ac3d94aa9d54baabafe769ddc");
		userMap.put("admin", "111111");
		userMap.put("user", "112233");
	}

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
		Set<String> pers = new HashSet<>();
		if(rolename.equals("admin")){
			pers.add("admin:delete");
			pers.add("admin:create");
			pers.add("admin:update");
		}else if(rolename.equals("user")){
			pers.add("user:view");
		}
		return pers;
	}

	private Set<String> getRolesByUsername(String username) {
		Set<String> roles = new HashSet<>();
		if(username.equals("caychen")){
			roles.add("admin");
			roles.add("user");
		}else if(username.equals("admin")){
			roles.add("admin");
		}else{
			roles.add("user");
		}
		return roles;
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
		return userMap.get(username);
	}

	public static void main(String[] args) {
		Md5Hash md5Hash = new Md5Hash("123456", "caychen", 5);
		System.out.println(md5Hash.toString());

	}
}
