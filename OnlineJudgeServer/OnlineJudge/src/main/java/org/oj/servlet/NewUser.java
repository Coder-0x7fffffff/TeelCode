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

import org.oj.service.INewUserService;
import org.oj.service.impl.NewUserServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class NewUser
 */
@WebServlet("/NewUser")
public class NewUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewUser() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = WebUtil.decode(request.getParameter("id"));
		String name = WebUtil.decode(request.getParameter("name"));
		int sex = -1;
		String psex = request.getParameter("sex");
		if (null != psex) {
			sex = Integer.parseInt(psex);
		}
		String dscp = request.getParameter("dscp");
		INewUserService newUserService = new NewUserServiceImpl();
		try {
			boolean result = newUserService.newUser(id, name, sex, dscp);
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
