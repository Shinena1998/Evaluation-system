package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.StudentClass;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class student
 */
@WebServlet("/student")
public class student extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public student() {
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
		String stu = request.getParameter("username");
		String xn = request.getParameter("xuenian");
		String xq = request.getParameter("xueqi");
		System.out.println(stu + " " + xn + " " + xq);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "select * from xj where XBianHao=? and XueNian = ? and XueQi = ?";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, stu);
			ps.setString(2, xn);
			ps.setString(3, xq);
			ResultSet rs = ps.executeQuery();
			System.out.println(stu + " " + xn + " " + xq);
			JSONArray jsonArray = new JSONArray();
			while (rs.next()) {
				System.out.println(rs.getString("XBianHao"));
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("XBianHao", rs.getString("XBianHao"));
				jsonObject.put("XXingMing", rs.getString("XXingMing"));
				jsonObject.put("JBianHao", rs.getString("JBianHao"));
				jsonObject.put("JXingMing", rs.getString("JXingMing"));
				jsonObject.put("KeCheng", rs.getString("KeCheng"));
				jsonObject.put("PingJiao", rs.getString("PingJiao"));
				jsonArray.add(jsonObject);
			}
			response.getWriter().println(jsonArray);
		}catch (Exception e) {
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
