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

import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;

import net.sf.json.JSONArray;

/**
 * Servlet implementation class ZengHePaiMing
 */
@WebServlet("/ZengHePaiMing")
public class ZengHePaiMing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZengHePaiMing() {
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
		System.out.println(xuenian+xueqi+shijian);
		String bumen = request.getParameter("bumen");
		String nianji = request.getParameter("nianji");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;
			String sql1 = "SELECT XueNian,XueQi,JiaoGongHao,XiangMuBianHao,jiaogongxingming,jiaogongbumen,XueKeKeMu,sum(DeFen) as 'zongfen',COUNT(JiaoGongHao) as 'renshu' FROM jiaogongpaiming WHERE xuenian=? and XueQi=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) GROUP BY XueNian,XueQi,JiaoGongHao,XiangMuBianHao,XueKeKeMu ORDER BY JiaoGongHao";
			ps = conn.prepareStatement(sql1);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, nianji);
			ps.setString(4, bumen);
			ResultSet rs = ps.executeQuery();
			JSONArray jArray = new JSONArray();
			while (rs.next()) {
				JSONArray jsonArray = new JSONArray();
				for (int i = 3; i <= 7; i++) {
					jsonArray.add(rs.getString(i));
				}
				jsonArray.add(rs.getInt(8)/(rs.getInt(9)+0.0));
				jArray.add(jsonArray);
			}
			String sql2 = "SELECT * from JiaoGongPaiMingBiao where JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) ORDER BY JiaoGongHao";
			ps = conn.prepareStatement(sql2);
			ps.setString(1, nianji);
			ps.setString(2, bumen);
			ResultSet rSet = ps.executeQuery();
			JSONArray jArray2 = new JSONArray();
			while(rSet.next()) {
				JSONArray jsonArray = new JSONArray();
				for (int i = 3; i <= 15; i++) {
					jsonArray.add(rSet.getString(i));
				}
				jArray2.add(jsonArray);
			}
			JSONArray end = new JSONArray();
			end.add(jArray);
			end.add(jArray2);
			response.getWriter().print(end);
			rs.close();
			rSet.close();
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
