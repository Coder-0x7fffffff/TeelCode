package org.oj.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IClassificationDao;
import org.oj.dao.IProblemClassificationDao;
import org.oj.dao.IProblemDao;
import org.oj.dao.impl.ClassificationDaoImpl;
import org.oj.dao.impl.ProblemClassificationDaoImpl;
import org.oj.dao.impl.ProblemDaoImpl;
import org.oj.entity.Classification;
import org.oj.entity.Problem;
import org.oj.model.ProblemWithClassification;
import org.oj.service.IProblemService;

public class ProblemServiceImpl implements IProblemService {

	IProblemDao problemDao = new ProblemDaoImpl();
	IClassificationDao classificationDao = new ClassificationDaoImpl();
	IProblemClassificationDao problemClassificationDao = new ProblemClassificationDaoImpl();
	
	@Override
	public List<ProblemWithClassification> all(int page, int pageSize) throws SQLException {
		List<ProblemWithClassification> problemWithClassificationList = new ArrayList<ProblemWithClassification>(); 
		List<Problem> problemList = problemDao.findProblems(page, pageSize);
		for (Problem problem : problemList) {
			problemWithClassificationList.add(getProblem(problem.getPid()));
		}
		return problemWithClassificationList;
	}

	@Override
	public ProblemWithClassification getProblem(int id) throws SQLException {
		Problem problem = problemDao.findProblemById(id);
		List<Integer> cidList = problemClassificationDao.findCidsByPid(problem.getPid());
		List<Classification> classificationList = new ArrayList<Classification>();
		for (int cid : cidList) {
			classificationList.add(classificationDao.findClassificationById(cid));
		}
		return new ProblemWithClassification(problem, classificationList);
	}

	@Override
	public boolean addProblem(int id, String name, int difficulty, int pass, int submit, String dscp, String inputs,
			String outputs, List<Classification> classificationList) throws SQLException {
		if (problemDao.insertProblem(id, name, difficulty, pass, submit, dscp, inputs, outputs)) {
			for (Classification classification : classificationList) {
				int cid = classification.getCid();
				if (!problemClassificationDao.insertProblemClassification(id, cid)) {
					return false;
				}
			}
			return true;
		}
		return false;
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
