package org.oj.common;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.oj.util.EncryptionUtil;

public class Global {

	/* token 过期时间 7 days */
	private static final long DELAY = 7 * 24 * 60 * 60 * 1000;
	
	public static class Token {
		public String uid;
		public String token;
		public long timeStamp;
		
		public Token(String uid, String token, long timeStamp) {
			this.uid = uid;
			this.token = token;
			this.timeStamp = timeStamp;
		}
	}
	
	private static ConcurrentMap<String, Token> tokenMap = new ConcurrentHashMap<String, Token>();
	
	/* Set timer to refresh tokens */
	static {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				long timeStamp = System.currentTimeMillis();
				for (String sign : tokenMap.keySet()) {
					Token token = tokenMap.get(sign);
					if (timeStamp - token.timeStamp >= DELAY) {
						tokenMap.remove(sign);
					}
				}
			}
		}, 1000);;
	}
	
	private static String generateToken(String uid) {
		return EncryptionUtil.sha256(uid);
	}
	
	/**
	 *  Get token by uid, if no token for uid, then create one and return 
	 */
	public static synchronized Token getOrCreateToken(String uid) {
		long timeStamp = System.currentTimeMillis();
		String sign = generateToken(uid + String.valueOf(timeStamp));
		if (tokenMap.containsKey(sign)) {
			return tokenMap.get(sign);
		}
		Token token = new Token(uid, sign, timeStamp);
		tokenMap.put(sign, token);
		return token;
	}
	
	public static synchronized Token getToken(String sign) {
		if (tokenMap.containsKey(sign)) {
			return tokenMap.get(sign);
		}
		return null;
	}
	
	public static synchronized boolean verifyToken(String token) {
		if (!token.isEmpty() && tokenMap.containsKey(token)) {
			return tokenMap.get(token).token.equals(token);
		}
		return false;
	}
	
}
