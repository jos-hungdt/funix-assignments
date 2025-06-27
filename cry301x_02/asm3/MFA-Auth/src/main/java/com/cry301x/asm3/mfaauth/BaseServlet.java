package com.cry301x.asm3.mfaauth;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class BaseServlet extends HttpServlet {
    protected void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response, HttpSession session, String errorMessage) {
        try {
            assert session != null;
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("message", errorMessage);

            //Set the session id cookie with HttpOnly, secure and samesite flags
            String custSession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
            response.setHeader("Set-Cookie", custSession);

            //Dispatch request to error.jsp
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
            rd.forward(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
