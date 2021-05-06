package org.oj.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oj.common.Global;
import org.oj.service.ISubmitService;
import org.oj.service.impl.SubmitServiceImpl;
import org.oj.util.ConcurrencyUtil;
import org.oj.util.DateUtil;
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = WebUtil.getToken(request);
		if (Global.verifyToken(token)) {
			try {
				/* problem id */
				int pid = 2;
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
				java.sql.Timestamp time = DateUtil.toSQLDate(new java.util.Date());
				String dirPath = "rid" + ConcurrencyUtil.nextId();
				Global.logger.info("uid=" + Global.getToken(token).uid + " commit, Directory Path=" + dirPath);
				String samplePath = "ojsample/" + pid;
				/* If java, add package header */
				if (2 == language) {
					code = "package ojrecord." + dirPath + ";\n" + code;
				}
				dirPath = "ojrecord/" + dirPath;
				FileUtil.mkdirs(dirPath);
				JNAUtil.judge(code, dirPath, samplePath, language);
				String resJson = FileUtil.readFromFile(dirPath + "/Main.res");
				JSONObject jsonObject = JSON.parseObject(resJson);
				int pstate = 1;
				int totalTimeUsage = 0;
				int maxMemUsage = 0;
				String resultInfo = "";
				int count = (int) jsonObject.getInteger("count");
				for (int i = 1; i <= count; ++i) {
					JSONObject cur = jsonObject.getJSONObject(String.valueOf(i));
					int resultCode = cur.getInteger("ResultCode");
					/* not AC */
					if (3 != resultCode) {
						pstate = 0;
						totalTimeUsage = maxMemUsage = -1;
						resultInfo = cur.getString("ResultInfo");
					} else {
						int timeUsage = cur.getInteger("TimeUsed");
						int memUsage = cur.getInteger("MemoryUsed");	
						totalTimeUsage += timeUsage;
						maxMemUsage = Math.max(maxMemUsage, memUsage);
					}
				}
				ISubmitService submitService = new SubmitServiceImpl();
				submitService.submit(pid, Global.getToken(token).uid, pstate, time, totalTimeUsage, maxMemUsage, code, language, resultInfo);
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(resJson);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
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
				int pid = Integer.parseInt(parameterMap.get("id"));
				String code = parameterMap.get("code");
				String dirPath = "rid" + ConcurrencyUtil.nextId();
				
				String samplePath = "/usr/local/oj/ojsample/" + pid;
				Global.logger.info("uid=" + Global.getToken(token).uid + 
						" commit, Directory Path=" + dirPath + 
						", Sample Path=" + samplePath + 
						", code=" + code);
				
				int language = Integer.parseInt(parameterMap.get("language"));
				java.sql.Timestamp time = DateUtil.string2SQLDate(parameterMap.get("time"));
				/* If java, add package header */
				if (2 == language) {
					code = "package usr.local.oj.ojrecord." + dirPath + ";\n" + code;
				}
				dirPath = "/usr/local/oj/ojrecord/" + dirPath;
				FileUtil.mkdirs(dirPath);
				JNAUtil.judge(code, dirPath, samplePath, language);
				String resJson = FileUtil.readFromFile(dirPath + "/Main.res");
				JSONObject jsonObject = JSON.parseObject(resJson);
				int pstate = 1;
				int totalTimeUsage = 0;
				int maxMemUsage = 0;
				String resultInfo = "";
				int count = (int) jsonObject.getInteger("count");
				for (int i = 1; i <= count; ++i) {
					JSONObject cur = jsonObject.getJSONObject(String.valueOf(i));
					int resultCode = cur.getInteger("ResultCode");
					/* not AC */
					if (3 != resultCode) {
						pstate = 0;
						totalTimeUsage = maxMemUsage = -1;
						resultInfo = cur.getString("ResultInfo");
					} else {
						int timeUsage = cur.getInteger("TimeUsed");
						int memUsage = cur.getInteger("MemoryUsed");	
						totalTimeUsage += timeUsage;
						maxMemUsage = Math.max(maxMemUsage, memUsage);
					}
				}
				ISubmitService submitService = new SubmitServiceImpl();
				submitService.submit(pid, Global.getToken(token).uid, pstate, time, totalTimeUsage, maxMemUsage, code, language, resultInfo);
				response.setContentType("text/json; charset=utf-8");
				response.getWriter().write(resJson);
			} catch (ParseException e) {
				Global.logger.info("time error :" + parameterMap.get("time"));
			} catch (SQLException e) {
				Global.logger.info("Exception :" + e.getMessage());
			}
		} else {
			/* */
		}
	}

}
