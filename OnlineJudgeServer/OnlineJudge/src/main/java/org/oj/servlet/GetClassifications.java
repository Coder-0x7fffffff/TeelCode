package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetClassifications
 */
@WebServlet("/GetClassifications")
public class GetClassifications extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetClassifications() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IProblemService problemService = new ProblemServiceImpl();
		try {
			List<Classification> classificationList = problemService.getClassifications();
			response.setContentType("text/json; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
	        jsonMap.put("result", classificationList);
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
