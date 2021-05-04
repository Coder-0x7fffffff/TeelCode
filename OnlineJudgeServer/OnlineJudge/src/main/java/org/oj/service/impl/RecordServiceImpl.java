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
	public List<Record> getRecord(int pid) throws SQLException {
		return recordDao.findRecord(pid);
	}

	@Override
	public List<Record> getRecord(int pid, String uid) throws SQLException {
		return recordDao.findRecord(pid, uid);
	}

}
