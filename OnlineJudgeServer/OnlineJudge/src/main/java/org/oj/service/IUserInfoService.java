package org.oj.service;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.oj.model.UserModel;

public interface IUserInfoService {

	public UserModel getUserInfo(String uid) throws SQLException, UnsupportedEncodingException;
	
}
