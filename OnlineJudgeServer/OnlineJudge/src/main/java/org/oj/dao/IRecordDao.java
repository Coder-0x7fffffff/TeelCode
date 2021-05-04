package org.oj.dao;

import java.sql.SQLException;

public interface IRecordDao {

	public boolean insertRecord(int pid, String uid, int pstate, java.sql.Date time,
			int timeUsage, int memUsage, String code, int codeType, String resultInfo) throws SQLException;
	
}
