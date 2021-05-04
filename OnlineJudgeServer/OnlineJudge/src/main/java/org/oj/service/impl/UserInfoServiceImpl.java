package org.oj.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.oj.common.Global;
import org.oj.dao.ILoginDao;
import org.oj.dao.IOJUserDao;
import org.oj.dao.impl.LoginDaoImpl;
import org.oj.dao.impl.OJUserDaoImpl;
import org.oj.entity.Login;
import org.oj.entity.OJUser;
import org.oj.model.UserModel;
import org.oj.service.IUserInfoService;
import org.oj.util.BlobUtil;

public class UserInfoServiceImpl implements IUserInfoService {

	ILoginDao loginDao = new LoginDaoImpl();
	IOJUserDao ojuserDao = new OJUserDaoImpl();
	
	@Override
	public UserModel getUserInfo(String uid) throws SQLException, UnsupportedEncodingException {
		Login login = loginDao.findLoginById(uid);
		OJUser ojuser = ojuserDao.findOJUserById(uid);
		if (null != login && null != ojuser) {
			UserModel userModel = new UserModel(
					uid, // uid
					Global.getSign(uid), // token 
					ojuser.getUname(), // name
					ojuser.getUsex(), // sex
					ojuser.getUdscp(), // dscp
					login.getUproblem(), // question
					login.getUanswer(), // answer
					BlobUtil.blob2String(ojuser.getUimg()), // img
					login.getUtype()); // isAdmin
			return userModel;
		}
		return null;
	}

}
