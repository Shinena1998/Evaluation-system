package servlet;

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

import com.mysql.cj.xdevapi.JsonArray;

import net.sf.json.JSONArray;
import sun.tools.jar.resources.jar;


/**
 * Servlet implementation class JiaoGongPaiMing
 */
@WebServlet("/JiaoGongPaiMing")
public class JiaoGongPaiMing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JiaoGongPaiMing() {
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
		String xuenian = request.getParameter("xuenian");
		String xueqi = request.getParameter("xueqi");
		String zhanghao = request.getParameter("zhanghao");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			//学生评教排名
			String sql = "SELECT XueNian,XueQi,JiaoGongHao,sum(DeFen) as 'zongfen',COUNT(JiaoGongHao) as 'renshu' FROM XueShengPingJiaoGongDeFenBiao WHERE xuenian=? and XueQi=?  GROUP BY XueNian,XueQi,JiaoGongHao";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ResultSet rSet = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArray2 = new JSONArray();//学生评分表
			int mark=0;
			int count = 0;
			while(rSet.next()) {
				if(rSet.getString(3).equals(zhanghao)) {
					mark = count;
				}
				JSONArray jsonArray3 = new JSONArray();
				double zongfen = rSet.getInt(4) / ((rSet.getInt(5)*5)+0.0) * 100;
				jsonArray3.add(rSet.getString(3));
				jsonArray3.add(zongfen);
				jsonArray2.add(jsonArray3);
				count ++;
			}
			int xspaiming = 1;
			for (int i = 0; i < jsonArray2.size(); i++) {
				if(jsonArray2.getJSONArray(mark).getDouble(1) < jsonArray2.getJSONArray(i).getDouble(1)) {
					xspaiming ++;
				}
			}
			jsonArray.add(xspaiming);
			
			//上级测评排名
			String sql2 = "SELECT XueNian,XueQi,BeiCeJiaoGongHao,sum(DeFen) as 'zongfen',COUNT(BeiCeJiaoGongHao) as 'renshu' FROM ShangJiCePingJiaoGong WHERE xuenian=? and XueQi=?  GROUP BY XueNian,XueQi,BeiCeJiaoGongHao";
			ps = conn.prepareStatement(sql2);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray4 = new JSONArray(); //上级评分表
			int mark2 = 0;
			int count2 = 0;
			while(rSet.next()) {
				if(rSet.getString(3).equals(zhanghao)) {
					mark2 = count2;
				}
				JSONArray jsonArray5 = new JSONArray();
				double zongfen = rSet.getInt(4) / (rSet.getInt(5)*5) * 100;
				jsonArray5.add(rSet.getString(3));
				jsonArray5.add(zongfen);
				jsonArray4.add(jsonArray5);
				count2 ++;
			}
			int sjpaiming = 1;
			for (int i = 0; i < jsonArray4.size(); i++) {
				if(jsonArray4.getJSONArray(mark2).getDouble(1) < jsonArray4.getJSONArray(i).getDouble(1)) {
					sjpaiming ++;
				}
			}
			jsonArray.add(sjpaiming);
			
			
			
			//总排名
//			JSONArray jsonArray6 = new JSONArray();
//			JSONArray longer = jsonArray2;//学生
//			JSONArray shorter = jsonArray4;//上级
//			for (int i = 0; i < longer.size(); i++) {
//				JSONArray jsonArray3 = new JSONArray();
//				boolean boo = false;
//				for (int j = 0; j < shorter.size(); j++) {
//					//两个表都有
//					if(longer.getJSONArray(i).getString(0).equals(shorter.getJSONArray(j).getString(0))) {
//						jsonArray3.add(longer.getJSONArray(i).getString(0));
//						jsonArray3.add(longer.getJSONArray(i).getDouble(1)*0.4+shorter.getJSONArray(j).getDouble(1)*0.1);
//						boo = true;
//						break;
//					}
//				}
//				//如果两个表都有，
//				if(boo) {
//					jsonArray6.add(jsonArray3);
//				}else {//只有一个表有
//					jsonArray3.add(longer.getJSONArray(i).getString(0));
//					jsonArray3.add(longer.getJSONArray(i).getDouble(1)*0.4);
//					jsonArray6.add(jsonArray3);
//				}
//			}
//			for (int i = 0; i < shorter.size(); i++) {
//				JSONArray jsonArray3 = new JSONArray();
//				boolean boo = false;
//				for (int j = 0; j < longer.size(); j++) {
//					//两个表都有,上边已经记录，这次不作处理
//					if(shorter.getJSONArray(i).getString(0).equals(longer.getJSONArray(j).getString(0))) {
//						boo = true;
//						break;
//					}
//				}
//				//只有一个表有
//				if(!boo) {
//					jsonArray3.add(shorter.getJSONArray(i).getString(0));
//					jsonArray3.add(shorter.getJSONArray(i).getDouble(1));
//					jsonArray6.add(jsonArray3);
//				}
//			}
//			double zongfen = jsonArray4.getJSONArray(mark2).getDouble(1);
			
			//System.out.println(jsonArray);
			response.getWriter().print(jsonArray);
//			request.getRequestDispatcher("manager.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
