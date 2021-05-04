package org.oj.service.impl;

import java.sql.SQLException;

import org.oj.dao.IClassificationDao;
import org.oj.dao.impl.ClassificationDaoImpl;
import org.oj.service.IClassificationService;

public class ClassificationServiceImpl implements IClassificationService {

	IClassificationDao classificationDao = new ClassificationDaoImpl();
	
	@Override
	public boolean alterClassificationName(int id, String name) throws SQLException {
		return classificationDao.updateClassification(id, name);
	}

}
