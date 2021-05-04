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

import org.oj.common.Global;
import org.oj.entity.UserProblem;
import org.oj.service.IUserService;
import org.oj.service.impl.UserServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetUserRecord
 */
@WebServlet("/GetUserRecord")
public class GetUserRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserRecord() {
        super();
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
		Map<String, String> paramterMap = WebUtil.parseRequest(request);
		String token = WebUtil.getToken(request);
		if (null == token) {
			token = paramterMap.get("token");
		}
		if (Global.verifyToken(token)) {
	        try {
		        IUserService userService = new UserServiceImpl();
				List<UserProblem> userProblemList = userService.getRecord(Global.getToken(token).uid);
				List<Integer> passedList = new ArrayList<Integer>();
		        List<Integer> notPassedList = new ArrayList<Integer>();
				for (UserProblem userProblem : userProblemList) {
					if (1 == userProblem.getPstate()) {
						passedList.add(userProblem.getPid());
					} else {
						notPassedList.add(userProblem.getPid());
					}
				}
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("passed", passedList);
		        jsonMap.put("not_passed", notPassedList);
		        String json = JSON.toJSONString(jsonMap);
		        response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        out.print(json);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			/* */
		}
	}

}
