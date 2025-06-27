<%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/17/2025
  Time: 7:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="java.util.logging.Logger" %>

<%!  private static final Logger log = Logger.getLogger("logout.jsp"); %>

<%

  response.setHeader("Cache-Control", "no-store");

  if (session.getAttribute("userid") != null || session.getAttribute("userid2fa") != null)
  {
    session.invalidate();
    String custsession = "JSESSIONID=" + " " +";Path=/;Secure;HttpOnly;SameSite=Strict";
    response.setHeader("Set-Cookie", custsession);
  }
  else
  {
    log.warning("Error: Invalid logout attempt " + request.getRemoteAddr());
    session.invalidate();
    session = request.getSession(true);
    session.setAttribute("message", "You have not logged in yet!");
    String custsession = "JSESSIONID=" + " " +";Path=/;Secure;HttpOnly;SameSite=Strict";
    response.setHeader("Set-Cookie", custsession);
    //Dispatch request to error.jsp
    RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
    rd.forward(request, response);
    return;
  }

%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles/main.css">
  <title>Logout Page</title>
</head>
<body>
<div class="body-container">
  <div class="mainbody">
    <p>
      You have been logged out!
    </p>

    <p>
      <a href="index.jsp">Login Again</a>
    </p>

  </div>
  <%@include file="templates/footer.html" %>
</div>
</body>
</html>