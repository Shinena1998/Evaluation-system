package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class ZongHeMingXinXi
 */
@WebServlet("/ZongHeMingXinXi")
public class ZongHeMingXinXi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZongHeMingXinXi() {
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
		String shijian = request.getParameter("shijian");
		String xuenian = shijian.split(";")[0];
		String xueqi = shijian.split(";")[1];
		String bumen = request.getParameter("bumen");
		String nianji = request.getParameter("nianji");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			String sql1 = "select XueNian,XueQi,JiaoGongHao,XingMing,XiangMuBianHao,XiangMuNeiRong,COUNT(XiangMuBianHao) as 'RenShu' from jiaogongduangerenmingxingxinxi where XueNian=? and XueQi=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=?)   GROUP BY XueNian,XueQi,JiaoGongHao,XiangMuBianHao,XiangMuNeiRong ORDER BY JiaoGongHao asc";
			System.out.println(sql1);
			ps = conn.prepareStatement(sql1);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, nianji);
			ps.setString(4, bumen);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray= new JSONArray();
			while(rs.next()) {
				JSONArray jsonArray2 = new JSONArray(); 
				for(int i = 1 ; i <= 7; i ++) {
					jsonArray2.add(rs.getString(i));
				}	
				jsonArray.add(jsonArray2);
			}
			String sql2 = "SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao";
			ps = conn.prepareStatement(sql2);
			ps.setString(1, nianji);
			ps.setString(2, bumen);
			ResultSet rs1 = ps.executeQuery();
			int count = 0;
			while(rs1.next()) {
				count ++;
			}
			jsonArray.add(count);
			response.getWriter().print(jsonArray);
			rs.close();
			rs1.close();
			ps.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
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
