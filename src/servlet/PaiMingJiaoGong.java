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
 * Servlet implementation class PaiMingJiaoGong
 */
@WebServlet("/PaiMingJiaoGong")
public class PaiMingJiaoGong extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaiMingJiaoGong() {
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
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";

			String[] bumen = {"小学","初中","高中"};
			String[] nianji = {"1","2","3","4","5","6"};
			Connection conn = DriverManager.getConnection(url, username, Password);
			PreparedStatement ps = null;

			JSONArray jiaogongbiao = new JSONArray();
			for (int x = 0; x < bumen.length; x++) {
				if(x == 0) {
					for (int y = 0; y < nianji.length; y++) {
						String select = "SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao";
						ps = conn.prepareStatement(select);
						ps.setString(1, nianji[y]);
						ps.setString(2, bumen[x]);
						ResultSet rSet = ps.executeQuery();
						JSONArray jsonArray4 = new JSONArray();
						while(rSet.next()) {
							jsonArray4.add(rSet.getString("JiaoGongHao"));
						}
						jiaogongbiao.add(jsonArray4);
					}
				}else {
					for (int y = 0; y < 3; y++) {
						String select = "SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao";
						ps = conn.prepareStatement(select);
						ps.setString(1, nianji[y]);
						ps.setString(2, bumen[x]);
						ResultSet rSet = ps.executeQuery();
						JSONArray jsonArray4 = new JSONArray();
						while(rSet.next()) {
							jsonArray4.add(rSet.getString("JiaoGongHao"));
						}
						jiaogongbiao.add(jsonArray4);
					}
				}
			}
			
			JSONArray jsonArray = new JSONArray();
			JSONArray xuesheng = new JSONArray();
			for (int x = 0; x < bumen.length; x++) {
				if(x == 0) {
					for (int y = 0; y < nianji.length; y++) {
						//学生评教排名
						String sql = "SELECT XueNian,XueQi,JiaoGongHao,sum(DeFen) as 'zongfen',COUNT(JiaoGongHao) as 'renshu' FROM XueShengPingJiaoGongDeFenBiao WHERE  xuenian=? and XueQi=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao)  GROUP BY XueNian,XueQi,JiaoGongHao order by zongfen desc";
						ps = conn.prepareStatement(sql);
						ps.setString(1, xuenian);
						ps.setString(2, xueqi);
						ps.setString(3, nianji[y]);
						ps.setString(4, bumen[x]);
						ResultSet rSet = ps.executeQuery();
						JSONArray jsonArray2 = new JSONArray();//学生评分表
						JSONArray jsonArray5 = new JSONArray();
						while(rSet.next()) {
							System.out.println("asd");
							JSONArray jsonArray3 = new JSONArray();
							double zongfen = rSet.getInt(4) / ((rSet.getInt(5)*5)+0.0) * 100;
							jsonArray3.add(rSet.getString(3));
							jsonArray3.add(zongfen);
							jsonArray2.add(jsonArray3);
						}
						for (int t = 0; t < jiaogongbiao.getJSONArray(y).size(); t++) {
							double grade = 0;
							int paiming = 1;
							for (int i = 0; i < jsonArray2.size(); i++) {
								if(jiaogongbiao.getJSONArray(y).getString(t).equals(jsonArray2.getJSONArray(i).getString(0))) {
									grade = jsonArray2.getJSONArray(i).getDouble(1);
								}
							}
							for (int i = 0; i < jsonArray2.size(); i++) {
								if(grade < jsonArray2.getJSONArray(i).getDouble(1)) {
									paiming ++;
								}
							}
							JSONArray jsonArray3 = new JSONArray();
							jsonArray3.add(jiaogongbiao.getJSONArray(y).getString(t));
							jsonArray3.add(grade);
							jsonArray3.add(paiming);
							jsonArray3.add(paiming/(jiaogongbiao.getJSONArray(y).size()+0.0));
							jsonArray5.add(jsonArray3);
						}
						xuesheng.add(jsonArray5);
					}
				}else {
					for (int y = 0;y < 3; y++) {
						//学生评教排名
						String sql = "SELECT XueNian,XueQi,JiaoGongHao,sum(DeFen) as 'zongfen',COUNT(JiaoGongHao) as 'renshu' FROM XueShengPingJiaoGongDeFenBiao WHERE  xuenian=? and XueQi=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao)  GROUP BY XueNian,XueQi,JiaoGongHao order by zongfen desc";
						ps = conn.prepareStatement(sql);
						ps.setString(1, xuenian);
						ps.setString(2, xueqi);
						ps.setString(3, nianji[y]);
						ps.setString(4, bumen[x]);
						ResultSet rSet = ps.executeQuery();
						JSONArray jsonArray2 = new JSONArray();//学生评分表
						JSONArray jsonArray5 = new JSONArray();
						while(rSet.next()) {
							System.out.println("asd");
							JSONArray jsonArray3 = new JSONArray();
							double zongfen = rSet.getInt(4) / ((rSet.getInt(5)*5)+0.0) * 100;
							jsonArray3.add(rSet.getString(3));
							jsonArray3.add(zongfen);
							jsonArray2.add(jsonArray3);
						}
						for (int t = 0; t < jiaogongbiao.getJSONArray(6+y+(x-1)*3).size(); t++) {
							double grade = 0;
							int paiming = 1;
							for (int i = 0; i < jsonArray2.size(); i++) {
								if(jiaogongbiao.getJSONArray(6+y+(x-1)*3).getString(t).equals(jsonArray2.getJSONArray(i).getString(0))) {
									grade = jsonArray2.getJSONArray(i).getDouble(1);
								}
							}
							for (int i = 0; i < jsonArray2.size(); i++) {
								if(grade < jsonArray2.getJSONArray(i).getDouble(1)) {
									paiming ++;
								}
							}
							JSONArray jsonArray3 = new JSONArray();
							jsonArray3.add(jiaogongbiao.getJSONArray(6+y+(x-1)*3).getString(t));
							jsonArray3.add(grade);
							jsonArray3.add(paiming);
							jsonArray3.add(paiming/(jiaogongbiao.getJSONArray(6+y+(x-1)*3).size()+0.0));
							jsonArray5.add(jsonArray3);
						}
						xuesheng.add(jsonArray5);
					}
				}
				
			}
			
//			for (int i = 0; i < jsonArray2.size()-1; i++) {
//				for (int j = 0; j < jsonArray2.size()-i-1; j++) {
//					if(jsonArray2.getJSONArray(j).getDouble(1) < jsonArray2.getJSONArray(j+1).getDouble(1)) {
//						JSONArray jsonArray3 = jsonArray2.getJSONArray(j);
//						jsonArray2.remove(j);//删除j位，j+1成为j位
//						jsonArray2.add(j+1, jsonArray3);
//					}
//				}
//			}
			
			//上级测评排名
			JSONArray shangji = new JSONArray();
			for (int x = 0; x < bumen.length; x++) {
				if(x == 0) {
					for (int y = 0; y < nianji.length; y++) {
						String sql2 = "SELECT XueNian,XueQi,BeiCeJiaoGongHao,sum(DeFen) as 'zongfen',COUNT(BeiCeJiaoGongHao) as 'renshu' FROM ShangJiCePingJiaoGong WHERE xuenian=? and XueQi=?  and BeiCeJiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) GROUP BY XueNian,XueQi,BeiCeJiaoGongHao ORDER BY zongfen desc";
						ps = conn.prepareStatement(sql2);
						ps.setString(1, xuenian);
						ps.setString(2, xueqi);
						ps.setString(3, nianji[y]);
						ps.setString(4, bumen[x]);
						ResultSet rs = ps.executeQuery();
						JSONArray jsonArray4 = new JSONArray(); //上级评分表
						JSONArray jsonArray6 = new JSONArray();
						while(rs.next()) {
							JSONArray jsonArray5 = new JSONArray();
							double zongfen = rs.getInt(4) / ((rs.getInt(5)*5)+0.0) * 100;
							jsonArray5.add(rs.getString(3));
							jsonArray5.add(zongfen);
							jsonArray4.add(jsonArray5);
						}
						for (int t = 0; t < jiaogongbiao.getJSONArray(y).size(); t++) {
							double grade = 0;
							int paiming = 1;
							for (int i = 0; i < jsonArray4.size(); i++) {
								if(jiaogongbiao.getJSONArray(y).getString(t).equals(jsonArray4.getJSONArray(i).getString(0))) {
									grade = jsonArray4.getJSONArray(i).getDouble(1);
								}
							}
							for (int i = 0; i < jsonArray4.size(); i++) {
								if(grade < jsonArray4.getJSONArray(i).getDouble(1)) {
									paiming ++;
								}
							}
							JSONArray jsonArray3 = new JSONArray();
							jsonArray3.add(jiaogongbiao.getJSONArray(y).getString(t));
							jsonArray3.add(grade);
							jsonArray3.add(paiming);
							jsonArray3.add(paiming/(jiaogongbiao.getJSONArray(y).size()+0.0));
							jsonArray6.add(jsonArray3);
						}
						shangji.add(jsonArray6);
					}
				}else {
					for (int y = 0;y < 3; y++) {
						String sql2 = "SELECT XueNian,XueQi,BeiCeJiaoGongHao,sum(DeFen) as 'zongfen',COUNT(BeiCeJiaoGongHao) as 'renshu' FROM ShangJiCePingJiaoGong WHERE xuenian=? and XueQi=?  and BeiCeJiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) GROUP BY XueNian,XueQi,BeiCeJiaoGongHao ORDER BY zongfen desc";
						ps = conn.prepareStatement(sql2);
						ps.setString(1, xuenian);
						ps.setString(2, xueqi);
						ps.setString(3, nianji[y]);
						ps.setString(4, bumen[x]);
						ResultSet rs = ps.executeQuery();
						JSONArray jsonArray4 = new JSONArray(); //上级评分表
						JSONArray jsonArray6 = new JSONArray();
						while(rs.next()) {
							JSONArray jsonArray5 = new JSONArray();
							double zongfen = rs.getInt(4) / ((rs.getInt(5)*5)+0.0) * 100;
							jsonArray5.add(rs.getString(3));
							jsonArray5.add(zongfen);
							jsonArray4.add(jsonArray5);
						}
						for (int t = 0; t < jiaogongbiao.getJSONArray(6+y+(x-1)*3).size(); t++) {
							double grade = 0;
							int paiming = 1;
							for (int i = 0; i < jsonArray4.size(); i++) {
								if(jiaogongbiao.getJSONArray(6+y+(x-1)*3).getString(t).equals(jsonArray4.getJSONArray(i).getString(0))) {
									grade = jsonArray4.getJSONArray(i).getDouble(1);
								}
							}
							for (int i = 0; i < jsonArray4.size(); i++) {
								if(grade < jsonArray4.getJSONArray(i).getDouble(1)) {
									paiming ++;
								}
							}
							JSONArray jsonArray3 = new JSONArray();
							jsonArray3.add(jiaogongbiao.getJSONArray(6+y+(x-1)*3).getString(t));
							jsonArray3.add(grade);
							jsonArray3.add(paiming);
							jsonArray3.add(paiming/(jiaogongbiao.getJSONArray(6+y+(x-1)*3).size()+0.0));
							jsonArray6.add(jsonArray3);
						}
						shangji.add(jsonArray6);
					}
				}
			}
			
			//个人量化
			JSONArray geren = new JSONArray();
			for (int x = 0; x < bumen.length; x++) {
				if(x == 0) {
					for (int y = 0; y < nianji.length; y++) {
						String sql3 = "SELECT * FROM GeRenLiangHuaBiao WHERE XueNian=? and XueQi=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) ORDER BY DeFen desc";
						ps = conn.prepareStatement(sql3);
						ps.setString(1, xuenian);
						ps.setString(2, xueqi);
						ps.setString(3, nianji[y]);
						ps.setString(4, bumen[x]);
						ResultSet rs2 = ps.executeQuery();
						JSONArray jsonArray6 = new JSONArray(); //上级评分表
						JSONArray jsonArray4 = new JSONArray();
						while(rs2.next()) {
							JSONArray jsonArray5 = new JSONArray();
							double zongfen = rs2.getDouble(4);
							jsonArray5.add(rs2.getString(3));
							jsonArray5.add(zongfen);
							jsonArray6.add(jsonArray5);
						}
						for (int t = 0; t < jiaogongbiao.getJSONArray(y).size(); t++) {
							double grade = 0;
							int paiming = 1;
							for (int i = 0; i < jsonArray6.size(); i++) {
								if(jiaogongbiao.getJSONArray(y).getString(t).equals(jsonArray6.getJSONArray(i).getString(0))) {
									grade = jsonArray6.getJSONArray(i).getDouble(1);
								}
							}
							for (int i = 0; i < jsonArray6.size(); i++) {
								if(grade < jsonArray6.getJSONArray(i).getDouble(1)) {
									paiming ++;
								}
							}
							JSONArray jsonArray3 = new JSONArray();
							jsonArray3.add(jiaogongbiao.getJSONArray(y).getString(t));
							jsonArray3.add(grade);
							jsonArray3.add(paiming);
							jsonArray3.add(paiming/(jiaogongbiao.getJSONArray(y).size()+0.0));
							jsonArray4.add(jsonArray3);
						}
						geren.add(jsonArray4);
					}
				}else {
					for (int y = 0; y < 3; y++) {
						String sql3 = "SELECT * FROM GeRenLiangHuaBiao WHERE XueNian=? and XueQi=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao) ORDER BY DeFen desc";
						ps = conn.prepareStatement(sql3);
						ps.setString(1, xuenian);
						ps.setString(2, xueqi);
						ps.setString(3, nianji[y]);
						ps.setString(4, bumen[x]);
						ResultSet rs2 = ps.executeQuery();
						JSONArray jsonArray6 = new JSONArray(); //上级评分表
						JSONArray jsonArray4 = new JSONArray();
						while(rs2.next()) {
							JSONArray jsonArray5 = new JSONArray();
							double zongfen = rs2.getDouble(4);
							jsonArray5.add(rs2.getString(3));
							jsonArray5.add(zongfen);
							jsonArray6.add(jsonArray5);
						}
						for (int t = 0; t < jiaogongbiao.getJSONArray(6+y+(x-1)*3).size(); t++) {
							double grade = 0;
							int paiming = 1;
							for (int i = 0; i < jsonArray6.size(); i++) {
								if(jiaogongbiao.getJSONArray(6+y+(x-1)*3).getString(t).equals(jsonArray6.getJSONArray(i).getString(0))) {
									grade = jsonArray6.getJSONArray(i).getDouble(1);
								}
							}
							for (int i = 0; i < jsonArray6.size(); i++) {
								if(grade < jsonArray6.getJSONArray(i).getDouble(1)) {
									paiming ++;
								}
							}
							JSONArray jsonArray3 = new JSONArray();
							jsonArray3.add(jiaogongbiao.getJSONArray(6+y+(x-1)*3).getString(t));
							jsonArray3.add(grade);
							jsonArray3.add(paiming);
							jsonArray3.add(paiming/(jiaogongbiao.getJSONArray(6+y+(x-1)*3).size()+0.0));
							jsonArray4.add(jsonArray3);
						}
						geren.add(jsonArray4);
					}
				}
			}
			
			//计算总排名
			JSONArray zongdefen  = new JSONArray();
			for (int i = 0; i < 12; i++) {
				JSONArray jsonArray4 = new JSONArray();
				for (int j = 0; j < jiaogongbiao.getJSONArray(i).size(); j++) {
					JSONArray jsonArray2 = new JSONArray();
					String jiaogonghao = jiaogongbiao.getJSONArray(i).getString(j);
					double xs=0,sj=0,gr=0;
					int xspm=0,sjpm=0,grpm=0;
					double xsbfb=0,sjbfb=0,grbfb=0;
					for (int k = 0; k < jiaogongbiao.getJSONArray(i).size(); k++) {
						System.out.println(jiaogonghao+" "+xuesheng.getJSONArray(i).getJSONArray(k).getString(0));
						if(jiaogonghao.equals(xuesheng.getJSONArray(i).getJSONArray(k).getString(0))) {
							xs = xuesheng.getJSONArray(i).getJSONArray(k).getDouble(1);
							xspm = xuesheng.getJSONArray(i).getJSONArray(k).getInt(2);
							xsbfb = xuesheng.getJSONArray(i).getJSONArray(k).getDouble(3);
						}
						if(jiaogonghao.equals(shangji.getJSONArray(i).getJSONArray(k).getString(0))) {
							sj = shangji.getJSONArray(i).getJSONArray(k).getDouble(1);
							sjpm = shangji.getJSONArray(i).getJSONArray(k).getInt(2);
							sjbfb = shangji.getJSONArray(i).getJSONArray(k).getDouble(3);
						}
						if(jiaogonghao.equals(geren.getJSONArray(i).getJSONArray(k).getString(0))) {
							gr = geren.getJSONArray(i).getJSONArray(k).getDouble(1);
							grpm = geren.getJSONArray(i).getJSONArray(k).getInt(2);
							grbfb = geren.getJSONArray(i).getJSONArray(k).getDouble(3);
						}
					}
					double zpm = xs*0.4 + sj*0.1 + gr*0.5;
					jsonArray2.add(zpm);
					jsonArray2.add(xs);
					jsonArray2.add(xspm);
					jsonArray2.add(xsbfb);
					jsonArray2.add(sj);
					jsonArray2.add(sjpm);
					jsonArray2.add(sjbfb);
					jsonArray2.add(gr);
					jsonArray2.add(grpm);
					jsonArray2.add(grbfb);
					jsonArray2.add(jiaogonghao);
					jsonArray4.add(jsonArray2);
				}
				zongdefen.add(jsonArray4);
			}
			
			for (int i = 0; i < 12; i++) {
				for (int j = 0; j < jiaogongbiao.getJSONArray(i).size(); j++) {
					double defen = zongdefen.getJSONArray(i).getJSONArray(j).getDouble(0);
					int paiming = 1;
					for (int k = 0; k < jiaogongbiao.getJSONArray(i).size();k++) {
						if(defen < zongdefen.getJSONArray(i).getJSONArray(k).getDouble(0)) {
							paiming ++;
						}
					}
					zongdefen.getJSONArray(i).getJSONArray(j).add(1, paiming);
					zongdefen.getJSONArray(i).getJSONArray(j).add(2,paiming/(jiaogongbiao.getJSONArray(i).size()+0.0));
				}
			}
			jsonArray.add(jiaogongbiao);
			jsonArray.add(xuesheng);
			jsonArray.add(shangji);
			jsonArray.add(geren);
			jsonArray.add(zongdefen);
			
			//想排名表去写
//			String insert = "insert into JiaoGongPaiMingBiao values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//			for (int i = 0; i < 12; i++) {
//				for (int j = 0; j < zongdefen.getJSONArray(i).size(); j++) {
//					ps = conn.prepareStatement(insert);
//					ps.setString(1, xuenian);
//					ps.setString(2, xueqi);
//					ps.setString(3, zongdefen.getJSONArray(i).getJSONArray(j).getString(12));
//					ps.setDouble(4, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(3));
//					ps.setInt(5, zongdefen.getJSONArray(i).getJSONArray(j).getInt(4));
//					System.out.println(zongdefen.getJSONArray(i).getJSONArray(j).getDouble(5));
//					ps.setDouble(6, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(5));
//					ps.setDouble(7, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(6));
//					ps.setInt(8, zongdefen.getJSONArray(i).getJSONArray(j).getInt(7));
//					ps.setDouble(9, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(8));
//					ps.setDouble(10, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(9));
//					ps.setInt(11, zongdefen.getJSONArray(i).getJSONArray(j).getInt(10));
//					ps.setDouble(12, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(11));
//					ps.setDouble(13, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(0));
//					ps.setInt(14, zongdefen.getJSONArray(i).getJSONArray(j).getInt(1));
//					ps.setDouble(15, zongdefen.getJSONArray(i).getJSONArray(j).getDouble(2));
//					ps.execute();
//				}
//			}
			//System.out.println(jsonArray);
			response.getWriter().print(jsonArray);
			ps.close();
			conn.close();
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
