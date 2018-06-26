package org.com.cay.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Cay on 2018/6/8.
 */
public class RolesOrFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = getSubject(request, response);
		String[] roles = (String[]) mappedValue;
		if(roles == null || roles.length == 0){
			return true;
		}

		//有一个role即可通过
		for(String role : roles){
			if(subject.hasRole(role)){
				return true;
			}
		}
		return false;
	}
}
