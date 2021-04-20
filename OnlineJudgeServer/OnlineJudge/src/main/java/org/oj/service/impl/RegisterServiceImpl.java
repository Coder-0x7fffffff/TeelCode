package org.oj.service.impl;

import java.sql.SQLException;

import org.oj.dao.ILoginDao;
import org.oj.dao.impl.LoginDaoImpl;
import org.oj.entity.Login;
import org.oj.service.IRegisterService;
import org.oj.util.EncryptionUtil;

public class RegisterServiceImpl implements IRegisterService {

	ILoginDao loginDao = new LoginDaoImpl();
	
	@Override
	public boolean register(String id, String pwd, String question, String answer) throws SQLException {
		Login login = loginDao.findLoginById(id);
		if (null == login) {
			return 1 == loginDao.insertLogin(id, EncryptionUtil.md5(pwd), question, answer);
		}
		return false;
	}
	
	
}