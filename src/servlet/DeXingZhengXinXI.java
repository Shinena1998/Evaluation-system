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
 * Servlet implementation class DeXingZhengXinXI
 */
@WebServlet("/DeXingZhengXinXI")
public class DeXingZhengXinXI extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeXingZhengXinXI() {
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
		String bumen = request.getParameter("bumen");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "SELECT BeiPingJiaoGongHao,XiangMuBianHao,COUNT(XiangMuBianHao) as 'renshu',SUM(DeFen) as defen  from ShangJiPingJiaoGongDeFenBiao where  xuenian=? and XueQi=? and BeiPingJiaoGongHao=? GROUP BY XiangMuBianHao,DeFen ORDER BY XiangMuBianHao";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, zhanghao);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(rs.next()) {
				JSONArray jsonArray2 = new JSONArray();
				for(int i = 1 ; i <= 4 ; i++) {
					jsonArray2.add(rs.getString(i));
				}
				jsonArray.add(jsonArray2);
			}
			
			String sql1 = "SELECT BeiPingJiaoGongHao,XiangMuBianHao,COUNT(xiangmubianHao) as 'renshu',SUM(DeFen) from ShangJiPingJiaoGongDeFenBiao where  xuenian=? and XueQi=? and BeiPingJiaoGongHao in (SELECT JiaoGongHao from JiaoGongBiao where ZhiBie like '%行政' and BuMen = ?) GROUP BY XiangMuBianHao,BeiPingJiaoGongHao ORDER BY BeiPingJiaoGongHao;";
			ps = conn.prepareStatement(sql1);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3,bumen);
			ResultSet rs1 = ps.executeQuery();
			JSONArray jsonArray1 = new JSONArray();
			while(rs1.next()) {
				JSONArray jsonArray2 = new JSONArray();
				for(int i = 1 ; i <= 4 ; i++) {
					jsonArray2.add(rs1.getInt(i));
				}
				jsonArray1.add(jsonArray2);
			}
			JSONArray jsonArray3 = new JSONArray();
			for (int i = 0; i < jsonArray1.size()/3; i++) {
				JSONArray jsonArray2 = new JSONArray();
				double fenshu= jsonArray1.getJSONArray(i*3).getInt(3) / (jsonArray1.getJSONArray(i*3).getInt(2)*5+0.0)*30 + jsonArray1.getJSONArray(i*3+1).getInt(3) / (jsonArray1.getJSONArray(i*3+1).getInt(2)*5+0.0)*30 + jsonArray1.getJSONArray(i*3+2).getInt(3) / (jsonArray1.getJSONArray(i*3+2).getInt(2)*5+0.0)*40;
				jsonArray2.add(jsonArray1.getJSONArray(i*3).getInt(0));
				jsonArray2.add(fenshu);
				jsonArray3.add(jsonArray2);
			}
			for (int i = 0; i < jsonArray3.size()-1; i++) {
				for (int j = 0; j < jsonArray3.size()-1-j; j++) {
					if(jsonArray3.getJSONArray(j).getDouble(1) < jsonArray3.getJSONArray(j+1).getDouble(1)) {
						JSONArray jsonArray2 = jsonArray3.getJSONArray(j);;
						jsonArray3.remove(j);
						jsonArray3.add(j+1,jsonArray2);
					}
				}
			}
			int count = 1;
			for (int i = 0; i < jsonArray3.size(); i++) {
				if(i>0) {
					if (jsonArray3.getJSONArray(i).getDouble(1) == jsonArray3.getJSONArray(i-1).getDouble(1)) {
						jsonArray3.getJSONArray(i).add(count);
					}else {
						count ++;
						jsonArray3.getJSONArray(i).add(count);
					}
				}else {
					jsonArray3.getJSONArray(i).add(count);
				}
				if(zhanghao.equals(jsonArray3.getJSONArray(i).getInt(0)+"")) {
					jsonArray.add(jsonArray3.getJSONArray(i));
					break;
				}
			}
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
