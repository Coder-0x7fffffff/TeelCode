package org.oj.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.oj.dao.IClassificationDao;
import org.oj.dao.impl.ClassificationDaoImpl;
import org.oj.entity.Classification;
import org.oj.service.IClassificationService;

public class ClassificationServiceImpl implements IClassificationService {

	IClassificationDao classificationDao = new ClassificationDaoImpl();
	
	@Override
	public boolean alterClassificationName(int id, String name) throws SQLException {
		return classificationDao.updateClassification(id, name);
	}
	
	@Override
	public boolean deleteClassification(int id) throws SQLException {
		return classificationDao.deleteClassification(id);
	}

	@Override
	public boolean addClassification(int id, String name) throws SQLException {
		return classificationDao.insertClassification(id, name);
	}
	
	@Override
	public Classification getClassification(String name) throws SQLException {
		return classificationDao.findClassificationByName(name);
	}

	@Override
	public List<Classification> getClassifications() throws SQLException {
		return classificationDao.findClassifications();
	}
	
}
