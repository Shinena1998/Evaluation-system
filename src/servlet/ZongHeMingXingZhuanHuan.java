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
import sun.tools.jar.resources.jar;

/**
 * Servlet implementation class ZongHeMingXingZhuanHuan
 */
@WebServlet("/ZongHeMingXingZhuanHuan")
public class ZongHeMingXingZhuanHuan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZongHeMingXingZhuanHuan() {
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
		String leixing = request.getParameter("leixing");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String jiaogong = "SELECT JiaoGongHao from  xueshengjiaogong WHERE NianJi=? and BuMen=? group by JiaoGongHao";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(jiaogong);
			ps.setString(1, nianji);
			ps.setString(2, bumen);
			ResultSet rs1 = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(rs1.next()) {
				jsonArray.add(rs1.getString(1));
			}
			JSONArray xueshengshu = new JSONArray();
			if(leixing.equals("学生数")) {
				String shumu = "SELECT COUNT(JiaoGongHao) from XueShengYvJiaoGongDuiYingBiao WHERE JiaoGongHao=?";
				for (int i = 0; i < jsonArray.size(); i++) {
					ps = conn.prepareStatement(shumu);
					ps.setString(1, jsonArray.getString(i));
					ResultSet rs2 = ps.executeQuery();
					while(rs2.next()) {
						System.out.println(rs2.getInt(1)+"asdas");
						xueshengshu.add(rs2.getInt(1));
					}
				}
			}else {
				String shumu = "SELECT BanJi from xueshengjiaogong WHERE JiaoGongHao=? GROUP BY BanJi";
				for (int i = 0; i < jsonArray.size(); i++) {
					ps = conn.prepareStatement(shumu);
					ps.setString(1, jsonArray.getString(i)); 
					ResultSet rs2 = ps.executeQuery();
					int count = 0;
					while(rs2.next()) {
						count ++;
					}
					System.out.println(count);
					xueshengshu.add(count);
				}
			}
			JSONArray jsonArray3 = new JSONArray();
			for (int i = 0; i < jsonArray.size(); i++) {
				String sql = "select XueNian,XueQi,JiaoGongHao,XingMing,XiangMuBianHao,XiangMuNeiRong,COUNT(XiangMuBianHao) as 'RenShu' from jiaogongduangerenmingxingxinxi where XueNian=? and XueQi=? and JiaoGongHao=? GROUP BY XueNian,XueQi,JiaoGongHao,XiangMuBianHao,XiangMuNeiRong ORDER BY XiangMuBianHao asc";
				ps = conn.prepareStatement(sql);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, jsonArray.getString(i));
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					JSONArray jsonArray2 = new JSONArray();
					for(int j = 1 ; j <= 6 ; j++) {
						jsonArray2.add(rs.getString(j));
					}
					int defen = rs.getInt(7); //该老师在该项的投票人数
					double tupiaolv = defen / (xueshengshu.getInt(i)+0.0) ;
					jsonArray2.add(tupiaolv);
					jsonArray3.add(jsonArray2);
				}
				
			}
			jsonArray3.add(jsonArray.size());
			response.getWriter().print(jsonArray3);
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
