package servlet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TianJiaJiaoGong
 */
@WebServlet("/TianJiaJiaoGong")
public class TianJiaJiaoGong extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TianJiaJiaoGong() {
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
		String xingming = request.getParameter("xingming");
		String jiaogonghao = request.getParameter("jiaogonghao");
		String mima = request.getParameter("mima");
		System.out.println(mima + "asdas");
		if(mima == "") {
			String string="1234567809qwertyuiopasdfghjklzxcvbnm";
	        for (int i = 0; i < 6; i++) {
				mima += string.charAt(new Random().nextInt(36));
			}
		}

		System.out.println(mima + "asdas");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "insert into JiaoGongBiao(JiaoGongHao,XingMing,MiMa,ShiFouTiJiaoXinXi) values(?,?,?,?)";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, jiaogonghao);
			ps.setString(2, xingming);
			ps.setString(3, mima);
			ps.setBoolean(4, false);
			int count = ps.executeUpdate();
			System.out.println(count);
			if(count>0){
				response.getWriter().print("添加成功\n"+xingming+"教工的初始密码为"+mima);
			}
		}catch (SQLIntegrityConstraintViolationException e) {
			response.getWriter().print("此教工已存在");
			// TODO: handle exception
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("添加失败");
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
