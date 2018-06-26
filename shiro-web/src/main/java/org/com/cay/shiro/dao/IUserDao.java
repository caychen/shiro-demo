package org.com.cay.shiro.dao;

import org.com.cay.shiro.vo.User;

import java.util.List;

/**
 * Created by Cay on 2018/6/7.
 */
public interface IUserDao {

	User getPasswordByUsername(String username);

	List<String> getRolesByUsername(String username);

	List<String> getPermissionsByUsername(String rolename);
}
