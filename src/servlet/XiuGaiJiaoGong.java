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
import javax.swing.text.AbstractDocument.BranchElement;

import com.sun.swing.internal.plaf.synth.resources.synth_pt_BR;

import javafx.css.PseudoClass;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class XiuGaiJiaoGong
 */
@WebServlet("/XiuGaiJiaoGong")
public class XiuGaiJiaoGong extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public XiuGaiJiaoGong() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method 
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		boolean empty = false;
		String[] info = new String[23];
		String leixing = request.getParameter("leixing");
		if(leixing.equals("baocun")) {
			for (int i = 1; i <= 23; i++) {
				info[i-1] = request.getParameter(i+"");
			}
		}else {
			for (int i = 1; i <= 23; i++) {
				info[i-1] = request.getParameter(i+"");
				if(info[i-1] == "" || info[i-1]==null) {
					empty = true;
					response.getWriter().print("请填全选项"+i);
					break;
				}
			}	
		}
		if(!empty) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
				String username = "root";
				String Password = "zb301522";
				Connection conn = DriverManager.getConnection(url, username, Password);
				Statement statement = conn.createStatement();
				String delete = "delete from JiaoGongBiao where JiaoGongHao=" + info[0];
				System.out.println(delete);
				int count = statement.executeUpdate(delete);
				if(count == 1) {
					String add = "insert into JiaoGongBiao values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = null;
					ps = conn.prepareStatement(add);
					for (int i = 1; i <= 23; i++) {
						ps.setString(i, info[i-1]);
					}
					if(leixing.equals("baocun")) {
						ps.setBoolean(24, false);
					}else {
						ps.setBoolean(24, true);
					}
					count = ps.executeUpdate();
					if(count == 1) {
						response.getWriter().print("修改成功");
					}else {
						response.getWriter().print("修改失败");
					}
					ps.close();
				}else {
					response.getWriter().print("修改失败");
				}
				statement.close();
				conn.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
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
