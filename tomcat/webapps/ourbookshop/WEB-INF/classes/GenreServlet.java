import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/genre") 
public class GenreServlet extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      // Get selected genre
      String genre = request.getParameter("name");
      if (genre == null || genre.trim().isEmpty()) {
         genre = "All Books";
      }

      // Retrieve session username
      HttpSession session = request.getSession(false); 
      Integer userId = (Integer) session.getAttribute("user_id");
      String username = (String) session.getAttribute("username");

      // Start HTML
      out.println("<!DOCTYPE html>");
      out.println("<html><head><title>" + genre + " Books - Our Bookshop</title>");
      out.println("<link rel='stylesheet' href='css/styleGenre.css'>");
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

      // Genre Title
      out.println("<h2 class='genre-title'>" + genre + " Books</h2>");

      // Book List for the Selected Genre
      try (Connection conn = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
           PreparedStatement stmt = conn.prepareStatement("SELECT * FROM books WHERE genre = ?")) {

          stmt.setString(1, genre);
          ResultSet rs = stmt.executeQuery();

          out.println("<div class='book-list'>");
          while (rs.next()) {
              int bookId = rs.getInt("book_id");
              out.println("<div class='book-item'>");
              out.println("<img src='" + rs.getString("image_url") + "' alt='" + rs.getString("title") + "'>");
              out.println("<div class='book-title'>" + rs.getString("title") + "</div>");
              out.println("<div class='book-author'>" + rs.getString("author") + "</div>");
              out.println("<div class='book-price'>$" + rs.getDouble("price") + "</div>");
              out.println("<form action='add-to-cart' method='post'>");
              out.println("<input type='hidden' name='book_id' value='" + bookId + "'>");
              out.println("<button type='submit' class='add-to-cart'>Add to Cart</button>");
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