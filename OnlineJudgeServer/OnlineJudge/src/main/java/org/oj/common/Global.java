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
	
	/* <sign, token> */
	private static ConcurrentMap<String, Token> tokenMap = new ConcurrentHashMap<String, Token>();
	
	/* <uid, sign> */
	private static ConcurrentMap<String, String> userSignMap = new ConcurrentHashMap<String, String>();
	
	/* Set timer to refresh tokens */
	static {
		/* init logger */
		logger = Logger.getGlobal();
		try {
			FileHandler fileHandler = new FileHandler("/usr/local/oj/oj.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* init timer */
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				long timeStamp = System.currentTimeMillis();
				for (String uid : userSignMap.keySet()) {
					String sign = userSignMap.get(uid);
					Token token = tokenMap.get(sign);
					if (timeStamp >= token.expireTimeStamp) {
						userSignMap.remove(uid);
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
		if (null == uid) {
			return null;
		}
		if (userSignMap.containsKey(uid)) {
			return tokenMap.get(userSignMap.get(uid));
		}
		long timeStamp = System.currentTimeMillis();
		String sign = generateToken(uid + String.valueOf(timeStamp));
		Token token = new Token(uid, sign, timeStamp);
		userSignMap.put(uid, sign);
		tokenMap.put(sign, token);
		return token;
	}
	
	public static synchronized String getSign(String uid) {
		if (userSignMap.containsKey(uid)) {
			return userSignMap.get(uid);
		}
		return null;
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
