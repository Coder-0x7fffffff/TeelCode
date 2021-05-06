package org.oj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.service.IRecordService;
import org.oj.service.impl.RecordServiceImpl;
import org.oj.util.WebUtil;
import org.oj.entity.Record;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class GetProblemRecord
 */
@WebServlet("/GetProblemRecord")
public class GetProblemRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProblemRecord() {
        super();
        // TODO Auto-generated constructor stub
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
	        	int pid = Integer.parseInt(paramterMap.get("pid"));
	        	String uid = paramterMap.get("uid");
	        	int page = Integer.parseInt(paramterMap.get("page"));
	        	int pageSize = Integer.parseInt(paramterMap.get("offset"));
	        	IRecordService recordService = new RecordServiceImpl();
	        	List<Record> recordList = null;
	        	if (null == uid || uid.isEmpty()) {
	        		recordList = recordService.getRecord(pid, page, pageSize);
	        	} else {
	        		recordList = recordService.getRecord(pid, uid, page, pageSize);
	        	}
		        String json = JSON.toJSONString(recordList);
		        response.setContentType("text/json; charset=utf-8");
		        PrintWriter out = response.getWriter();
		        out.print(json);
			} catch (SQLException e) {
				Global.logger.info("Exception :" + e.getMessage());
			}
		} else {
			/* */
		}
	}

}
