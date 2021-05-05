package org.oj.service;

import java.sql.SQLException;
import java.util.List;

import org.oj.model.UserRecordModel;

public interface IUserService {

	public int getRecordCount(String id) throws SQLException;
	
	public List<UserRecordModel> getRecord(String id, int page, int pageSize) throws SQLException;
	
}
