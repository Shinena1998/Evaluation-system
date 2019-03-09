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
 * Servlet implementation class DePaiMing
 */
@WebServlet("/DePaiMing")
public class DePaiMing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DePaiMing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		String zhanghao = request.getParameter("zhanghao");
		String shijian = request.getParameter("shijian");
		String xuenian = shijian.split(";")[0];
		String xueqi = shijian.split(";")[1];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "select * from JiaoGongPaiMingBiao where JiaoGongHao=? and XueNian=? and XueQi=?";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, zhanghao);
			ps.setString(2, xuenian);
			ps.setString(3, xueqi);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(rs.next()) {
				JSONArray jsonArray2 = new JSONArray();
				for (int i = 1; i <= 15; i++) {
					jsonArray2.add(rs.getString(i));
				}
				jsonArray.add(jsonArray2);
			}
			response.getWriter().print(jsonArray);
			rs.close();
			ps.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.getWriter().print("网络错误，请重试");
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
