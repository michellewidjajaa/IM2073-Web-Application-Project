// To save as "ebookshop/WEB-INF/classes/ThankyouServlet.java"
import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/thankyou")
public class ThankyouServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ourbookshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "myuser";
    private static final String DB_PASSWORD = "xxxx";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get user session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = (int) session.getAttribute("user_id");
        String shippingAddress = "";
        String username = (String) session.getAttribute("username");

        int orderId = -1;
        double totalPrice = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Fetch user's shipping address
            try (PreparedStatement addrStmt = conn.prepareStatement("SELECT address FROM users WHERE user_id = ?")) {
                addrStmt.setInt(1, userId);
                ResultSet addrRs = addrStmt.executeQuery();
                if (addrRs.next()) {
                    shippingAddress = addrRs.getString("address");
                }
            }

            // Calculate total price from cart
            try (PreparedStatement totalStmt = conn.prepareStatement(
                    "SELECT SUM(b.price * c.quantity) FROM cart c JOIN books b ON c.book_id = b.book_id WHERE c.user_id = ?")) {
                totalStmt.setInt(1, userId);
                ResultSet totalRs = totalStmt.executeQuery();
                if (totalRs.next()) {
                    totalPrice = totalRs.getDouble(1) + 2.00; // Adding flat shipping cost
                }
            }

            // Insert order into orders table
            String insertOrderSQL = "INSERT INTO orders (user_id, total_price, shipping_address, payment_status) VALUES (?, ?, ?, 'completed')";
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalPrice);
                orderStmt.setString(3, shippingAddress);
                orderStmt.executeUpdate();

                // Get generated order_id
                ResultSet orderRs = orderStmt.getGeneratedKeys();
                if (orderRs.next()) {
                    orderId = orderRs.getInt(1);
                }
            }

            // Insert cart items into order_items table
            String insertOrderItemsSQL = "INSERT INTO order_items (order_id, book_id, quantity, price) SELECT ?, book_id, quantity, price FROM cart JOIN books USING(book_id) WHERE user_id = ?";
            try (PreparedStatement orderItemStmt = conn.prepareStatement(insertOrderItemsSQL)) {
                orderItemStmt.setInt(1, orderId);
                orderItemStmt.setInt(2, userId);
                orderItemStmt.executeUpdate();
            }

            // Clear the cart after order placement
            try (PreparedStatement clearCartStmt = conn.prepareStatement("DELETE FROM cart WHERE user_id = ?")) {
                clearCartStmt.setInt(1, userId);
                clearCartStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error processing order: " + e.getMessage() + "</p>");
            return;
        }

        // Start HTML Response
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Thank You - Our Bookshop</title>");
        out.println("<link rel='stylesheet' href='css/styleThankyou.css'>");
        out.println("</head><body>");

        // === HEADER (Updated with Cart Button) ===
        out.println("<div class='header'>");
        out.println("<div class='logo'><img src='images/mainlogo.png' alt='Logo'><h1>Our Bookshop</h1></div>");

        // ✅ Proper Search Bar (Fixed Width & Correct Button)
        out.println("<div class='search-bar'>");
        out.println("<form action='search' method='get' class='search-form'>");
        out.println("<input type='text' name='query' placeholder='Search books or author...' required>");
        out.println("<button type='submit' class='submit-btn'>🔍</button>");
        out.println("</form>");
        out.println("</div>");

        
        out.println("<div class='cart-user'>");
        // 🛒 Cart button (Redirect to CartServlet)
        out.println("<a href='cart' class='cart-btn'>🛒 Cart</a>");
        out.println("<a href='user' class='user-btn'>" + username + "</a>");
        out.println("</div>");
        out.println("</div>");

        // Thank You Message
        out.println("<div class='thankyou-container'>");
        out.println("<h1>Thank You for Purchasing!</h1>");
        out.println("<p>Your purchase is being processed.</p>");
        out.println("<p class='order-number'>Order Number: #" + orderId + "</p>");
        out.println("</div>");

        // BACK TO SHOPPING BUTTON
         out.println("<div class='back-shopping-container'>");
         out.println("<form action='home' method='get'>");
         out.println("<button type='submit' class='back-shopping-btn'>Back to Shopping</button>");
         out.println("</form>");
         out.println("</div>");

        out.println("</body></html>");
    }
}
