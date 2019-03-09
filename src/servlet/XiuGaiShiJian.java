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

import net.sf.json.JSONArray;

/**
 * Servlet implementation class XiuGaiShiJian
 */
@WebServlet("/XiuGaiShiJian")
public class XiuGaiShiJian extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XiuGaiShiJian() {
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
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
				String update = "update PeiZhiBiao set KaiShiPingJiaoShiJian=?,JieShuPingJiaoShiJian=? where 1=1";
				PreparedStatement ps = null;
				ps = conn.prepareStatement(update);
				ps.setString(1, start);
				ps.setString(2, end);
				if(ps.executeUpdate() == 1) {
					response.getWriter().print("设置成功");
				}else {
					response.getWriter().print("设置失败");
				}	
			ps.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.getWriter().print("设置失败");
		}	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
