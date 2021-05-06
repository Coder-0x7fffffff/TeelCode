package org.oj.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.entity.Classification;
import org.oj.service.IClassificationService;
import org.oj.service.IProblemService;
import org.oj.service.impl.ClassificationServiceImpl;
import org.oj.service.impl.ProblemServiceImpl;
import org.oj.util.FileUtil;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * Servlet implementation class AddProblem
 */
@WebServlet("/AddProblem")
public class AddProblem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddProblem() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = WebUtil.decode(request.getParameter("name"));
		int difficulty = Integer.parseInt(request.getParameter("difficulty"));
		String dscp = WebUtil.decode(request.getParameter("dscp"));
		String inputs = WebUtil.decode(request.getParameter("inputs"));
		String outputs = WebUtil.decode(request.getParameter("outputs"));
		String classificationStr = WebUtil.decode(request.getParameter("classification"));
		String[] classificationNames = classificationStr.split(",");
		IClassificationService classificationService = new ClassificationServiceImpl();
		IProblemService problemService = new ProblemServiceImpl();
		try {
			List<Classification> classificationList = new ArrayList<Classification>();
			for (String classificationName : classificationNames) {
				classificationList.add(classificationService.getClassification(classificationName));
			}
			boolean result = problemService.addProblem(id, name, difficulty, 0, 0, dscp, inputs, outputs, classificationList);
			response.setContentType("text/json; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
	        if (result) {
	        	new File("/usr/local/oj/ojsample/" + id).mkdirs();
	        }
	        jsonMap.put("result", result);
	        String json = JSON.toJSONString(jsonMap);
	        out.print(json);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		request.setCharacterEncoding("UTF-8");
		Map<String, String> parameterMap = WebUtil.parseRequest(request);
		String token = WebUtil.getToken(request);
		if (null == token) {
			token = parameterMap.get("token");
		}
		if (Global.verifyToken(token)) {
			try {
				int pid = Integer.parseInt(parameterMap.get("id"));
				String name = parameterMap.get("name");
				int difficulty = Integer.parseInt(parameterMap.get("difficulty"));
				String dscp = parameterMap.get("dscp");
				String inputs = parameterMap.get("inputs");
				String outputs = parameterMap.get("outputs");
				List<Classification> classificationList = new ArrayList<Classification>();
				String classificationString = parameterMap.get("classification");
				if (null != classificationString) {
					IClassificationService classificationService = new ClassificationServiceImpl();
					String[] classificationNames = classificationString.split(",");
					for (String classificationName : classificationNames) {
						classificationList.add(classificationService.getClassification(classificationName));
					}
				}
				IProblemService problemService = new ProblemServiceImpl();
				boolean result = problemService.addProblem(pid, name, difficulty, 0, 0, dscp, inputs, outputs, classificationList);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        if (result) {
		        	String samplePath = "/usr/local/oj/ojsample/" + pid;
		        	new File(samplePath).mkdirs();
		        	JSONArray inputArray = JSON.parseArray(inputs);
		        	JSONArray outputArray = JSON.parseArray(outputs);
		        	int length = Math.min(inputArray.size(), outputArray.size());
		        	for (int i = 1; i <= length; ++i) {
		        		FileUtil.writeToFile(samplePath + "/" + i + ".in", inputArray.getString(i - 1));
		        		FileUtil.writeToFile(samplePath + "/" + i + ".out", outputArray.getString(i - 1));
		        	}
		        }
		        jsonMap.put("result", result);
		        String json = JSON.toJSONString(jsonMap);
		        out.print(json);
			} catch (SQLException e) {
				Global.logger.info("Exception :" + e.getMessage());
			}	
		} else {
			/* */
		}
	}

}
