package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShanChuJiaoGong
 */
@WebServlet("/ShanChuJiaoGong")
public class ShanChuJiaoGong extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShanChuJiaoGong() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String jiaogonghao = request.getParameter("jiaogonghao");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			Statement statement = conn.createStatement();
			String delete = "delete from JiaoGongBiao where JiaoGongHao=" + jiaogonghao;
			System.out.println(delete);
			int count = statement.executeUpdate(delete);
			String delete2 = "delete from XueShengYvJiaoGongDuiYingBiao where JiaoGongHao=" + jiaogonghao;
			System.out.println(delete2);
			 count = statement.executeUpdate(delete2);
			String delete3 = "delete from XueShengPingYvBiao where JiaoGongHao=" + jiaogonghao;
			System.out.println(delete3);
			count = statement.executeUpdate(delete3);
			String delete4 = "delete from XueShengPingJiaoGongDeFenBiao where JiaoGongHao=" + jiaogonghao;
			System.out.println(delete4);
			count = statement.executeUpdate(delete4);
			String delete5 = "delete from ShangJiPingJiaoGongDeFenBiao where ShangJiJiaoGongHao=" + jiaogonghao + " or BeiPingJiaoGongHao="+jiaogonghao;
			System.out.println(delete5);
			count = statement.executeUpdate(delete5);
			String delete6 = "delete from ShangJiYuJiaoGongDuiYingBiao where ShangJiJiaoGongHao=" + jiaogonghao + " or BeiPingJiaoGongHao="+jiaogonghao;
			System.out.println(delete6);
			count = statement.executeUpdate(delete6);
			String delete7 =  "delete from ShangJiCePingJiaoGong where ShangJiJiaoGongHao=" + jiaogonghao + " or BeiPingJiaoGongHao="+jiaogonghao;
			System.out.println(delete7);
			count = statement.executeUpdate(delete7);
			String delete8 = "delete from GeRenLiangHuaBiao where JiaoGongHao=" + jiaogonghao;
			System.out.println(delete8);
			count = statement.executeUpdate(delete8);
			String delete9 = "delete from MingXingJiaoShiDeFenBiao where JiaoGongHao=" + jiaogonghao;
			System.out.println(delete9);
			count = statement.executeUpdate(delete9);
			statement.close();
			conn.close();
		}catch (Exception e) {
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
