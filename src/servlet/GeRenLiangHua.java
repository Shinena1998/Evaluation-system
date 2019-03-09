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
 * Servlet implementation class GeRenLiangHua
 */
@WebServlet("/GeRenLiangHua")
public class GeRenLiangHua extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeRenLiangHua() {
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
		String shijian = request.getParameter("shijian");
		String xuenian = shijian.split(";")[0];
		String xueqi = shijian.split(";")[1];
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "select * from GeRenLiangHuaBiao where XueNian=? and XueQi=? order by DeFen DESC";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			int count = 0;
			while(rs.next()) {
				count ++;
				System.out.println(rs.getString("JiaoGongHao"));
				if(zhanghao.equals(rs.getString("JiaoGongHao"))) {
					for(int i = 1 ; i <= 4 ; i++) {
						jsonArray.add(rs.getString(i));
					}
					break;
				}
			}
			jsonArray.add(count);
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
