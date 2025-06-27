<%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/23/2025
  Time: 9:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles/main.css">
  <title>Error Page</title>
</head>
<body>
<div class="body-container">
  <div class="webformdiv">
    <h3>An error has occurred</h3>
    <p>
      Opps ... Please check back later.
      Contact the administrator if the problem persists.
    </p>

    <h4 class="even-row-color">
      <%
        String errorMessage = (String) session.getAttribute("message");
        if (errorMessage != null)
        {
          session.removeAttribute("message");
          out.println(errorMessage);
        }
      %>
    </h4>
  </div>

  <%@include file="templates/footer.html" %>
</div>
</body>
</html>
