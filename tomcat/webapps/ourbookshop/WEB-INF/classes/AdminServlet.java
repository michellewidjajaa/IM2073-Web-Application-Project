// To save as "ebookshop/WEB-INF/classes/AdminServlet.java"
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null || 
            !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("login");
            return;
        }

        // Start HTML
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Admin - Add Books</title>");
        out.println("<link rel='stylesheet' href='css/styleAdmin.css'>");
        out.println("</head><body>");

        // Book Form
        out.println("<div class='admin-container'>");
        out.println("<h2>Add a New Book</h2>");
        out.println("<form action='admin' method='post'>");

        out.println("<div class='form-group'><label>ISBN:</label><input type='text' name='isbn' placeholder='Enter ISBN' required></div>");
        out.println("<div class='form-group'><label>Title:</label><input type='text' name='title' placeholder='Enter Book Title' required></div>");
        out.println("<div class='form-group'><label>Author:</label><input type='text' name='author' placeholder='Enter Author Name' required></div>");
        out.println("<div class='form-group'><label>Genre:</label><input type='text' name='genre' placeholder='Enter Book Genre' required></div>");
        out.println("<div class='form-group'><label>Price ($):</label><input type='number' step='0.01' name='price' placeholder='Enter Price' required></div>");
        out.println("<div class='form-group'><label>Stock:</label><input type='number' name='stock' placeholder='Enter Stock Quantity' required></div>");
        out.println("<div class='form-group'><label>Image URL:</label><input type='text' name='image_url' placeholder='Enter Image URL' required></div>");

        out.println("<button type='submit' class='add-book-btn'>Add Book</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO books (isbn, title, author, genre, price, stock, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)");) {

            stmt.setString(1, request.getParameter("isbn"));
            stmt.setString(2, request.getParameter("title"));
            stmt.setString(3, request.getParameter("author"));
            stmt.setString(4, request.getParameter("genre"));
            stmt.setDouble(5, Double.parseDouble(request.getParameter("price")));
            stmt.setInt(6, Integer.parseInt(request.getParameter("stock")));
            stmt.setString(7, request.getParameter("image_url")); // No need for length limit now

            stmt.executeUpdate();
            response.sendRedirect("thankyou-admin"); // Redirect after adding book

        } catch (SQLException ex) {
            out.println("<p>Error adding book: " + ex.getMessage() + "</p>");
        }
    }
}
