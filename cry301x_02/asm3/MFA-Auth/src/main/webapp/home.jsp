<%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/27/2025
  Time: 7:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.cry301x.asm3.dao.UserDAO" %>
<%@ page import="com.cry301x.asm3.common.HtmlEscape" %>
<%@ page import="com.cry301x.asm3.common.AuthSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  response.setHeader("Cache-Control", "no-store");
  if (!AuthSession.validate(request, response)) {
    return;
  }

  String userid = (String)session.getAttribute("userid");

  if (userid == null) {
    response.sendRedirect("error.html");
    return;
  }

  String username = UserDAO.getUserName(userid, request.getRemoteAddr());
  username = HtmlEscape.escapeHTML(username);
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles/main.css">
  <title>Success Page</title>
</head>
<body>
<%@include file="templates/navbar.jsp" %>
<div class="body-container">
  <div class="mainbody">
    <div class="webformdiv">
      <p>
        Welcome
        <%
          if (username != null) {
            out.print(username);
          }
          else {
            out.print("Unknown");
          }

        %>
        <br>
      </p>

      <p>
        <a href="view-notices.jsp">View notices</a>
      </p>

      <p>
        <a href="post-notices.jsp">Write notices</a>
      </p>

      <% if (userid.equals("admin")) {  %>
      <p>
        <a href="account-label.jsp">Account Label</a>
      </p>
      <% } %>

      <p>
        <a href="logout.jsp">Logout</a>
      </p>
    </div>

  </div>
  <%@include file="templates/footer.html" %>
</div>
</body>
</html>