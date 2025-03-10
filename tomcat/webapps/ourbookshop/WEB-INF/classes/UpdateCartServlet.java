import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/update-cart")
public class UpdateCartServlet extends HttpServlet {
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      HttpSession session = request.getSession(false);
      if (session == null || session.getAttribute("user_id") == null) {
         response.sendRedirect("login");
         return;
      }

      int userId = (int) session.getAttribute("user_id");
      int bookId = Integer.parseInt(request.getParameter("book_id"));
      String action = request.getParameter("action"); // "increase" or "decrease"

      try (Connection conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx")) {

         if ("increase".equals(action)) {
            // Increase quantity
            String updateQuery = "UPDATE cart SET quantity = quantity + 1 WHERE user_id = ? AND book_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
               pstmt.setInt(1, userId);
               pstmt.setInt(2, bookId);
               pstmt.executeUpdate();
            }
         } else if ("decrease".equals(action)) {
            // Decrease quantity
            String checkQuery = "SELECT quantity FROM cart WHERE user_id = ? AND book_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
               checkStmt.setInt(1, userId);
               checkStmt.setInt(2, bookId);
               ResultSet rs = checkStmt.executeQuery();
               if (rs.next()) {
                  int quantity = rs.getInt("quantity");
                  if (quantity > 1) {
                     String updateQuery = "UPDATE cart SET quantity = quantity - 1 WHERE user_id = ? AND book_id = ?";
                     try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, userId);
                        updateStmt.setInt(2, bookId);
                        updateStmt.executeUpdate();
                     }
                  } else {
                     // Remove book from cart if quantity is 0
                     String deleteQuery = "DELETE FROM cart WHERE user_id = ? AND book_id = ?";
                     try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, userId);
                        deleteStmt.setInt(2, bookId);
                        deleteStmt.executeUpdate();
                     }
                  }
               }
            }
         }

         // Redirect back to the cart
         response.sendRedirect("cart");

      } catch (SQLException ex) {
         out.println("<p>Database error: " + ex.getMessage() + "</p>");
      }
   }
}