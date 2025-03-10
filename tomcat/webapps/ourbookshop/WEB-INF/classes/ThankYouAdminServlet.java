// To save as "ebookshop/WEB-INF/classes/ThankYouAdminServlet.java"
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/thankyou-admin")
public class ThankYouAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Thank You - Admin</title>");
        out.println("<link rel='stylesheet' href='css/styleThankYouAdmin.css'>");
        out.println("</head><body>");

        out.println("<div class='thankyou-container'>");
        out.println("<h1>Thank You!</h1>");
        out.println("<p>The book has been successfully added to the database.</p>");

        // Logout Button (Redirects to LogoutServlet)
        out.println("<form action='logout' method='post'>");
        out.println("<button type='submit' class='logout-btn'>Log Out</button>");
        out.println("</form>");

        out.println("</div>");
        out.println("</body></html>");
    }
}
