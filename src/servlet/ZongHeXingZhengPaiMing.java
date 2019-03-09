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
 * Servlet implementation class ZongHeXingZhengPaiMing
 */
@WebServlet("/ZongHeXingZhengPaiMing")
public class ZongHeXingZhengPaiMing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZongHeXingZhengPaiMing() {
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
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			//行政人员名单
			String xingzheng = "SELECT JiaoGongHao,xingming from JiaoGongBiao where ZhiBie like '%行政' and BuMen = ? group by jiaogonghao";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(xingzheng);
			ps.setString(1, bumen);
			ResultSet rs2 = ps.executeQuery();
			JSONArray jArray = new JSONArray();
			while(rs2.next()) {
				JSONArray jsonArray2 = new JSONArray();
				for(int i = 1 ; i <= 2 ; i++) {
					jsonArray2.add(rs2.getString(i));
				}
				jArray.add(jsonArray2);
			}
			//基本信息
			String sql = "SELECT BeiPingJiaoGongHao,XiangMuBianHao,COUNT(XiangMuBianHao) as 'renshu',SUM(DeFen) as defen  from ShangJiPingJiaoGongDeFenBiao where  xuenian=? and XueQi=? and BeiPingJiaoGongHao in (SELECT JiaoGongHao from JiaoGongBiao where ZhiBie like '%行政' and BuMen = ?) GROUP BY XiangMuBianHao,DeFen,BeiPingJiaoGongHao";
			ps = conn.prepareStatement(sql);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3, bumen);
			ResultSet rs = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(rs.next()) {
				JSONArray jsonArray2 = new JSONArray();
				jsonArray2.add(rs.getString(1));
				for(int i = 2 ; i <= 4 ; i++) {
					jsonArray2.add(rs.getInt(i));
				}
				jsonArray.add(jsonArray2);
			}
			JSONArray jsonArray5 = new JSONArray();
			for (int i = 0; i < jArray.size(); i++) {
				JSONArray jsonArray2 = new JSONArray();
				for (int j = 0; j < jsonArray.size(); j++) {
					if(jArray.getJSONArray(i).getString(0).equals(jsonArray.getJSONArray(j).getString(0))) {
						jsonArray2.add(jsonArray.getJSONArray(j));
					}
				}
				jsonArray5.add(jsonArray2);
			}
			
			//各项排名
			JSONArray xm = new JSONArray();
			String sql2 = "SELECT BeiPingJiaoGongHao,COUNT(xiangmubianHao) as 'renshu',SUM(DeFen) from ShangJiPingJiaoGongDeFenBiao where  xuenian=? and XueQi=? and BeiPingJiaoGongHao in (SELECT JiaoGongHao from JiaoGongBiao where ZhiBie like '%行政' and BuMen = ?) and XiangMuBianHao=? GROUP BY XiangMuBianHao,BeiPingJiaoGongHao ORDER BY BeiPingJiaoGongHao";
			for (int i = 1; i <= 3; i++) {
				ps = conn.prepareStatement(sql2);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setString(3,bumen);
				ps.setInt(4, i);
				ResultSet rs3 = ps.executeQuery();
				JSONArray jsonArray6 = new JSONArray();
				while(rs3.next()) {
					JSONArray jsonArray2 = new JSONArray();
					jsonArray2.add(rs3.getString(1));
					int defen =rs3.getInt(3);
					int renshu = rs3.getInt(2);
					int quan = 30;
					if(i == 3) {
						quan = 40;
					}
					double zong = defen / (renshu*5+0.0) * quan;
					jsonArray2.add(zong);
					jsonArray6.add(jsonArray2);
				}
				for (int t = 0; t < jsonArray6.size()-1; t++) {
					for (int j = 0; j < jsonArray6.size()-1-t; j++) {
						if(jsonArray6.getJSONArray(j).getDouble(1) < jsonArray6.getJSONArray(j+1).getDouble(1)) {
							JSONArray jsonArray2 = jsonArray6.getJSONArray(j);;
							jsonArray6.remove(j);
							jsonArray6.add(j+1,jsonArray2);
						}
					}
				}
				int count = 1;
				for (int j = 0; j < jsonArray6.size(); j++) {
					if(j>0) {
						if (jsonArray6.getJSONArray(j).getDouble(1) == jsonArray6.getJSONArray(j-1).getDouble(1)) {
							jsonArray6.getJSONArray(j).add(count);
						}else {
							count ++;
							jsonArray6.getJSONArray(j).add(count);
						}
					}else {
						jsonArray6.getJSONArray(j).add(count);
					}
				}
				xm.add(jsonArray6);
			}

			//总排名
			String sql1 = "SELECT BeiPingJiaoGongHao,XiangMuBianHao,COUNT(xiangmubianHao) as 'renshu',SUM(DeFen) from ShangJiPingJiaoGongDeFenBiao where  xuenian=? and XueQi=? and BeiPingJiaoGongHao in (SELECT JiaoGongHao from JiaoGongBiao where ZhiBie like '%行政' and BuMen = ?) GROUP BY XiangMuBianHao,BeiPingJiaoGongHao ORDER BY BeiPingJiaoGongHao";
			ps = conn.prepareStatement(sql1);
			ps.setString(1, xuenian);
			ps.setString(2, xueqi);
			ps.setString(3,bumen);
			ResultSet rs1 = ps.executeQuery();
			JSONArray jsonArray1 = new JSONArray();
			while(rs1.next()) {
				JSONArray jsonArray2 = new JSONArray();
				jsonArray2.add(rs1.getString(1));
				for(int i = 2 ; i <= 4 ; i++) {
					jsonArray2.add(rs1.getInt(i));
				}
				jsonArray1.add(jsonArray2);
			}
			JSONArray jsonArray3 = new JSONArray();
			for (int i = 0; i < jsonArray1.size()/3; i++) {
				JSONArray jsonArray2 = new JSONArray();
				double fenshu= jsonArray1.getJSONArray(i*3).getInt(3) / (jsonArray1.getJSONArray(i*3).getInt(2)*5+0.0)*30 + jsonArray1.getJSONArray(i*3+1).getInt(3) / (jsonArray1.getJSONArray(i*3+1).getInt(2)*5+0.0)*30 + jsonArray1.getJSONArray(i*3+2).getInt(3) / (jsonArray1.getJSONArray(i*3+2).getInt(2)*5+0.0)*40;
				jsonArray2.add(jsonArray1.getJSONArray(i*3).getString(0));
				jsonArray2.add(fenshu);
				jsonArray3.add(jsonArray2);
			}
			for (int i = 0; i < jsonArray3.size()-1; i++) {
				for (int j = 0; j < jsonArray3.size()-1-i; j++) {
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
			}
			for (int i = 0; i <	jsonArray3.size(); i++) {
				for (int j = 0; j < jArray.size(); j++) {
					if(jsonArray3.getJSONArray(i).getString(0).equals(jArray.getJSONArray(j).getString(0))) {
						jsonArray3.getJSONArray(i).add(jArray.getJSONArray(j).getString(1));
					}
				}
			}
			JSONArray ja = new JSONArray();
			ja.add(jsonArray3); //加入总排名
			ja.add(jsonArray5); // 加入基础信息
			ja.add(xm); //加入各项总分排名
			response.getWriter().print(ja);
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
