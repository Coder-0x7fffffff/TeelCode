package org.oj.service;

import java.sql.SQLException;

public interface ISubmitService {

	public boolean submit(int pid, String uid, int pstate, java.sql.Date time,
			int timeUsage, int memUsage, String code, int codeType, String resultInfo) throws SQLException;
	
}
