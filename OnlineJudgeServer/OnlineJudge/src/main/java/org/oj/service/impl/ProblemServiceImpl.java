package org.oj.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oj.dao.IClassificationDao;
import org.oj.dao.IProblemClassificationDao;
import org.oj.dao.IProblemDao;
import org.oj.dao.IUserProblemDao;
import org.oj.dao.impl.ClassificationDaoImpl;
import org.oj.dao.impl.ProblemClassificationDaoImpl;
import org.oj.dao.impl.ProblemDaoImpl;
import org.oj.dao.impl.UserProblemDaoImpl;
import org.oj.entity.Classification;
import org.oj.entity.Problem;
import org.oj.model.ProblemWithClassification;
import org.oj.service.IProblemService;

public class ProblemServiceImpl implements IProblemService {

	IProblemDao problemDao = new ProblemDaoImpl();
	IClassificationDao classificationDao = new ClassificationDaoImpl();
	IProblemClassificationDao problemClassificationDao = new ProblemClassificationDaoImpl();
	IUserProblemDao userProblemDao = new UserProblemDaoImpl();
	
	@Override
	public List<ProblemWithClassification> all(
			String uid, int page, int pageSize, int difficulty, int classification, int status) throws SQLException {
		List<ProblemWithClassification> problemWithClassificationList = new ArrayList<ProblemWithClassification>(); 
		List<Problem> problemList = problemDao.findProblems(uid, page, pageSize, difficulty, classification, status);
		for (Problem problem : problemList) {
			problemWithClassificationList.add(getProblemWithClassification(uid, problem.getPid()));
		}
		return problemWithClassificationList;
	}

	@Override
	public ProblemWithClassification getProblemWithClassification(String uid, int pid) throws SQLException {
		Problem problem = problemDao.findProblemById(pid);
		List<Integer> cidList = problemClassificationDao.findCidsByPid(problem.getPid());
		List<Classification> classificationList = new ArrayList<Classification>();
		for (int cid : cidList) {
			classificationList.add(classificationDao.findClassificationById(cid));
		}
		int passed = userProblemDao.findStateByUidAndPid(uid, pid);
		return new ProblemWithClassification(problem, classificationList, passed);
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
	
	public boolean deleteProblem(int id) throws SQLException {
		return problemDao.deleteProblem(id);
	}

	@Override
	public int getProblemCount() throws SQLException {
		return problemDao.getProblemCount();
	}
	
	@Override
	public int getNextProblemId() throws SQLException {
		return problemDao.getNextProblemId();
	}
	
}
