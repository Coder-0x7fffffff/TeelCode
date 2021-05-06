package org.oj.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.oj.dao.IRecordDao;
import org.oj.dao.impl.RecordDaoImpl;
import org.oj.entity.Record;
import org.oj.service.IRecordService;

public class RecordServiceImpl implements IRecordService {

	IRecordDao recordDao = new RecordDaoImpl();
	
	@Override
	public List<Record> getRecord(int pid, int page, int pageSize) throws SQLException {
		return recordDao.findRecord(pid, page, pageSize);
	}

	@Override
	public List<Record> getRecord(int pid, String uid, int page, int pageSize) throws SQLException {
		return recordDao.findRecord(pid, uid,  page, pageSize);
	}
	
	@Override
	public int getRecordCount(int pid) throws SQLException {
		return recordDao.getRecordCount(pid);
	}
	
	@Override
	public int getRecordCount(int pid, String uid) throws SQLException {
		return recordDao.getRecordCount(pid, uid);
	}

}
