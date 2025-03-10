// To save as "ebookshop/WEB-INF/classes/AddToCartServlet.java"
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      HttpSession session = request.getSession(false);
      if (session == null || session.getAttribute("username") == null) {
         response.sendRedirect("login.html");
         return;
      }

      int userId = (int) session.getAttribute("user_id");
      int bookId = Integer.parseInt(request.getParameter("book_id"));

      try (Connection conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
           PreparedStatement checkStmt = conn.prepareStatement(
                   "SELECT quantity FROM cart WHERE user_id = ? AND book_id = ?");
           PreparedStatement updateStmt = conn.prepareStatement(
                   "UPDATE cart SET quantity = quantity + 1 WHERE user_id = ? AND book_id = ?");
           PreparedStatement insertStmt = conn.prepareStatement(
                   "INSERT INTO cart (user_id, book_id, quantity) VALUES (?, ?, 1)")) {

          // Check if book is already in the cart
          checkStmt.setInt(1, userId);
          checkStmt.setInt(2, bookId);
          ResultSet rs = checkStmt.executeQuery();

          if (rs.next()) {
              // Book exists in cart, update quantity
              updateStmt.setInt(1, userId);
              updateStmt.setInt(2, bookId);
              updateStmt.executeUpdate();
          } else {
              // Book not in cart, insert new entry
              insertStmt.setInt(1, userId);
              insertStmt.setInt(2, bookId);
              insertStmt.executeUpdate();
          }

      } catch (SQLException ex) {
          ex.printStackTrace();
      }

      response.sendRedirect("cart"); // Redirect back to cart page
   }
}
