package org.oj.dao.impl;

import java.sql.SQLException;

import org.oj.dao.IRecordDao;
import org.oj.util.DBUtil;

public class RecordDaoImpl implements IRecordDao {

	public boolean insertRecord(int pid, String uid, int pstate, java.sql.Date time,
			int timeUsage, int memUsage, String code, int codeType, String resultInfo) throws SQLException {
		String sql = "INSERT INTO Record VALUES(?,?,?,?,?,?,?,?,?)";
		Object[] params = { pid, uid, pstate, time, timeUsage, memUsage, code, codeType, resultInfo };
		return 1 == DBUtil.update(sql, params);
	}
	
}
