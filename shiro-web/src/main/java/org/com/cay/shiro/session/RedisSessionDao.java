package org.com.cay.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.com.cay.shiro.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Cay on 2018/6/8.
 */
public class RedisSessionDao extends AbstractSessionDAO {

	@Autowired
	private JedisUtil jedisUtil;

	private final String SHIRO_SESSION_PREFIX = "shiro-session:";

	private byte[] getkey(String key) {
		return (SHIRO_SESSION_PREFIX + key).getBytes();
	}

	private void saveSession(Session session) {
		if (session != null && session.getId() != null) {
			byte[] key = getkey(session.getId().toString());
			byte[] value = SerializationUtils.serialize(session);
			jedisUtil.set(key, value);
			jedisUtil.expire(key, 600);
		}
	}

	@Override
	protected Serializable doCreate(Session session) {
		System.out.println("create session...");
		Serializable sessionId = generateSessionId(session);
		//将生成的sessionId和session进行捆绑
		assignSessionId(session, sessionId);
		saveSession(session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		System.out.println("read session...");
		if (sessionId != null) {
			byte[] key = getkey(sessionId.toString());
			byte[] value = jedisUtil.get(key);
			if (value == null) {
				return null;
			}

			return (Session) SerializationUtils.deserialize(value);
		}
		return null;
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		System.out.println("update session...");
		saveSession(session);
	}

	@Override
	public void delete(Session session) {
		System.out.println("delete session...");
		if (session != null && session.getId() != null) {
			byte[] key = getkey(session.getId().toString());
			jedisUtil.del(key);
		}
	}

	@Override
	public Collection<Session> getActiveSessions() {
		System.out.println("get session...");
		Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
		Set<Session> sessions = new HashSet<>();

		if (!CollectionUtils.isEmpty(keys)) {
			keys.stream().forEach(key -> sessions.add((Session) SerializationUtils.deserialize(key)));
		}

		return sessions;
	}
}
