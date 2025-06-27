<%@ page import="com.cry301x.asm3.common.AuthSession" %><%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/25/2025
  Time: 11:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  response.setHeader("Cache-Control", "no-store");
  if (!AuthSession.validate(request, response)) {
    return;
  }

  String userid = (String)session.getAttribute("userid");

  if (userid == null) {
    response.sendRedirect("index.jsp");
    return;
  }

  if (!userid.equals("admin")) {
    response.sendRedirect("logout.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles/main.css">
  <title>Account Label</title>
</head>
<body>
<div class="body-container">
  <div class="webformdiv">

    <div class="formheader">
      <h3>Assign Security Level</h3>
    </div>

    <form method="post" autocomplete="off" accept-charset="utf-8" action="account-label">
      <ul class="form-ul" >

        <li>
          <label>Username</label>
          <input type="text" required autofocus name="userId">
        </li>

        <li>
          <label for="securityLevel">Security Level</label>
          <select id="securityLevel" required name="securityLevel">
            <option value="1">Unclassified</option>
            <option value="2">Confidential</option>
            <option value="3">Secret</option>
            <option value="4">TopSecret</option>
          </select>
        </li>

        <li>
          <p class="settingmsg">
            <%
              String errorMsg = (String) session.getAttribute("errorMsg");
              if (errorMsg != null) {
                session.removeAttribute("errorMsg");
                out.println(errorMsg);
              }
            %>
          </p>
          <p>
            <%
              String msg = (String) session.getAttribute("message");
              if (msg != null) {
                session.removeAttribute("message");
                out.println(msg);
              }
            %>
          </p>
        </li>

        <li>
          <input type="submit" value="Apply" >
        </li>
      </ul>
    </form>

  </div>

  <%@include file="templates/footer.html" %>
</div>
</body>
</html>
