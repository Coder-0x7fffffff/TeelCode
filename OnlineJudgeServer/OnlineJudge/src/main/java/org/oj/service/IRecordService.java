package org.oj.service;

import java.sql.SQLException;
import java.util.List;
import org.oj.entity.Record;

public interface IRecordService {

	public List<Record> getRecord(int pid, int page, int pageSize) throws SQLException;
	
	public List<Record> getRecord(int pid, String uid, int page, int pageSize) throws SQLException;
	
	public int getRecordCount(int pid) throws SQLException;
	
	public int getRecordCount(int pid, String uid) throws SQLException;
	
}
