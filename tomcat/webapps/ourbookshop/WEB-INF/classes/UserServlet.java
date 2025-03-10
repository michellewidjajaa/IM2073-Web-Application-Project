import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
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
        String username = (String) session.getAttribute("username");

        // User details
        String name = "", email = "", birthdate = "", address = "", passwordHash = "********";

        // Retrieve user details from the database
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
             PreparedStatement stmt = conn.prepareStatement("SELECT name, email, password_hash, birthdate, address FROM users WHERE user_id = ?")) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                name = rs.getString("name");
                email = rs.getString("email");
                birthdate = rs.getString("birthdate");
                address = rs.getString("address");
                passwordHash = rs.getString("password_hash");
            }
        } catch (SQLException ex) {
            out.println("<p>Error retrieving user details.</p>");
        }

        // Start HTML
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>User Profile - Our Bookshop</title>");
        out.println("<link rel='stylesheet' href='css/styleUser.css'>");
        out.println("<script>");
        out.println("function togglePassword() {");
        out.println("  var passwordField = document.getElementById('passwordField');");
        out.println("  if (passwordField.type === 'password') {");
        out.println("    passwordField.type = 'text';");
        out.println("  } else {");
        out.println("    passwordField.type = 'password';");
        out.println("  }");
        out.println("}");
        out.println("</script>");
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

        out.println("<div class='user-container'>");
        out.println("<h2>User Profile</h2>");

        out.println("<div class='profile-info'>");
        out.println("<label><strong>Name:</strong></label><span class='profile-data'>" + name + "</span>");
        out.println("<label><strong>Email:</strong></label><span class='profile-data'>" + email + "</span>");
        out.println("<label><strong>Birthdate:</strong></label><span class='profile-data'>" + birthdate + "</span>");
        out.println("<label><strong>Address:</strong></label><span class='profile-data'>" + address + "</span>");

        out.println("<label><strong>Password:</strong></label>");
        out.println("<div class='password-field'>");
        out.println("<input type='password' value='" + passwordHash + "' id='passwordField' readonly>");
        out.println("<input type='checkbox' id='showPassword' onclick='togglePassword()'> <label for='showPassword'>Show Password</label>");
        out.println("</div>");

        out.println("</div>"); 

        out.println("<div class='button-container'>");
        out.println("<form action='home' method='get'>");
        out.println("<button type='submit' class='home-btn'>Back to Home</button>");
        out.println("</form>");
        out.println("</div>");

        // === User Orders Section ===
        out.println("<h2>Order History</h2>");
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ourbookshop", "myuser", "xxxx");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT o.order_id, o.total_price, o.order_date, oi.book_id, b.title, oi.quantity " +
                     "FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                     "JOIN books b ON oi.book_id = b.book_id WHERE o.user_id = ? ORDER BY o.order_date DESC")) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            int currentOrderId = -1;
            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                double totalPrice = rs.getDouble("total_price");
                String orderDate = rs.getString("order_date");
                String bookTitle = rs.getString("title");
                int quantity = rs.getInt("quantity");

                if (orderId != currentOrderId) {
                    if (currentOrderId != -1) {
                        out.println("</div>"); // Close previous order
                    }
                    out.println("<div class='order-box'>");
                    out.println("<h3>Order #" + orderId + "</h3>");
                    out.println("<p><strong>Order Date:</strong> " + orderDate + "</p>");
                    out.println("<p><strong>Total Purchase:</strong> $" + String.format("%.2f", totalPrice) + "</p>");
                    out.println("<h4>Ordered Books:</h4>");
                    currentOrderId = orderId;
                }
                out.println("<p>" + bookTitle + " - Quantity: " + quantity + "</p>");
            }
            if (currentOrderId != -1) {
                out.println("</div>"); // Close last order
            }
        } catch (SQLException ex) {
            out.println("<p>Error retrieving orders.</p>");
        }

        // Logout Button at the Bottom
        out.println("<div class='logout-container'>");
        out.println("<form action='logout' method='post'>");
        out.println("<button type='submit' class='logout-btn'>Logout</button>");
        out.println("</form>");
        out.println("</div>");

        out.println("</div>"); // Close user-container
        out.println("</body></html>");
    }
}
