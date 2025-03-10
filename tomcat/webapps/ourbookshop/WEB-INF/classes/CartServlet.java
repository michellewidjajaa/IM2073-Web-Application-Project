import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      HttpSession session = request.getSession(false);
      if (session == null || session.getAttribute("user_id") == null) {
         response.sendRedirect("login.html");
         return;
      }

      int userId = (int) session.getAttribute("user_id");
      String userAddress = "";
      String username = (String) session.getAttribute("username");

      // Get user shipping address from the database
      try (Connection conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
           PreparedStatement stmt = conn.prepareStatement(
                   "SELECT address FROM users WHERE user_id = ?")) {

         stmt.setInt(1, userId);
         ResultSet rs = stmt.executeQuery();
         if (rs.next()) {
            userAddress = rs.getString("address");
         }
      } catch (SQLException ex) {
         out.println("<p>Error retrieving user address.</p>");
      }

      out.println("<!DOCTYPE html>");
      out.println("<html><head><title>Cart - Our Bookshop</title>");
      out.println("<link rel='stylesheet' href='css/styleCart.css'>");
      out.println("</head><body>");

      out.println("<div class='header'>");
      out.println("<div class='logo'><img src='images/mainlogo.png' alt='Logo'><h1>Our Bookshop</h1></div>");

      out.println("<div class='search-bar'>");
      out.println("<form action='search' method='get' class='search-form'>");
      out.println("<input type='text' name='query' placeholder='Search books or author...' required>");
      out.println("<button type='submit' class='submit-btn'>🔍</button>");
      out.println("</form>");
      out.println("</div>");

      
      out.println("<div class='cart-user'>");
      out.println("<a href='cart' class='cart-btn'>🛒 Cart</a>");
      out.println("<a href='user' class='user-btn'>" + username + "</a>");
      out.println("</div>");
      out.println("</div>");

      out.println("<div class='cart-container'>");
      out.println("<h2>Cart</h2>");
      out.println("<p>Check your cart items before proceeding to payment.</p>");

      boolean hasItems = false;
      double totalPrice = 0.0;

      try (Connection conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
           PreparedStatement stmt = conn.prepareStatement(
                   "SELECT b.book_id, b.image_url, b.title, b.author, b.price, c.quantity " +
                   "FROM cart c JOIN books b ON c.book_id = b.book_id WHERE c.user_id = ?")) {

         stmt.setInt(1, userId);
         ResultSet rs = stmt.executeQuery();

         out.println("<div class='cart-content'>");
         out.println("<div class='cart-items'>");

         while (rs.next()) {
            hasItems = true;
            int bookId = rs.getInt("book_id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            double price = rs.getDouble("price");
            int quantity = rs.getInt("quantity");
            double bookTotal = price * quantity;
            totalPrice += bookTotal;

            out.println("<div class='cart-item'>");
            out.println("<img src='" + rs.getString("image_url") + "' alt='" + title + "'>");
            out.println("<div class='cart-details'>");
            out.println("<div class='book-title'>" + title + "</div>");
            out.println("<div class='book-author'>" + author + "</div>");
            out.println("<div class='book-price'>$" + String.format("%.2f", price) + "</div>");
            out.println("<div class='quantity-control'>");
            out.println("<form action='update-cart' method='post'>");
            out.println("<input type='hidden' name='book_id' value='" + bookId + "'>");
            out.println("<button type='submit' name='action' value='decrease' class='quantity-btn'>-</button>");
            out.println("<span class='quantity'>" + quantity + "</span>");
            out.println("<button type='submit' name='action' value='increase' class='quantity-btn'>+</button>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
         }

         out.println("</div>");


      /*   if (!hasItems) {
            out.println("<div class='cart-header'>");
            out.println("<p class='empty-cart-msg'>Your cart is empty.</p>");
            out.println("</div>");
         } */

         out.println("<div class='cart-summary'>");
         out.println("<h3>Summary</h3>");
         if (hasItems) {
            out.println("<p>Total Books Price: $" + String.format("%.2f", totalPrice) + "</p>");
            out.println("<p>Shipping Cost: $2.00</p>");
            out.println("<h4>Total: $" + String.format("%.2f", totalPrice + 2) + "</h4>");
            out.println("<h3>Shipping Address</h3>");
            out.println("<p>" + userAddress + "</p>");
            out.println("<img src='images/qrcode.png' alt='QR Code' class='qr-code'>");
            out.println("<form action='thankyou' method='post'>");
            out.println("<button type='submit' class='pay-button'>Pay</button>");
            out.println("</form>");
         } else {
            out.println("<p>Total: $0.00</p>");
         }
         out.println("</div>");

         out.println("</div>");

         out.println("<div class='back-shopping-container'>");
         out.println("<form action='home' method='get'>");
         out.println("<button type='submit' class='back-shopping-btn'>Back to Shopping</button>");
         out.println("</form>");
         out.println("</div>");

         out.println("</div>");

      } catch (SQLException ex) {
         out.println("<p>Error retrieving cart items.</p>");
      }

      out.println("</body></html>");
   }
}