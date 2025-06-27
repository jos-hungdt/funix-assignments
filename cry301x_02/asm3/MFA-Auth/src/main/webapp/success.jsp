<%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/17/2025
  Time: 7:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.cry301x.asm3.dao.UserDAO" %>
<%@page import="com.cry301x.asm3.common.AuthSession" %>
<%@page import="com.cry301x.asm3.common.HtmlEscape" %>

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

  //Prevent CSRF by requiring OTP validation each time page is displayed.
  String anticsrf = (String)session.getAttribute("anticsrf_success");
  if (anticsrf == null) {//token not present redirect back to OTP page for validation again
    session.removeAttribute("userid");
    session.setAttribute("userid2fa", userid);
    userid = null;
    RequestDispatcher rd = request.getRequestDispatcher("otp.jsp");
    rd.forward(request, response);
    return;
  } else {//token present
    //remove the token so that subsequent request will require OTP validation
    session.removeAttribute("anticsrf_success");
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
