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

import org.oj.common.Global;
import org.oj.model.ProblemWithClassification;
import org.oj.service.IProblemService;
import org.oj.service.impl.ProblemServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class All
 */
@WebServlet("/All")
public class All extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public All() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * {
		 * page:1,
		 * offset:50,
		 * difficulty:0, #0代表查询所有题目，与info取值含义相同
		 * class:0, #0代表查询所有题目，与info取值含义相同
		 * status:0, #0代表未完成，1代表完成，-1代表所有题目！！！
		 * }
		 */
		String token = WebUtil.getToken(request);
		if (Global.verifyToken(token)) {
			String uid = Global.getToken(token).uid;
			int page = Integer.parseInt(request.getParameter("page"));
			int pageSize = Integer.parseInt(request.getParameter("offset"));
			int difficulty = Integer.parseInt(request.getParameter("difficulty"));
			int classification = Integer.parseInt(request.getParameter("class"));
			int status = Integer.parseInt(request.getParameter("status"));
			IProblemService problemService = new ProblemServiceImpl();
			try {
				List<ProblemWithClassification> problemWithClassificationList = problemService.all(
						uid, page, pageSize, difficulty, classification, status);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("result", problemWithClassificationList);
		        String json = JSON.toJSONString(jsonMap);
		        out.print(json);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			/* */
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		request.setCharacterEncoding("UTF-8");
		Map<String, String> paramterMap = WebUtil.parseRequest(request);
		String token = WebUtil.getToken(request);
		if (null == token) {
			token = paramterMap.get("token");
		}
		if (Global.verifyToken(token)) {
			String uid = Global.getToken(token).uid;
			int page = Integer.parseInt(paramterMap.get("page"));
			int pageSize = Integer.parseInt(paramterMap.get("offset"));
			int difficulty = Integer.parseInt(paramterMap.get("difficulty"));
			int classification = Integer.parseInt(paramterMap.get("class"));
			int status = Integer.parseInt(paramterMap.get("status"));
			IProblemService problemService = new ProblemServiceImpl();
			try {
				List<ProblemWithClassification> problemWithClassificationList = problemService.all(
						uid, page, pageSize, difficulty, classification, status);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("result", problemWithClassificationList);
		        String json = JSON.toJSONString(jsonMap);
		        out.print(json);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			/* */
		}
	}

}
