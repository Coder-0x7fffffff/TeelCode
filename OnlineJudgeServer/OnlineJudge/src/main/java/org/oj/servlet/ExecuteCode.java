package org.oj.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.util.ConcurrencyUtil;
import org.oj.util.FileUtil;
import org.oj.util.JNAUtil;
import org.oj.util.WebUtil;

import com.alibaba.fastjson.JSON;

/**
 * Servlet implementation class ExecuteCode
 */
@WebServlet("/ExecuteCode")
public class ExecuteCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExecuteCode() {
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
		Map<String, String> parameterMap = WebUtil.parseRequest(request);
		String token = WebUtil.getToken(request);
		if (null == token) {
			token = parameterMap.get("token");
		}
		if (Global.verifyToken(token)) {
			String code = parameterMap.get("code");
			String dirPath = "rid" + ConcurrencyUtil.nextId();
			String input = parameterMap.get("input");
			int language = Integer.parseInt(parameterMap.get("language"));
			/* If java, add package header */
			if (2 == language) {
				code = "package usr.local.oj.ojrecord." + dirPath + ";\n" + code;
			}
			dirPath = "/usr/local/oj/ojrecord/" + dirPath;
			FileUtil.mkdirs(dirPath);
			JNAUtil.execute(code, dirPath, input, language);
			String resJson = FileUtil.readFromFile(dirPath + "/Main.res");
			String outResult = FileUtil.readFromFile(dirPath + "/Main.out");
			Map<String, Object> resJsonMap = new HashMap<String, Object>();
			resJsonMap.put("result", resJson);
			resJsonMap.put("output", outResult);
			response.setContentType("text/json; charset=utf-8");
			response.getWriter().write(JSON.toJSONString(resJsonMap));
		} else {
			/* */
		}
	}

}
