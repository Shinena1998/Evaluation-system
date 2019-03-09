package servlet;

import java.beans.Transient;
import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class XiePingJiaoDeFen
 */
@WebServlet("/XiePingJiaoDeFen")
public class XiePingJiaoDeFen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XiePingJiaoDeFen() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Transient
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String name = request.getParameter("zhanghao");
		String jiaogong = request.getParameter("tea");
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String[] contant = new String[11];
		String str = "klmnopqrstu";
		System.out.println(jiaogong);
		for (int i = 0; i < 11; i++) {
			contant[i] = request.getParameter(str.charAt(i)+"");
		}
		System.out.println(contant[10]);
		int count = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			String sql1 = "insert into XueShengPingJiaoGongDeFenBiao values(?,?,?,?,?,?)";
			for (int i = 0; i < 10; i++) {
				ps = conn.prepareStatement(sql1);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, name);
				ps.setString(4, jiaogong);
				ps.setString(5, i+1+"");
				ps.setString(6, contant[i]);
				count = count + ps.executeUpdate();
				System.out.println(count);
			}
			if(contant[10] != null && contant[10] != "") {
				String sql3 = "insert into XueShengPingYvBiao values(?,?,?,?,?)";
				ps = conn.prepareStatement(sql3);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, name);
				ps.setString(4, jiaogong);
				ps.setString(5, contant[10]);
				count = count + ps.executeUpdate();
				System.out.println(count);
			}
			if(count > 0) {
				String sql2 = "update XueShengYvJiaoGongDuiYingBiao set ShiFouYiPingJiao=1 where XueNian=? and XueQi=? and XueHao=? and JiaoGongHao=?";
				ps = conn.prepareStatement(sql2);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, name);
				ps.setString(4, jiaogong);
				if(ps.executeUpdate() == 1) {
					response.getWriter().print("评教成功");
				}else {
					response.getWriter().print("评教失败");
				}
			}else {
				response.getWriter().print("评教失败");
			}
			ps.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			response.getWriter().print("评教失败");
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
