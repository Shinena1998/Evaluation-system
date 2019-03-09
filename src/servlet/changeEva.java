package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class changeEva
 */
@WebServlet("/changeEva")
public class changeEva extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public changeEva() {
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
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String c = request.getParameter("count");
		System.out.println(start);
		int count = Integer.parseInt(c);
		int i = 0;
		int j = 0;
		String contant="",grade="";
		while(i<count) {
			if(request.getParameter("contant"+j) != null) {
				contant = contant +  request.getParameter("contant"+j) +"+";
				grade = grade + request.getParameter("grade"+j)+"+";
				i++;
			}
			j++;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			String sql = "insert into assess values(?,?,?,?,?)";
			PreparedStatement ps = null;
			ps = conn.prepareStatement(sql);
			ps.setString(1, contant);
			ps.setString(2, grade);
			ps.setString(3, start);
			ps.setString(4, end);
			ps.setBoolean(5,true);
			ps.execute();
			//System.out.println(jsonArray);
			response.getWriter().print("修改成功");
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
