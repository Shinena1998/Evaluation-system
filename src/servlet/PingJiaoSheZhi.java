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
 * Servlet implementation class PingJiaoSheZhi
 */
@WebServlet("/PingJiaoSheZhi")
public class PingJiaoSheZhi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PingJiaoSheZhi() {
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
		String dangqianxn = request.getParameter("dangqianxuenian");
		String dangqianxq = request.getParameter("dangqianxueqi");
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		boolean boo = true;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String	sql1 = "SELECT dangqianxuenian,dangqianxueqi,XueNian,XueQi FROM PeiZhiBiao";
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql1);
			JSONArray jsonArray= new JSONArray();
			if(rs.next()) {
				if(dangqianxn.equals(rs.getString(1)) && dangqianxq.equals(rs.getString(2))) {
					response.getWriter().print("当前学年学期已经设置评教，如果想修改时间请在当前设置界面右上角点击修改");
					boo = false;
				}
				xuenian  = xuenian+";"+rs.getString(3);
				xueqi = xueqi +";"+rs.getString(4);
				jsonArray.add(xuenian);
				jsonArray.add(xueqi);
			}
			if(boo) {
				String update = "update PeiZhiBiao set DangQianXueNian=?,DangQianXueQi=?,XueNian=?,XueQi=?,KaiShiPingJiaoShiJian=?,JieShuPingJiaoShiJian=? where GuanLiYuanZhangHao=?";
				PreparedStatement ps = null;
				ps = conn.prepareStatement(update);
				ps.setString(1, dangqianxn);
				ps.setString(2, dangqianxq);
				ps.setString(3, xuenian);
				ps.setString(4, xueqi);
				ps.setString(5, start);
				ps.setString(6, end);
				ps.setString(7, zhanghao);
				if(ps.executeUpdate() == 1) {
					response.getWriter().print("设置成功");
				}else {
					response.getWriter().print("设置失败");
				}	
				ps.close();
			}
			statement.close();
			rs.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.getWriter().print("设置失败");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("asdasd");
		doGet(request, response);
	}

}
