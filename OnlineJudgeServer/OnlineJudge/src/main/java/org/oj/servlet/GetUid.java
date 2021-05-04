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
import org.oj.common.Global.Token;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetUid
 */
@WebServlet("/GetUid")
public class GetUid extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUid() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = WebUtil.getToken(request);
		PrintWriter out = response.getWriter();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Token t = Global.getToken(token);
		if (null != t) {
			jsonMap.put("uid", t.uid);
		} else {
			jsonMap.put("uid", -1);
		}
        String json = JSON.toJSONString(jsonMap);
        out.print(json);
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
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("uid", Global.getToken(token).uid);
        String json = JSON.toJSONString(jsonMap);
        out.print(json);
	}

}
