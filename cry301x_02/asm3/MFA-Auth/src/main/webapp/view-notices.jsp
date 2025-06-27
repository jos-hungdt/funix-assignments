<%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/27/2025
  Time: 10:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cry301x.asm3.common.AuthSession" %>
<%@ page import="com.cry301x.asm3.dao.UserDAO" %>
<%@ page import="com.cry301x.asm3.enums.SecurityLabel" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cry301x.asm3.models.News" %>
<%@ page import="com.cry301x.asm3.dao.NoticeDAO" %>
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

    int label = UserDAO.getAccountSecurityLabel(userid);
    ArrayList<News> notices = NoticeDAO.getNotices(label);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles/main.css">
    <title>Account Label</title>
</head>
<body>
<%@include file="templates/navbar.jsp" %>
<div class="body-container">
    <table border="1">
        <tr>
            <th>ID#</th>
            <th>Content</th>
            <th>Author</th>
            <th>Date</th>
            <th>Label</th>
        </tr>

        <% for (News notice : notices) { %>
        <tr>
            <td><%= notice.getId() %></td>
            <td><%= notice.getContent() %></td>
            <td><%= notice.getUserId() %></td>
            <td><%= notice.getDate() %></td>
            <td><%= SecurityLabel.getLabel(notice.getLabel()) %></td>
        </tr>
        <% } %>
    </table>

    <%@include file="templates/footer.html" %>
</div>
</body>
</html>

