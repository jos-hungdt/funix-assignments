package com.cry301x.asm3.mfaauth;

import java.io.*;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Logger;

import com.cry301x.asm3.common.AppConstants;
import com.cry301x.asm3.crypto.CryptoUtil;
import com.cry301x.asm3.dao.RegisterDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "registerServlet", value = "/register")
public class RegisterServlet extends BaseServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[/register]: accepts POST.");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warning("[/register]: session is null, redirecting to index.jsp");
            // no existing session, Redirect to index.jsp
            response.sendRedirect("index.jsp");
            return;
        }
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String userid = request.getParameter("userid");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        if (firstName == null || lastName == null || userid == null || password == null || repassword == null) {
            // Redirect to index.jsp
            log.warning("[/register]: Register details is missing. Redirecting to index.jsp");
            response.sendRedirect("index.jsp");
            return;
        }

        if (password.length() < AppConstants.MIN_LENGTH_PASS) {
            // Redirect to error.html
            log.warning("[/register]: Password is not satisfied safety conditions.");
            redirectToErrorPage(request, response, session, "Password length must be greater or equal to " + AppConstants.MIN_LENGTH_PASS);
            // response.sendRedirect("error.html");
            return;
        }

        if (password.compareTo(repassword) != 0) {
            // Redirect to error.html
            log.warning("[/register]: Re-password is incorrect.");
            response.sendRedirect("error.html");
            redirectToErrorPage(request, response, session, " Re-password is incorrect.");
            return;
        }

        try {
            if (RegisterDAO.findUser(userid, request.getRemoteAddr())) {
                // Redirect to error.html
                log.warning("[/register]: User already exists.");
                // response.sendRedirect("error.html");
                redirectToErrorPage(request, response, session, "Username already exists.");
            } else {
                // Generate salt by CryptoUtil
                byte[] saltByte = CryptoUtil.generateRandomBytes(CryptoUtil.SALT_SIZE);

                // Encode Base64 salt
                String salt = Base64.getEncoder().encodeToString(saltByte);

                // Generate hexadecimal OTP Secret by CryptoUtil
                String otpSecret = CryptoUtil.genHexaOTPSecret();

                String remoteIp = request.getRemoteAddr();

                // Add new user to Database
                boolean isAddUserSuccess = RegisterDAO.addUser(firstName, lastName, userid, password, salt, otpSecret, remoteIp);
                if (!isAddUserSuccess) {
                    log.warning("[/register]: Failed to add user to the database.");
                    response.sendRedirect("error.html");
                }

                password = null;
                repassword = null;
                //Prevent Session fixation, invalidate and assign a new session
                assert session != null;
                session.invalidate();
                session = request.getSession(true);
                session.setAttribute("userid2fa", userid);

                //Set the session id cookie with HttpOnly, secure and samesite flags
                String custSession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
                response.setHeader("Set-Cookie", custSession);

                log("[/register]: success validating user info, redirecting to confirm page.");
                //Dispatch request to confirm.jsp
                RequestDispatcher rd = request.getRequestDispatcher("confirm.jsp");
                rd.forward(request, response);
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.warning("[/register]: ERROR - an exception occurred => " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}