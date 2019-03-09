package servlet;

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
 * Servlet implementation class XieShangJiCePing
 */
@WebServlet("/XieShangJiCePing")
public class XieShangJiCePing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XieShangJiCePing() {
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
		int length = Integer.parseInt(request.getParameter("length"));
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String[] defen = new String[length];
		String[] jiaogong = new String[length];
		String str = "xyz";
		for (int i = 0; i < length; i++) {
			int j = i+70;
			defen[i] = request.getParameter(j+"");
		
		}
		for (int i = 0; i < length; i++) {
			int j = i+3000;
			jiaogong[i] = request.getParameter(j+"");
		}

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			int num = 0;
			for (int i = 0; i < length; i++) {
				int count = 0;
				String sql1 = "insert into ShangJiCePingJiaoGong values(?,?,?,?,?)";
				ps = conn.prepareStatement(sql1);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, name);
				ps.setString(4, jiaogong[i]);
				ps.setString(5, defen[i]);
				count = ps.executeUpdate();
				System.out.println(count);
				if(count == 1) {
					String sql2 = "update ShangJiYuJiaoGongDuiYingBiao set ShiFouYiPingJiaoCePing=1 where XueNian=? and XueQi=? and ShangJiJiaoGongHao=? and BeiPingJiaoGongHao=?";
					ps = conn.prepareStatement(sql2);
					ps.setString(1, xuenian);
					ps.setString(2, xueqi);
					ps.setString(3, name);
					ps.setString(4, jiaogong[i]);
					num +=ps.executeUpdate();
				}
			}
			if(num == length) {
				response.getWriter().print("评教成功");
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
