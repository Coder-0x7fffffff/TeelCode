package org.oj.service.impl;

import java.sql.SQLException;

import org.oj.dao.IOJUserDao;
import org.oj.dao.impl.OJUserDaoImpl;
import org.oj.entity.OJUser;
import org.oj.service.IUpdateUserService;

public class UpdateUserServiceImpl implements IUpdateUserService {

	IOJUserDao ojuserDao = new OJUserDaoImpl();
	
	@Override
	public boolean updateUser(String id, String name, int sex, String dscp) throws SQLException {
		OJUser ojuser = ojuserDao.findOJUserById(id);
		if (null != ojuser) {
			if (null != name) {
				ojuserDao.updateOJUserName(id, name);
			}
			if (sex > 0) {
				ojuserDao.updateOJUserSex(id, sex);
			}
			if (null != dscp) {
				ojuserDao.updateOJUserDscp(id, dscp);
			}
			return true;
		}
 		return false;
	}

}
