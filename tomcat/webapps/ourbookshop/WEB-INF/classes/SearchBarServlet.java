import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

    @WebServlet("/search")
    public class SearchBarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve session username
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");
        
        // Get the search query
        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            response.sendRedirect("home"); // Redirect to home if empty query
            return;
        }

        // Start HTML
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Search Results - Our Bookshop</title>");
        out.println("<link rel='stylesheet' href='css/styleSearch.css'>");
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

        // === SEARCH RESULTS ===
        out.println("<h2 class='search-title'>Search Results for '" + query + "'</h2>");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?")) {

            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();

            out.println("<div class='book-list'>");
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
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
            
            if (!hasResults) {
                out.println("<p>No books found for '<b>" + query + "</b>'. Try searching something else!</p>");
            }
        } catch (SQLException ex) {
            out.println("<p>Error retrieving search results.</p>");
        }
        
        out.println("</body></html>");
    }
    }