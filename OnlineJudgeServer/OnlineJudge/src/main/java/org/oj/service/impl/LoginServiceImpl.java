package org.oj.service.impl;

import java.sql.SQLException;

import org.oj.dao.ILoginDao;
import org.oj.dao.impl.LoginDaoImpl;
import org.oj.entity.Login;
import org.oj.service.ILoginService;
import org.oj.util.EncryptionUtil;

public class LoginServiceImpl implements ILoginService {

	ILoginDao loginDao = new LoginDaoImpl();

	@Override
	public boolean login(String id, String pwd) throws SQLException {
		Login login = loginDao.findLoginById(id);
		if (null != login && login.getUpwd().equals(EncryptionUtil.md5(pwd))) {
			return true;
		}
		return false;
	}
	
}
