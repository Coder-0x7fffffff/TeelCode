package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.model.UserModel;
import org.oj.service.IUserInfoService;
import org.oj.service.impl.UserInfoServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetUserInfo
 */
@WebServlet("/GetUserInfo")
public class GetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserInfo() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		try {
			request.setCharacterEncoding("UTF-8");
			Map<String, String> parameterMap = WebUtil.parseRequest(request);
			String uid = parameterMap.get("uid");
			IUserInfoService userInfoService = new UserInfoServiceImpl();
			UserModel userModel = userInfoService.getUserInfo(uid);
			response.setContentType("text/json; charset=utf-8");
			PrintWriter out = response.getWriter();
	        String json = JSON.toJSONString(userModel);
	        out.print(json);
		} catch (SQLException | UnsupportedEncodingException e) {
			Global.logger.info("Exception :" + e.getMessage());
		}
	}

}
