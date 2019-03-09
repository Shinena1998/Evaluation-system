package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class PingJiaoXinXi
 */
@WebServlet("/PingJiaoXinXi")
public class PingJiaoXinXi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PingJiaoXinXi() {
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
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String riqi = request.getParameter("riqi");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			String sql1 = "SELECT * FROM PeiZhiBiao WHERE DangQianXueNian=? and DangQianXueQi=? and ?>=DATE(KaiShiPingJiaoShiJian) and ?<=Date(JieShuPingJiaoShiJian)";
			System.out.println(sql1);
			ps = conn.prepareStatement(sql1);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, riqi);
			ps.setString(4, riqi);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				response.getWriter().print("到评教时间");
			}else {
				response.getWriter().print("未到评教师间");
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
