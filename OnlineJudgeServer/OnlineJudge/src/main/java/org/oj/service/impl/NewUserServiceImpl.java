package org.oj.service.impl;

import java.sql.SQLException;

import org.oj.dao.IOJUserDao;
import org.oj.dao.impl.OJUserDaoImpl;
import org.oj.entity.OJUser;
import org.oj.service.INewUserService;

public class NewUserServiceImpl implements INewUserService {

	IOJUserDao ojuserDao = new OJUserDaoImpl();
	
	@Override
	public boolean newUser(String id, String name, int sex, String dscp) throws SQLException {
		OJUser ojuser = ojuserDao.findOJUserById(id);
		if (null == ojuser) {
			if (1 == ojuserDao.insertOJUser(id, name)) {
				if (sex >= 0) {
					ojuserDao.updateOJUserSex(id, sex);
				}
				if (null != dscp) {
					ojuserDao.updateOJUserDscp(id, dscp);
				}
				return true;
			}
		}
		return false;
	}
	
}
