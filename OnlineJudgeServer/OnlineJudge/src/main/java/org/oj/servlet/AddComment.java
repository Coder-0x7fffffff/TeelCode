package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.service.ICommentsService;
import org.oj.service.impl.CommentsServiceImpl;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class AddComment
 */
@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComment() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		// problem_id;
		// username;
		// time;
		// detail;
		// parent_comment
		// reply_username
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
				String uid = parameterMap.get("username");
				String time = parameterMap.get("time");
				String detail = parameterMap.get("detail");
				int cfaid = Integer.parseInt(parameterMap.get("parent_comment_id"));
				String ruid = parameterMap.get("reply_username");
				ICommentsService commentService = new CommentsServiceImpl();
				int cid = commentService.addComment(uid, pid, cfaid, ruid, detail, time);
				response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        Map<String, Object> jsonMap = new HashMap<String, Object>();
		        jsonMap.put("err", null);
		        jsonMap.put("cid", cid);
		        String json = JSON.toJSONString(jsonMap);
		        out.print(json);
			} catch (SQLException e) {
				Global.logger.info("Exception :" + e.getMessage());
			} catch (ParseException e) {
				Global.logger.info("time error :" + e.getMessage());
			}
			
		} else {
			/* */
		}
	}

}
