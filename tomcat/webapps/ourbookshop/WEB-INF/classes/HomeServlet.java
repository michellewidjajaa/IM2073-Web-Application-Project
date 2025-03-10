// To save as "ebookshop/WEB-INF/classes/HomeServlet.java"
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/home") 
public class HomeServlet extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      // Retrieve session username
      HttpSession session = request.getSession(false);
      String username = (String) session.getAttribute("username");

      // Start HTML
      out.println("<!DOCTYPE html>");
      out.println("<html><head><title>Home - Our Bookshop</title>");
      out.println("<link rel='stylesheet' href='css/styleHome.css'>");
      out.println("</head><body>");

      // === HEADER (Updated with Cart Button) ===
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

      // Slider and Small Images
      out.println("<div class='slider-container'>");
      out.println("<div class='slider'><div class='slides'>");
      out.println("<img src='images/disc1.png'><img src='images/disc2.png'><img src='images/disc3.png'>");
      out.println("</div></div>");
      out.println("<div class='small-images'><img src='images/promo1.png'><img src='images/promo2.png'></div>");
      out.println("</div>");

      // Genre Buttons
      out.println("<div class='genre-container'>");
      String[] genres = {"Romance", "Thriller", "Fantasy", "Mystery", "Horror", "Fiction"};
      for (String genre : genres) {
         out.println("<form action='genre' method='get'>");
         out.println("<input type='hidden' name='name' value='" + genre + "'>");
         out.println("<button class='genre-button' type='submit'><img src='images/" + genre.toLowerCase() + ".png' alt='" + genre + "'>" + genre + "</button>");
         out.println("</form>");
      }
      out.println("</div>");

      // Book List
      try (Connection conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

          out.println("<div class='book-list'>");
          while (rs.next()) {
              int bookId = rs.getInt("book_id");
              out.println("<div class='book-item'>");
              out.println("<img src='" + rs.getString("image_url") + "' alt='" + rs.getString("title") + "'>");
              out.println("<div class='book-title'>" + rs.getString("title") + "</div>");
              out.println("<div class='book-author'>" + rs.getString("author") + "</div>");
              out.println("<div class='book-price'>$" + rs.getDouble("price") + "</div>");
              out.println("<form action='add-to-cart' method='post'>");
              out.println("<input type='hidden' name='book_id' value=" + bookId + ">");
              out.println("<button class='add-to-cart' type='submit'>Add to Cart</button>");
              out.println("</form>");
              out.println("</div>");

          }
          out.println("</div>");
      } catch (SQLException ex) {
          out.println("<p>Error retrieving books.</p>");
      }

      out.println("</body></html>");
   }
}