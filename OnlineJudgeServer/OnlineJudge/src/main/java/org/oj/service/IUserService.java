package org.oj.service;

import java.sql.SQLException;
import java.util.List;

import org.oj.model.UserRecordModel;

public interface IUserService {

	public List<UserRecordModel> getRecord(String id) throws SQLException;
	
}
