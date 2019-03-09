package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ChaXunJiaoGong
 */
@WebServlet("/ChaXunJiaoGong")
public class ChaXunJiaoGong extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChaXunJiaoGong() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String jiaogonghao = request.getParameter("zhanghao");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			Statement statement = conn.createStatement();
			String sql1 = "select * from JiaoGongBiao where JiaoGongHao="+jiaogonghao;
			System.out.println(sql1);
			ResultSet rs = statement.executeQuery(sql1);
			JSONArray jsonArray= new JSONArray();
			if(rs.next()) {
				for(int i = 1 ; i <= 22 ; i ++) {
					jsonArray.add(rs.getString(i));
				}
				if(rs.getString(23) != null) {
					if(!"".equals(rs.getString(23))) {
						String base64 = rs.getString(23).replaceAll(" ", "+");
						jsonArray.add(base64);
					}else {
						jsonArray.add("");
					}
				}else {
					jsonArray.add("");
				}
				jsonArray.add(rs.getString(24));	
				response.getWriter().print(jsonArray);
			}else {
				response.getWriter().print("不存在此教工");
			}
			rs.close();
			statement.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
