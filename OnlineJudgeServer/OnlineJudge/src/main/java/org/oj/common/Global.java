package org.oj.common;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.oj.util.EncryptionUtil;

public class Global {

	public static Logger logger;
	
	/* token 过期时间 7 days */
	private static final long DELAY = 7 * 24 * 60 * 60 * 1000;
	
	public static class Token {
		public String uid;
		public String token;
		public long expireTimeStamp;
		
		public Token(String uid, String token, long timeStamp) {
			this.uid = uid;
			this.token = token;
			this.expireTimeStamp = timeStamp + DELAY;
		}
		
		public boolean equals(Token token) {
			return uid == token.uid;
		}
	}
	
	private static ConcurrentMap<String, Token> tokenMap = new ConcurrentHashMap<String, Token>();
	
	/* Set timer to refresh tokens */
	static {
		logger = Logger.getGlobal();
		try {
			FileHandler fileHandler = new FileHandler("oj.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				long timeStamp = System.currentTimeMillis();
				for (String sign : tokenMap.keySet()) {
					Token token = tokenMap.get(sign);
					if (timeStamp >= token.expireTimeStamp) {
						tokenMap.remove(sign);
					}
				}
			}
		}, 1000);;
	}
	
	private static String generateToken(String uid) throws NoSuchAlgorithmException {
		return EncryptionUtil.sha1(uid);
	}
	
	/**
	 *  Get token by uid, if no token for uid, then create one and return 
	 */
	public static synchronized Token getOrCreateToken(String uid) throws NoSuchAlgorithmException {
		for (String key : tokenMap.keySet()) {
			if (uid.equals(tokenMap.get(key).uid)) {
				return tokenMap.get(key);
			}
		}
		long timeStamp = System.currentTimeMillis();
		String sign = generateToken(uid + String.valueOf(timeStamp));
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
		if (null != token && !token.isEmpty() && tokenMap.containsKey(token)) {
			return token.equals(tokenMap.get(token).token);
		}
		return false;
	}
	
}
