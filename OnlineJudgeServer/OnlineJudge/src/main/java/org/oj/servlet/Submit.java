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
import com.alibaba.fastjson.JSONObject;

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
    @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = WebUtil.getToken(request);
		if (Global.verifyToken(token)) {
			/* problem id */
			int id = 1;
			String code = "#include <iostream>\n"
					+ "#include <vector>\n"
					+ "using namespace std;\n"
					+ "int main() {\n"
					+ "    ios::sync_with_stdio(false);\n"
					+ "    int a, b;\n"
					+ "    while (cin >> a >> b){\n"
					+ "        printf(\"%d\\n\", a + b);\n"
					+ "    }\n"
					+ "    return 0;\n"
					+ "}";
			int language = 1;
			String dirPath = "rid" + ConcurrencyUtil.nextId();
			Global.logger.info("uid=" + Global.getToken(token).uid + " commit, Directory Path=" + dirPath);
			String samplePath = "ojsample/" + id;
			/* If java, add package header */
			if (2 == language) {
				code = "package ojrecord." + dirPath + ";\n" + code;
			}
			dirPath = "ojrecord/" + dirPath;
			FileUtil.mkdirs(dirPath);
			JNAUtil.judge(code, dirPath, samplePath, language);
			String resJson = FileUtil.readFromFile(dirPath + "/Main.res");
			JSONObject jsonObject = JSON.parseObject(resJson);
			/* update userproblem and record */
			int count = (int) jsonObject.getInteger("count");
			for (int i = 1; i <= count; ++i) {
				JSONObject cur = jsonObject.getJSONObject(String.valueOf(i));
				int resultCode = cur.getInteger("ResultCode");
				int timeUsage = cur.getInteger("TimeUsed");
				int memUsage = cur.getInteger("MemoryUsed");
				String resultInfo = cur.getString("ResultInfo");
			}
			response.getWriter().write(resJson);
		}
		else {
			/* */
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		request.setCharacterEncoding("UTF-8");
		Map<String, String> paramterMap = WebUtil.parseRequest(request);
		String token = WebUtil.getToken(request);
		if (null == token) {
			token = paramterMap.get("token");
		}
		if (Global.verifyToken(token)) {
			int id = Integer.parseInt(paramterMap.get("id"));
			String code = paramterMap.get("code");
			int language = Integer.parseInt(paramterMap.get("language"));
			String dirPath = "rid" + ConcurrencyUtil.nextId();
			Global.logger.info("uid=" + Global.getToken(token).uid + " commit, Directory Path=" + dirPath);
			String samplePath = "/usr/local/oj/ojsample/" + id;
			/* If java, add package header */
			if (2 == language) {
				code = "package usr.local.oj.ojrecord." + dirPath + ";\n" + code;
			}
			dirPath = "/usr/local/oj/ojrecord/" + dirPath;
			FileUtil.mkdirs(dirPath);
			JNAUtil.judge(code, dirPath, samplePath, language);
			String resJson = FileUtil.readFromFile(dirPath + "/Main.res");
			JSONObject jsonObject = JSON.parseObject(resJson);
			/* update userproblem and record */
			int count = (int) jsonObject.getInteger("count");
			for (int i = 1; i <= count; ++i) {
				JSONObject cur = jsonObject.getJSONObject(String.valueOf(i));
				int resultCode = cur.getInteger("ResultCode");
				int timeUsage = cur.getInteger("TimeUsed");
				int memUsage = cur.getInteger("MemoryUsed");
				String resultInfo = cur.getString("ResultInfo");
			}
			response.getWriter().write(resJson);
		} else {
			/* */
		}
	}

}
