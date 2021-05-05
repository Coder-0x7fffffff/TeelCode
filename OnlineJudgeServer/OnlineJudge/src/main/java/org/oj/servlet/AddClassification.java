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
import org.oj.service.IClassificationService;
import org.oj.service.impl.ClassificationServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class AddClassification
 */
@WebServlet("/AddClassification")
public class AddClassification extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddClassification() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = WebUtil.decode(request.getParameter("name"));
		IClassificationService classificationService = new ClassificationServiceImpl();
		try {
			boolean result = classificationService.addClassification(id, name);
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
		// doGet(request, response);
		try {
			request.setCharacterEncoding("UTF-8");
			Map<String, String> parameterMap = WebUtil.parseRequest(request);
			String token = WebUtil.getToken(request);
			if (null == token) {
				token = parameterMap.get("token");
			}
			if (Global.verifyToken(token)) {
				int id = Integer.parseInt(parameterMap.get("id"));
				String name = parameterMap.get("name");
				IClassificationService classificationService = new ClassificationServiceImpl();
				boolean result = classificationService.addClassification(id, name);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("result", result);
		        String json = JSON.toJSONString(jsonMap);
		        out.print(json);
			} else {
				/* */
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
