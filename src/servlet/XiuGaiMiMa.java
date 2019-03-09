package servlet;

import java.io.Console;
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
 * Servlet implementation class XiuGaiMiMa
 */
@WebServlet("/XiuGaiMiMa")
public class XiuGaiMiMa extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XiuGaiMiMa() {
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
		String leixing = request.getParameter("leixing");
		String old = request.getParameter("old");
		String mima = request.getParameter("new");
		String str = "";
		if(leixing.equals("XueShengBiao")) {
			str = "XueHao";
		}else if(leixing.equals("JiaoGongBiao")) {
			str = "JiaoGongHao";
		}
		System.out.println(name);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			String sql1 = "select * from "+leixing+" where "+str+"=? and MiMa=?";
		    System.out.println(sql1);
			ps = conn.prepareStatement(sql1);
			ps.setString(1, name);
			ps.setString(2, old);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				String sql2 = "update "+leixing+" set MiMa=? where "+str+"=?";
				System.out.println(sql2);
				ps = conn.prepareStatement(sql2);
				ps.setString(1, mima);
				ps.setString(2, name);
				if(ps.executeUpdate() == 1) {
					response.getWriter().print("修改成功");
				}else {
					response.getWriter().print("网络错误，修改失败");
				}
			}else {
				response.getWriter().print("原密码错误。请重新输入");
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
