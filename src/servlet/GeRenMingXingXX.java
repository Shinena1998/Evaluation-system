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

import org.apache.commons.lang.math.DoubleRange;
import org.w3c.dom.DocumentFragment;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class GeRenMingXingXX
 */
@WebServlet("/GeRenMingXingXX")
public class GeRenMingXingXX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GeRenMingXingXX() {
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
		String bumen=null;
		String nianji=null;
		String leixing = request.getParameter("leixing");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			//获得部门年级
			String jiaogong = "SELECT BuMen,NianJi from  xueshengjiaogong WHERE JiaoGongHao=? limit 1";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(jiaogong);
			ps.setString(1, zhanghao);
			ResultSet rs2 = ps.executeQuery();
			while(rs2.next()) {
				bumen = rs2.getString(1);
				nianji = rs2.getString(2);
			}
			int jgrenshu = 0;
			String jiaogong1 = "SELECT JiaoGongHao from  xueshengjiaogong WHERE NianJi=? and BuMen=? group by JiaoGongHao";
			ps = conn.prepareStatement(jiaogong1);
			ps.setString(1, nianji);
			ps.setString(2, bumen);
			ResultSet rs5 = ps.executeQuery();
			while(rs5.next()) {
				jgrenshu++;
			}
			int xueshengshu = 0;
			if(leixing.equals("学生数")) {
				String shumu = "SELECT COUNT(JiaoGongHao) from XueShengYvJiaoGongDuiYingBiao WHERE JiaoGongHao=?";
				ps = conn.prepareStatement(shumu);
				ps.setString(1, zhanghao);
				ResultSet rs1 = ps.executeQuery();
				while(rs1.next()) {
					xueshengshu = rs1.getInt(1);
				}
			}else {
				String shumu = "SELECT BanJi from xueshengjiaogong WHERE JiaoGongHao=? GROUP BY BanJi";
				ps = conn.prepareStatement(shumu);
				ps.setString(1, zhanghao);
				ResultSet rs1 = ps.executeQuery();
				while(rs1.next()) {
					xueshengshu ++;
				}
			}
			//获得没项人数
			String sql = "select XueNian,XueQi,JiaoGongHao,XiangMuBianHao,XiangMuNeiRong,COUNT(XiangMuBianHao) as 'RenShu' from jiaogongduangerenmingxingxinxi where XueNian=? and XueQi=? and JiaoGongHao=? GROUP BY XueNian,XueQi,JiaoGongHao,XiangMuBianHao,XiangMuNeiRong ORDER BY XiangMuBianHao asc";
			ps = conn.prepareStatement(sql);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, zhanghao);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(rs.next()) {
				System.out.println(rs.getString(3));
				JSONArray jsonArray2 = new JSONArray();
				for(int i = 1 ; i <= 6 ; i++) {
					jsonArray2.add(rs.getString(i));
				}
				int defen = rs.getInt(6); //该老师在该项的投票人数
				double tupiaolv = defen / (xueshengshu+0.0) ;
				System.out.println("tupiaolv="+tupiaolv);
				//获得每项的每个老师的人数
				String sql2 = "select JiaoGongHao,COUNT(XiangMuBianHao) as 'RenShu' from MingXingJiaoShiDeFenBiao where XueNian=? and XueQi=? and XiangMuBianHao=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) GROUP BY XueNian,XueQi,JiaoGongHao,XiangMuBianHao ORDER BY RenShu desc";
				ps = conn.prepareStatement(sql2);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3, rs.getString("XiangMuBianHao"));
				ps.setString(4, nianji);
				ps.setString(5, bumen);
				ResultSet r2 = ps.executeQuery();
				int count = 1;
				while(r2.next()) {
					String sql3 = "SELECT COUNT(JiaoGongHao) from XueShengYvJiaoGongDuiYingBiao WHERE JiaoGongHao=?";
					ps = conn.prepareStatement(sql3);
					ps.setString(1, r2.getString(1));
					ResultSet r3 = ps.executeQuery();
					int num = 0;
					if(r3.next()) {
						num = r3.getInt(1);
					}
					double dpl= r2.getInt(2) / (num + 0.0);
					if(tupiaolv < dpl) {
						count ++;
					}
				}
				jsonArray2.add(tupiaolv);
				jsonArray2.add(count);
				jsonArray.add(jsonArray2);
			}
			jsonArray.add(jgrenshu);
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
