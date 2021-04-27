package org.oj.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IClassificationDao;
import org.oj.entity.Classification;
import org.oj.util.DBUtil;
import org.oj.util.DBUtil.ResultHandler;

public class ClassificationDaoImpl implements IClassificationDao {

	private class SelectResultHandler implements ResultHandler {
		@Override
		public Object handle(ResultSet resultSet) throws SQLException {
			List<Classification> classificationList = new ArrayList<Classification>();
			while (resultSet.next()) {
				int cid = resultSet.getInt(1);
				String cname = resultSet.getString(2);
				classificationList.add(new Classification(cid, cname));	
			}
			return classificationList;
		}
	}
	
	@Override
	public Classification findClassificationById(int id) throws SQLException {
		String sql = "SELECT * FROM Classification WHERE c_id=?";
		Object[] params = { id };
		@SuppressWarnings("unchecked")
		List<Classification> classificationList = (List<Classification>) DBUtil.query(sql, params, new SelectResultHandler());
		return classificationList.isEmpty() ? null : classificationList.get(0);
	}

	@Override
	public Classification findClassificationByName(String name) throws SQLException {
		String sql = "SELECT * FROM Classification WHERE c_name=?";
		Object[] params = { name };
		@SuppressWarnings("unchecked")
		List<Classification> classificationList = (List<Classification>) DBUtil.query(sql, params, new SelectResultHandler());
		return classificationList.isEmpty() ? null : classificationList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Classification> findClassifications() throws SQLException {
		String sql = "SELECT * FROM Classification";
		return (List<Classification>) DBUtil.query(sql, null, new SelectResultHandler());
	}
	
	@Override
	public boolean insertClassification(int id, String name) throws SQLException {
		String sql = "INSERT INTO Classification VALUES(?,?)";
		Object[] params = { id, name };
		return 1 == DBUtil.update(sql, params);
	}

	@Override
	public boolean deleteClassification(int id) throws SQLException {
		String sql = "DELETE FROM Classification WHERE c_id=?";
		Object[] params = { id };
		return 1 == DBUtil.update(sql, params);
	}
	
	@Override
	public boolean deleteClassification(String name) throws SQLException {
		String sql = "DELETE FROM Classification WHERE c_name=?";
		Object[] params = { name };
		return 1 == DBUtil.update(sql, params);
	}

	@Override
	public boolean updateClassification(int id, String name) throws SQLException {
		String sql = "UPDATE Classification SET c_name=? WHERE c_id=?";
		Object[] params = { name, id };
		return 1 == DBUtil.update(sql, params);
	}
	
}
