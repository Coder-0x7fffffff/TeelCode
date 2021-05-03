package org.oj.servlet;

import java.io.IOException;
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
 * Servlet implementation class Submit
 */
@WebServlet("/Submit")
public class Submit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Submit() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = WebUtil.getToken(request);
		if (Global.verifyToken(token)) {
			/* problem id */
			int id = Integer.parseInt(WebUtil.decode(request.getParameter("id")));
			String code = WebUtil.decode(request.getParameter("code"));
			int language = Integer.parseInt(WebUtil.decode(request.getParameter("language")));
			String dirPath = "rid" + ConcurrencyUtil.nextId();
			String samplePath = "ojsample/" + id;
			/* If java, add package header */
			if (2 == language) {
				code = "package ojrecord." + dirPath + ";\n" + code;
			}
			dirPath = "ojrecord/" + dirPath;
			FileUtil.mkdirs(dirPath);
			JNAUtil.judge(code, dirPath, samplePath, language);
			String resJson = FileUtil.readFromFile(dirPath + "/Main.res");
			@SuppressWarnings("unchecked")
			Map<String, Object> resJsonMap = (Map<String, Object>) JSON.parse(resJson);
			/* update userproblem and record */
			response.getWriter().write(resJson);
		}
		else {
			/* */
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
