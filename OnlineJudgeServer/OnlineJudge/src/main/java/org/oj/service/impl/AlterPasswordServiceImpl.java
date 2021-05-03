package org.oj.service.impl;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.oj.dao.ILoginDao;
import org.oj.dao.impl.LoginDaoImpl;
import org.oj.entity.Login;
import org.oj.service.IAlterPasswordService;
import org.oj.util.EncryptionUtil;

public class AlterPasswordServiceImpl implements IAlterPasswordService {
	
	ILoginDao loginDao = new LoginDaoImpl();
	
	public boolean verify(String id, String answer) throws SQLException {
		Login login = loginDao.findLoginById(id);
		return null != login && 
				login.getUanswer().equals(answer);
	}

	public boolean alter(String id, String pwd) throws SQLException, NoSuchAlgorithmException {
		Login login = loginDao.findLoginById(id);
		return null != login && 
				(1 == loginDao.updateLoginPwd(id, EncryptionUtil.md5(pwd)));
	}
	
}
