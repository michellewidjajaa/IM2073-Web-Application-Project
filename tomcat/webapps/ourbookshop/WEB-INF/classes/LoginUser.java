// To save as "ebookshop/WEB-INF/classes/LoginUser.java"
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/login") 
public class LoginUser extends HttpServlet {

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      // Get parameters from login form
      String username = request.getParameter("username");
      String password = request.getParameter("password");

      if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
         out.println("<p>Error: Missing username or password. <a href='login.html'>Try again</a></p>");
         return;
      }

      // Database connection details
      String dbURL = "jdbc:mysql://localhost:3306/ourbookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
      String dbUser = "myuser";
      String dbPassword = "xxxx";

      try {
         Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC Driver
         try (
            Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? and password_hash=?");
         ) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
               // Username not found
               out.println("<p>Invalid username. <a href='login.html'>Try again</a></p>");
               return;
            }

            // Get stored password hash
            String storedHash = rs.getString("password_hash");
            String role = rs.getString("role");

            // Compare password
            if (password.equals(storedHash)) {  // ⚠️ Replace with hashing check if passwords are hashed
                HttpSession session = request.getSession();
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("user_id", rs.getInt("user_id"));
                session.setAttribute("role", role);
                
                if ("admin".equals(role)) {
                    response.sendRedirect("admin"); // Redirect to admin page
                } else {
                    response.sendRedirect("home"); // Redirect to home page
                }
            } else {
                out.println("<p>Invalid password. <a href='login.html'>Try again</a></p>");
            }
         }
      } catch (ClassNotFoundException ex) {
         out.println("<p>Database Driver Error: " + ex.getMessage() + "</p>");
      } catch (SQLException ex) {
         out.println("<p>Database Error: " + ex.getMessage() + "</p>");
         ex.printStackTrace();
      }
   }
}
