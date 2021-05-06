package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.service.ILoginService;
import org.oj.service.impl.LoginServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String id = WebUtil.decode(request.getParameter("id"));
			String pwd = WebUtil.decode(request.getParameter("pwd"));
			ILoginService loginService = new LoginServiceImpl();
			boolean result = loginService.login(id, pwd);
			response.setContentType("text/json; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
	        jsonMap.put("result", result);
	        String token = null;
	        if (result) {
	        	token = Global.getOrCreateToken(id).token;
	        }
	        jsonMap.put("token", token);
	        String json = JSON.toJSONString(jsonMap);
	        out.print(json);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
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
			String id = parameterMap.get("id");
			String pwd = parameterMap.get("pwd");
			ILoginService loginService = new LoginServiceImpl();
			boolean result = loginService.login(id, pwd);
			response.setContentType("text/json; charset=utf-8");
	        PrintWriter out = response.getWriter();
	        Map<String, Object> jsonMap = new HashMap<String, Object>();
	        jsonMap.put("result", result);
	        String token = null;
	        if (result) {
	        	token = Global.getOrCreateToken(id).token;
	        }
	        jsonMap.put("token", token);
	        String json = JSON.toJSONString(jsonMap);
	        out.print(json);
		} catch (SQLException | NoSuchAlgorithmException e) {
			Global.logger.info("Exception :" + e.getMessage());
		}
	}

}
