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
import org.oj.service.IUpdateUserService;
import org.oj.service.impl.UpdateUserServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class UpdateUser
 */
@WebServlet("/UpdateUser")
public class UpdateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateUser() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = WebUtil.getToken(request);
		if (Global.verifyToken(token)) {
			String name = WebUtil.decode(request.getParameter("name"));
			int sex = -1;
			String psex = request.getParameter("sex");
			if (null != psex) {
				sex = Integer.parseInt(psex);
			}
			String dscp = request.getParameter("dscp");
			IUpdateUserService updateUserService = new UpdateUserServiceImpl();
			try {
				boolean result = updateUserService.updateUser(Global.getToken(token).uid, name, sex, dscp);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("result", result);
		        String json = JSON.toJSONString(jsonMap);
		        out.print(json);
			} catch (SQLException e) {
				Global.logger.info("SQL Error :" + e.getMessage());
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
		Map<String, String> parameterMap = WebUtil.parseRequest(request);
		String token = WebUtil.getToken(request);
		if (null == token) {
			token = parameterMap.get("token");
		}
		if (Global.verifyToken(token)) {
			try {
				String name = parameterMap.get("name");
				int sex = -1;
				String psex = parameterMap.get("sex");
				if (null != psex) {
					sex = Integer.parseInt(psex);
				}
				String dscp = parameterMap.get("dscp");
				IUpdateUserService updateUserService = new UpdateUserServiceImpl();
				boolean result = updateUserService.updateUser(Global.getToken(token).uid, name, sex, dscp);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
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
