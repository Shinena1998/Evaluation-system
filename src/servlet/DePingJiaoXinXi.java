package servlet;

import java.io.Console;
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
 * Servlet implementation class DePingJiaoXinXi
 */
@WebServlet("/DePingJiaoXinXi")
public class DePingJiaoXinXi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DePingJiaoXinXi() {
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
		String zhanghao = request.getParameter("zhanghao");
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String jiaogong = request.getParameter("jiaogong");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "select * from XueShengPingJiaoGongDeFenBiao where XueNian=? and XueQi=? and XueHao=? and JiaoGongHao=? order by XiangMuBianHao asc";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, zhanghao);
			ps.setString(4, jiaogong );
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			String sql2 = "select * from XueShengPingYvBiao where XueNian=? and XueQi=? and XueHao=? and JiaoGongHao=?";
			System.out.println(sql2);
			ps = conn.prepareStatement(sql2);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, zhanghao);
			ps.setString(4, jiaogong );
			ResultSet rs2 = ps.executeQuery();
			while(rs.next()) {
				JSONArray jsonArray2 = new JSONArray();
				for(int i = 1 ; i <= 6 ; i++) {
					jsonArray2.add(rs.getString(i));
				}
				jsonArray.add(jsonArray2);
			}

			if(rs2.next()) {
				jsonArray.add(rs2.getString("NeiRong"));
			}
			response.getWriter().print(jsonArray);
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
