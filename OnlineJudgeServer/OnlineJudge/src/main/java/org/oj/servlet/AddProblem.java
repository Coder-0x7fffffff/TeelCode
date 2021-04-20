package org.oj.servlet;

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

import org.oj.entity.Classification;
import org.oj.service.IProblemService;
import org.oj.service.impl.ProblemServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

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
		IProblemService problemService = new ProblemServiceImpl();
		try {
			List<Classification> classificationList = new ArrayList<Classification>();
			for (String classificationName : classificationNames) {
				classificationList.add(problemService.getClassification(classificationName));
			}
			boolean result = problemService.addProblem(id, name, difficulty, 0, 0, dscp, inputs, outputs, classificationList);
			response.setContentType("text/json; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
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
		doGet(request, response);
	}

}
