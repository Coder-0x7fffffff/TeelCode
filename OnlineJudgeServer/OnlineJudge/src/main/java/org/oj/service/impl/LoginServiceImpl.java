package org.oj.service.impl;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import org.oj.dao.ILoginDao;
import org.oj.dao.impl.LoginDaoImpl;
import org.oj.entity.Login;
import org.oj.service.ILoginService;
import org.oj.util.EncryptionUtil;

public class LoginServiceImpl implements ILoginService {

	ILoginDao loginDao = new LoginDaoImpl();

	@Override
	public boolean login(String id, String pwd) throws SQLException, NoSuchAlgorithmException {
		Login login = loginDao.findLoginById(id);
		return null != login && login.getUpwd().equals(EncryptionUtil.md5(pwd).substring(7, 23));
	}
	
}
