package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.PortableInterceptor.RequestInfoOperations;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		String leixing = request.getParameter("leixing");
		String yanzhengma = request.getParameter("yanzhengma");
		if(leixing.equals("XueShengBiao")) {
			name = "XueHao=" + name;
		}else if(leixing.equals("JiaoGongBiao")) {
			name = "JiaoGongHao=" + name;
		}else if(leixing.equals("PeiZhiBiao")) {
			name = "GuanLiYuanZhangHao="+name;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			Statement stmt = conn.createStatement();
			String sql = "select * from "+leixing + " where "+name+" and MiMa="+password;
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			request.getSession().setAttribute(leixing,name);
			if(rs.next()) {
				if(yanzhengma.equalsIgnoreCase((String) request.getSession().getAttribute("YanZhengMa"))) {
//					  Cookie cookiename = new Cookie("username", name);
//					  Cookie cookiepassword = new Cookie("password", password);
//					  cookiename.setMaxAge(864000);
//					  cookiepassword.setMaxAge(86400);
//					  response.addCookie(cookiename);
//					  response.addCookie(cookiepassword);
					  response.getWriter().print("true");
				}else {
				      response.getWriter().print("验证码错误");
				}
			}else {
				System.out.println("asda");
				response.getWriter().print("用户名或者密码错误");
			}
			rs.close();
			stmt.close();
			conn.close();
		}catch(Exception e) {
			
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
