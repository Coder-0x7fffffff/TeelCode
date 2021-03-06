package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetDifficulties
 */
@WebServlet("/GetDifficulties")
public class GetDifficulties extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDifficulties() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        Map<String, String> difficultMap = new HashMap<String, String>();
        difficultMap.put("0", "全部");
        difficultMap.put("1", "简单");
        difficultMap.put("2", "中等");
        difficultMap.put("3", "困难");
        String json = JSON.toJSONString(difficultMap);
        out.print(json);
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
			response.setContentType("text/json; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        Map<String, String> difficultMap = new HashMap<String, String>();
	        difficultMap.put("0", "全部");
	        difficultMap.put("1", "简单");
	        difficultMap.put("2", "中等");
	        difficultMap.put("3", "困难");
	        String json = JSON.toJSONString(difficultMap);
	        out.print(json);
		} else {
			/* */
		}
	}

}
