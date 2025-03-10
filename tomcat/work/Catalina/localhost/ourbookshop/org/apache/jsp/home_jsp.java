/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/10.1.36
 * Generated at: 2025-03-06 08:17:45 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;
import java.util.List;
import Book;

public final class home_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports,
                 org.apache.jasper.runtime.JspSourceDirectives {

  private static final jakarta.servlet.jsp.JspFactory _jspxFactory =
          jakarta.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.LinkedHashSet<>(4);
    _jspx_imports_packages.add("jakarta.servlet");
    _jspx_imports_packages.add("jakarta.servlet.http");
    _jspx_imports_packages.add("jakarta.servlet.jsp");
    _jspx_imports_classes = new java.util.LinkedHashSet<>(3);
    _jspx_imports_classes.add("java.util.List");
    _jspx_imports_classes.add("Book");
  }

  private volatile jakarta.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public boolean getErrorOnELNotFound() {
    return false;
  }

  public jakarta.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final jakarta.servlet.http.HttpServletRequest request, final jakarta.servlet.http.HttpServletResponse response)
      throws java.io.IOException, jakarta.servlet.ServletException {

    if (!jakarta.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final jakarta.servlet.jsp.PageContext pageContext;
    jakarta.servlet.http.HttpSession session = null;
    final jakarta.servlet.ServletContext application;
    final jakarta.servlet.ServletConfig config;
    jakarta.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    jakarta.servlet.jsp.JspWriter _jspx_out = null;
    jakarta.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("<head>\n");
      out.write("    <meta charset=\"UTF-8\">\n");
      out.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("    <title>Home - Our Bookshop</title>\n");
      out.write("    <link rel=\"stylesheet\" href=\"css/style.css\">\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("\n");
      out.write("    <!-- Header -->\n");
      out.write("    <header class=\"header\">\n");
      out.write("        <div class=\"logo\">\n");
      out.write("            <img src=\"images/mainlogo.png\" alt=\"Logo\">\n");
      out.write("            <h1>Our Bookshop</h1>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"search-bar\">\n");
      out.write("            <input type=\"text\" placeholder=\"Search by title or author\">\n");
      out.write("            <button onclick=\"location.href='cart.jsp'\">ð Cart</button>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"cart-user\">\n");
      out.write("            <button onclick=\"location.href='user.jsp'\">ð¤ Username</button>\n");
      out.write("        </div>\n");
      out.write("    </header>\n");
      out.write("\n");
      out.write("    <!-- Image Slider -->\n");
      out.write("    <div class=\"slider\">\n");
      out.write("        <input type=\"radio\" name=\"slider\" id=\"slide1\" checked>\n");
      out.write("        <input type=\"radio\" name=\"slider\" id=\"slide2\">\n");
      out.write("        <input type=\"radio\" name=\"slider\" id=\"slide3\">\n");
      out.write("\n");
      out.write("        <div class=\"slides\">\n");
      out.write("            <div class=\"slide\">\n");
      out.write("                <img src=\"images/disc1.png\" alt=\"Discount 1\">\n");
      out.write("            </div>\n");
      out.write("            <div class=\"slide\">\n");
      out.write("                <img src=\"images/disc2.png\" alt=\"Discount 2\">\n");
      out.write("            </div>\n");
      out.write("            <div class=\"slide\">\n");
      out.write("                <img src=\"images/disc3.png\" alt=\"Discount 3\">\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"navigation\">\n");
      out.write("            <label for=\"slide1\" class=\"nav-dot\"></label>\n");
      out.write("            <label for=\"slide2\" class=\"nav-dot\"></label>\n");
      out.write("            <label for=\"slide3\" class=\"nav-dot\"></label>\n");
      out.write("        </div>\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Promo Images -->\n");
      out.write("    <div class=\"promo-container\">\n");
      out.write("        <img src=\"images/promo1.png\" alt=\"Promo 1\">\n");
      out.write("        <img src=\"images/promo2.png\" alt=\"Promo 2\">\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Genre Buttons -->\n");
      out.write("    <div class=\"genre-container\">\n");
      out.write("        <button class=\"genre-button\" onclick=\"location.href='genre_romance.jsp'\">Romance</button>\n");
      out.write("        <button class=\"genre-button\" onclick=\"location.href='genre_thriller.jsp'\">Thriller</button>\n");
      out.write("        <button class=\"genre-button\" onclick=\"location.href='genre_fantasy.jsp'\">Fantasy</button>\n");
      out.write("        <button class=\"genre-button\" onclick=\"location.href='genre_mystery.jsp'\">Mystery</button>\n");
      out.write("        <button class=\"genre-button\" onclick=\"location.href='genre_horror.jsp'\">Horror</button>\n");
      out.write("        <button class=\"genre-button\" onclick=\"location.href='genre_fiction.jsp'\">Fiction</button>\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Book List (Dynamically Fetched from Database) -->\n");
      out.write("    <div class=\"book-list\">\n");
      out.write("        ");

            List<Book> books = (List<Book>) request.getAttribute("books");
            if (books != null) {
                for (Book book : books) {
        
      out.write("\n");
      out.write("            <div class=\"book-item\">\n");
      out.write("                <img src=\"");
      out.print( book.getImage() );
      out.write("\" alt=\"");
      out.print( book.getTitle() );
      out.write("\">\n");
      out.write("                <div class=\"book-title\">");
      out.print( book.getTitle() );
      out.write("</div>\n");
      out.write("                <div class=\"book-author\">");
      out.print( book.getAuthor() );
      out.write("</div>\n");
      out.write("                <div class=\"book-price\">$");
      out.print( book.getPrice() );
      out.write("</div>\n");
      out.write("                <button class=\"add-to-cart\">â Add to Cart</button>\n");
      out.write("            </div>\n");
      out.write("        ");

                }
            }
        
      out.write("\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof jakarta.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
