package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IRecordDao;
import org.oj.entity.Record;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class RecordDaoImpl implements IRecordDao {
	
	private static class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<Record> recordList = new ArrayList<Record>();
			while (resultSet.next()) {
				int pid = resultSet.getInt(1);
				String uid = resultSet.getString(2);
				int pstate = resultSet.getInt(3);
				java.sql.Date time = resultSet.getDate(4);
				int timeUsage = resultSet.getInt(5);
				int memUsage = resultSet.getInt(6);
				String code = resultSet.getString(7);
				int codeType = resultSet.getInt(8);
				String resultInfo = resultSet.getString(9);
				Record record = new Record(pid, uid, pstate, time, timeUsage, memUsage, code, codeType, resultInfo);
				recordList.add(record);
			}
			return recordList;
		}
	}

	private static final ResultHandler SELECT_RESULT_HANDLER = new SelectResultHandler();
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Record> findRecord(int pid) throws SQLException {
		String sql = "SELECT * FROM WHERE p_id=?";
		Object[] params = { pid };
		return (List<Record>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Record> findRecord(int pid, String uid) throws SQLException {
		String sql = "SELECT * FROM WHERE p_id=? AND u_id=?";
		Object[] params = { pid, uid };
		return (List<Record>) DBUtil.query(sql, params, SELECT_RESULT_HANDLER);
	}
	
	@Override
	public boolean insertRecord(int pid, String uid, int pstate, java.sql.Date time,
			int timeUsage, int memUsage, String code, int codeType, String resultInfo) throws SQLException {
		String sql = "INSERT INTO Record VALUES(?,?,?,?,?,?,?,?,?)";
		Object[] params = { pid, uid, pstate, time, timeUsage, memUsage, code, codeType, resultInfo };
		return 1 == DBUtil.update(sql, params);
	}
	
}
