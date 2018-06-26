package org.com.cay.shiro.dao.impl;

import org.com.cay.shiro.dao.IUserDao;
import org.com.cay.shiro.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Cay on 2018/6/7.
 */
@Repository
public class UserDaoImpl implements IUserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public User getPasswordByUsername(String username) {
		String sql = "select username, password from user where username = ?";

		User u = jdbcTemplate.queryForObject(sql, new String[]{username}, (rs, rowNum) -> {
			User user = new User();
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			return user;
		});


		return u;
	}

	@Override
	public List<String> getRolesByUsername(String username) {
		String sql = "select rolename from user_role where username = ?";
		return jdbcTemplate.queryForList(sql, new String[]{username}, String.class);
	}

	@Override
	public List<String> getPermissionsByUsername(String rolename) {
		String sql = "select permissionname from role_permission where rolename = ?";
		return jdbcTemplate.queryForList(sql, new String[]{rolename}, String.class);
	}
}
