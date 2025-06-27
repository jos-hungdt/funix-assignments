<%--
  Created by IntelliJ IDEA.
  User: joshu
  Date: 6/16/2025
  Time: 10:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cry301x.asm3.common.AuthSession" %>

<%
    response.setHeader("Cache-Control", "no-store");

    if(!AuthSession.check2FASession(request, response, "index.jsp")) {
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles/main.css">
    <title>2 Factor Authentication Page</title>
</head>
<body>
<div class="body-container">
    <div class="webformdiv">
        <div class="formheader">
            <h3>2 Factor Authentication</h3>
        </div>

        <form method="POST" autocomplete="off" accept-charset="utf-8" action="otpctl">
            <ul class="form-ul" >

                <li>
                    <label>Enter OTP</label>
                </li>

                <li>
                    <div id="msg" class="settingmsg">

                        <%
                            //Check for OTP error message
                            String otperror = (String) session.getAttribute("otperror");
                            if (otperror != null)
                            {
                                session.removeAttribute("otperror");
                                out.println("Invalid OTP");
                            }
                        %>


                    </div>
                    <input type="text" required name="totp" size="25" >
                </li>
                <li>
                    <input type="submit" value="Submit" >
                </li>

            </ul>
        </form>

    </div>

    <%@include file="templates/footer.html" %>
</div>
</body>
</html>
