package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.service.IProblemService;
import org.oj.service.impl.ProblemServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetProblemCount
 */
@WebServlet("/GetProblemCount")
public class GetProblemCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProblemCount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getWriter().append("Served at: ").append(request.getContextPath());
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
			IProblemService problemService = new ProblemServiceImpl();
			try {
				int count = problemService.getProblemCount();
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("count", count);
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
