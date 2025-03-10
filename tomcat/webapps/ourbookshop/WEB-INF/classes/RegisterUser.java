// To save as "ebookshop\WEB-INF\classes\RegisterUser.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;            
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/register")   
public class RegisterUser extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/ourbookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "myuser";
    private static final String PASSWORD = "xxxx";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password"); // Hash before storing
        String birthdate = request.getParameter("birthdate");
        String address = request.getParameter("address");

        if (username == null || name == null || email == null || password == null || birthdate == null || address == null) {
            out.println("<script>alert('Please fill in all fields.'); window.location='register.html';</script>");
            return;
        }

        String insertQuery = "INSERT INTO users (username, name, email, password_hash, birthdate, address, role) VALUES (?, ?, ?, ?, ?, ?, 'user')";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setDate(5, java.sql.Date.valueOf(birthdate));
            pstmt.setString(6, address);

            System.out.println("Executing query: " + pstmt);  // Debugging

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("login.html");
            } else {
                out.println("<script>alert('Registration failed. Try again.'); window.location='register.html';</script>");
            }
        } catch (SQLException e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        out.close();
    }
}