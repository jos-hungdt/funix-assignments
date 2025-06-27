package com.cry301x.asm3.mfaauth;

import com.cry301x.asm3.dao.LoginDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(LoginServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[/login]: accepts POST request.");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        if (session == null) {//no existing session
            log.warning("[/login]: no existing session.");
            // Redirect to index.jsp
            response.sendRedirect("index.jsp");
            return;
        }

        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        if (userid == null || password == null) {
            // Redirect to index.jsp
            response.sendRedirect("index.jsp");
            return;
        }

        try {
            if (LoginDAO.isAccountLocked(userid, request.getRemoteAddr())) {
                log.warning("Error: Account is locked " + userid  + " " + request.getRemoteAddr());
                redirectToErrorPage(request, response, session, "Error: " + userid + " is locked");
//                session.setAttribute("errorMsg", "Error: " + userid + " is locked");
//                response.sendRedirect("index.jsp");
            } else if (LoginDAO.validateUser(userid, password, request.getRemoteAddr())) {
                password = null;
                //Prevent Session fixation, invalidate and assign a new session

                session.invalidate();
                session = request.getSession(true);
                session.setAttribute("userid2fa", userid);
                //Set the session id cookie with HttpOnly, secure and samesite flags
                String custsession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
                response.setHeader("Set-Cookie", custsession);

                //Dispatch request to otp.jsp
                RequestDispatcher rd = request.getRequestDispatcher("otp.jsp");
                rd.forward(request, response);
            } else {
                log.warning("Error: Username or password is invalid " + userid  + " " + request.getRemoteAddr());
                String remoteip = request.getRemoteAddr();
                LoginDAO.incrementFailLogin(userid, remoteip);
                session.setAttribute("errorMsg", "Username or password is invalid");
                response.sendRedirect("index.jsp");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
