package org.oj.dao;

import java.sql.SQLException;

import org.oj.entity.OJUser;

public interface IOJUserDao {

	public OJUser findOJUserById(String id) throws SQLException;
	
	public int insertOJUser(String id, String name) throws SQLException;
	
	public int deleteOJUser(String id) throws SQLException;
	
	public int updateOJUserName(String id, String name) throws SQLException;
	
	public int updateOJUserSex(String id, int sex) throws SQLException;
	
	public int updateOJUserDscp(String id, String dscp) throws SQLException;
	
}
