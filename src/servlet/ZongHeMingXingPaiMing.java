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
 * Servlet implementation class ZongHeMingXingPaiMing
 */
@WebServlet("/ZongHeMingXingPaiMing")
public class ZongHeMingXingPaiMing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZongHeMingXingPaiMing() {
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
			//获得该年级的教师
			String jiaogong = "SELECT JiaoGongHao,XingMing from  xueshengjiaogong WHERE NianJi=? and BuMen=? group by JiaoGongHao";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(jiaogong);
			ps.setString(1, nianji);
			ps.setString(2, bumen);
			ResultSet rs1 = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(rs1.next()) {
				JSONArray jsonArray2 = new JSONArray();
				jsonArray2.add(rs1.getString(1));
				jsonArray2.add(rs1.getString(2));
				jsonArray.add(jsonArray2);
			}
			System.out.println(jsonArray.size());
			//获得每个老师带的班级数或者学生数
			JSONArray xueshengshu = new JSONArray();
			if(leixing.equals("学生数")) {
				String shumu = "SELECT COUNT(JiaoGongHao) from XueShengYvJiaoGongDuiYingBiao WHERE JiaoGongHao=?";
				for (int i = 0; i < jsonArray.size(); i++,i++) {
					ps = conn.prepareStatement(shumu);
					ps.setString(1, jsonArray.getJSONArray(i).getString(0));
					ResultSet rs2 = ps.executeQuery();
					while(rs2.next()) {
						System.out.println(rs2.getInt(1)+"asdas");
						xueshengshu.add(rs2.getInt(1));
					}
				}
			}else {
				String shumu = "SELECT BanJi from xueshengjiaogong WHERE JiaoGongHao=? GROUP BY BanJi";
				for (int i = 0; i < jsonArray.size(); i++,i++) {
					ps = conn.prepareStatement(shumu);
					ps.setString(1, jsonArray.getJSONArray(i).getString(0));
					ResultSet rs2 = ps.executeQuery();
					int count = 0;
					while(rs2.next()) {
						count ++;
					}
					System.out.println(count);
					xueshengshu.add(count);
				}
			}
			JSONArray jsonArray4 = new JSONArray();
			for (int l = 1; l <= 10; l++) {
				JSONArray jsonArray3 = new JSONArray();
				String sql = "select XueNian,XueQi,JiaoGongHao,XingMing,XiangMuBianHao,XiangMuNeiRong,COUNT(XiangMuBianHao) as 'RenShu' from jiaogongduangerenmingxingxinxi where XueNian=? and XueQi=? and XiangMuBianHao=? and JiaoGongHao in(SELECT JiaoGongHao FROM xueshengjiaogong WHERE NianJi=? and BuMen=? GROUP BY JiaoGongHao)   GROUP BY XueNian,XueQi,JiaoGongHao,XiangMuBianHao,XiangMuNeiRong ORDER BY JiaoGongHao asc";
				ps = conn.prepareStatement(sql);
				ps.setString(1, xuenian);
				ps.setString(2, xueqi);
				ps.setInt(3, l);
				ps.setString(4, nianji);
				ps.setString(5, bumen);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					JSONArray jsonArray2 = new JSONArray();
					for(int j = 3 ; j <= 6 ; j++) {
						jsonArray2.add(rs.getString(j));
					}
					int mark=0;
					for (int i = 0; i < jsonArray.size(); i++) {
						if(jsonArray2.getString(0) == jsonArray.getJSONArray(i).getString(0)) {
							mark = i;
							break;
						}
					}
					int defen = rs.getInt(7); //该老师在该项的投票人数
					double tupiaolv = defen / (xueshengshu.getInt(mark)+0.0) ;
					jsonArray2.add(tupiaolv);
					jsonArray3.add(jsonArray2);
				}
				
//				//获得每个人的排名，次排名可以解决同分排名相同
//				for (int i = 0; i < jsonArray.size(); i++) {
//					int count = 1;
//					double grade = 0;
//					int mark = 0;
//					for (int j = 0; j < jsonArray3.size(); j++) {
//						if(jsonArray.getJSONArray(i).getString(0).equals(jsonArray3.getJSONArray(j).getString(0))) {
//							grade = jsonArray3.getJSONArray(j).getDouble(4);
//							mark = j;
//						}
//					}
//					//grade=0接该教师没有票，从数据库搜索不到该老师，故手动加上
//					if(grade == 0) {
//						JSONArray jArray = new JSONArray();
//						jArray.add(jsonArray.getJSONArray(i).getString(0));
//						jArray.add(jsonArray.getJSONArray(i).getString(1));
//						jArray.add(l+"");
//						jArray.add(jsonArray3.getJSONArray(0).getString(3));
//						jArray.add(0);
//						jArray.add(jsonArray.size());
//						jsonArray3.add(jArray);	
//					}else {
//						for (int j = 0; j < jsonArray3.size(); j++) {
//							if(grade < jsonArray3.getJSONArray(j).getDouble(4)) {
//								count ++;
//							}
//						}
//						jsonArray3.getJSONArray(mark).add(count);
//					}
//				}
				
//				//但实际只需要从大到小显示投票数即可，不需排名，故直接排序，然后加上0票教师即可。
//				for (int i = 0; i < jsonArray3.size()-1; i++) {
//					for (int j = 0; j < jsonArray3.size()-1-i; j++) {
						//	有排名，按排名排序，小的在前
//						if(jsonArray3.getJSONArray(j).getInt(5) > jsonArray3.getJSONArray(j+1).getInt(5)) {
//							JSONArray jsonArray5 = jsonArray3.getJSONArray(j);
//							jsonArray3.remove(j);//删除j位，j+1成为j位
//							jsonArray3.add(j+1, jsonArray5);
//						}
//					}
//				}
				
				//但实际只需要从大到小显示投票数即可，不需排名，故直接排序，然后加上0票教师即可。
				for (int i = 0; i < jsonArray3.size()-1; i++) {
					for (int j = 0; j < jsonArray3.size()-1-i; j++) {
						//没有排名，故按投票数排序,大的在前
						if(jsonArray3.getJSONArray(j).getInt(4) < jsonArray3.getJSONArray(j+1).getInt(4)) {
							JSONArray jsonArray5 = jsonArray3.getJSONArray(j);
							jsonArray3.remove(j);//删除j位，j+1成为j位
							jsonArray3.add(j+1, jsonArray5);
						}
					}
				}
				for (int i = 0; i < jsonArray.size(); i++) {
					double grade = 0;
					for (int j = 0; j < jsonArray3.size(); j++) {
						if(jsonArray.getJSONArray(i).getString(0).equals(jsonArray3.getJSONArray(j).getString(0))) {
							grade = jsonArray3.getJSONArray(j).getDouble(4);
						}
					}
					//grade=0接该教师没有票，从数据库搜索不到该老师，故手动加上
					if(grade == 0) {
						JSONArray jArray = new JSONArray();
						jArray.add(jsonArray.getJSONArray(i).getString(0));
						jArray.add(jsonArray.getJSONArray(i).getString(1));
						jArray.add(l+"");
						jArray.add(jsonArray3.getJSONArray(0).getString(3));
						jArray.add(0);
						jsonArray3.add(jArray);	
					}
				}
				jsonArray4.add(jsonArray3);
			}
			
			response.getWriter().print(jsonArray4);
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
