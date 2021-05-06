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
import org.oj.model.CommentsModel;
import org.oj.service.ICommentsService;
import org.oj.service.impl.CommentsServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetComments
 */
@WebServlet("/GetComments")
public class GetComments extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetComments() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// problem_id;
		// page;
		// offset
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
				int pid = Integer.parseInt(parameterMap.get("problem_id"));
				int page = Integer.parseInt(parameterMap.get("page"));
				int pageSize = Integer.parseInt(parameterMap.get("offset"));
				ICommentsService commentService = new CommentsServiceImpl();
				List<CommentsModel> commentsModelList = commentService.getComments(pid, page, pageSize);
				response.setContentType("text/json; charset=utf-8");
				PrintWriter out = response.getWriter();
				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("err", null);
		        jsonMap.put("comments", commentsModelList);
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
