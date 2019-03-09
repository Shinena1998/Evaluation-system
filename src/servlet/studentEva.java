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

/**
 * Servlet implementation class studentEva
 */
@WebServlet("/studentEva")
public class studentEva extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public studentEva() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");

		System.out.println("adasd");
		response.setContentType("text/html;charset=utf-8");
		String teacher = request.getParameter("teacher");
		String loaction = request.getParameter("loaction");
		String student = request.getParameter("student");
		String c = request.getParameter("evaLength");
		int count = Integer.parseInt(c);
		int i = 0;
		String contant="",grade="";
		while(i<count) {
			contant = contant +  request.getParameter("contant"+i) +"+";
			grade = grade + request.getParameter("grade"+i)+"+";
			i++;
		}
		System.out.println(contant +" "+grade+"loaction"+loaction);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/evaluate?useSSL=false&serverTimezone=UTC";
			String username = "root";
			String Password = "zb301522";
			Connection conn = DriverManager.getConnection(url, username, Password);
			Statement statement = conn.createStatement();
			String sql1 = "select * from teacherEva where student="+student+" and teacher="+teacher;
			ResultSet rSet = statement.executeQuery(sql1);
			if(rSet.next()) {
				//注意这里不能用println不然传过去字符串会加换行符
				response.getWriter().print("已经评教，请勿再次评教");
			}else {
				String sql = "insert into teacherEva values(?,?,?,?,?)";
				PreparedStatement ps = null;
				ps = conn.prepareStatement(sql);
				ps.setString(1, contant);
				ps.setString(2, teacher);
				ps.setString(3, grade);
				ps.setString(4, loaction);
				ps.setString(5, student);
				ps.execute();
				//System.out.println(jsonArray);
				response.getWriter().print(grade);
			}
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
