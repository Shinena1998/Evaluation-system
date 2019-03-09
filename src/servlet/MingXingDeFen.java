package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class MingXingDeFen
 */
@WebServlet("/MingXingDeFen")
public class MingXingDeFen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MingXingDeFen() {
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
		String name = request.getParameter("zhanghao");
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String[] contant = new String[10];
		String str = "abcdefghij";
		for (int i = 0; i < 10; i++) {
			contant[i] = request.getParameter(str.charAt(i)+"");
		}
		int count = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			String sql1 = "insert into MingXingJiaoShiDeFenBiao values(?,?,?,?,?)";
			for (int i = 0; i < 10; i++) {
				ps = conn.prepareStatement(sql1);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, name);
				ps.setString(4, contant[i]);
				ps.setInt(5, i+1);
				count = count + ps.executeUpdate();
			}
			if(count > 0) {
				response.getWriter().print("投票成功");
			}else {
				response.getWriter().print("投票失败");
			}
			ps.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.getWriter().print("投票失败");
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
