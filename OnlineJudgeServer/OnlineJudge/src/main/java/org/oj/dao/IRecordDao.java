package org.oj.dao;

import java.sql.SQLException;
import java.util.List;
import org.oj.entity.Record;

public interface IRecordDao {
	
	public List<Record> findRecord(int pid) throws SQLException;
	
	public List<Record> findRecord(int pid, String uid) throws SQLException;

	public boolean insertRecord(int pid, String uid, int pstate, java.sql.Timestamp time,
			int timeUsage, int memUsage, String code, int codeType, String resultInfo) throws SQLException;
	
}
