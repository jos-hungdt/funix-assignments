package com.cry301x.asm3.mfaauth;

import com.cry301x.asm3.crypto.CryptoUtil;
import com.cry301x.asm3.crypto.TimeBaseOTP;
import com.cry301x.asm3.dao.LoginDAO;
import com.cry301x.asm3.dao.OTPDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "otpServlet", value = "/otpctl")
public class OTPServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(OTPServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[/otpctl]: accepts POST request.");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        // Make sure it has a valid 2fa session from login page
        // userid2fa session attribute must be set

        // check2FASession

        log("[/otpctl]: checking 2FA session.");
        HttpSession session = request.getSession(false);
        String userid = (String) session.getAttribute("userid2fa");
        // Remove the userid2fa attribute to prevent multiple submission attempts
        session.removeAttribute("userid2fa");

        String otpValue = (String) request.getParameter("totp");

        if (otpValue == null) {
            log.warning("[/otpctl]: cannot get OTP value.");
            // Invalidate session
            session.invalidate();

            // Redirect error.html
            response.sendRedirect("error.html");
            return;
        }

        // Get otpsecret from Database using OTPDAO class
        log("[/otpctl]: retrieving OTP secret from database.");
        String remoteIp = request.getRemoteAddr();
        String otpsecret = null;
        try {
            otpsecret = OTPDAO.getOTPSecret(userid, remoteIp);
        } catch (SQLException | ClassNotFoundException e) {
            log.warning("[/otpctl]: ERROR - an exception occurred => " + e.getMessage());
            throw new RuntimeException(e);
        }

        // GenerateOTP using TimeBaseOTP class
        log("[/otpctl]: Generate OTP from secret.");
        String otpResult = TimeBaseOTP.generateOTP(CryptoUtil.hexStringToByteArray(otpsecret));
        otpsecret = null;

        if (otpResult == null) {
            log.warning("[/otpctl]: cannot generate OTP from secret.");
            // Invalidate session
            session.invalidate();

            // Redirect error.html
            response.sendRedirect("error.html");
            return;
        }

        assert otpResult != null;
        if (otpResult.equals(otpValue)) {
            // Correct OTP value
            session.invalidate();
            session = request.getSession(true);
            session.setAttribute("userid", userid);
            session.setAttribute("anticsrf_success", "AntiCSRF");

            String custsession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
            response.setHeader("Set-Cookie", custsession);

            // reset fail login attempt
            try {
                OTPDAO.resetFailLogin(userid, remoteIp);
            } catch (SQLException | ClassNotFoundException e) {
                log.warning("[/otpctl]: cannot reset failed login attempts => " + e.getMessage());
                throw new RuntimeException(e);
            }

            log("[/otpctl]: OTP is valid.");
            // Redirect /success.jsp
            response.sendRedirect("success.jsp");
        } else {
            // Incorrect OTP value
            log.warning("Error: Invalid otp value " + remoteIp + " " + userid);

            // Update fail login count.
            try {
                LoginDAO.incrementFailLogin(userid, remoteIp);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            try {
                if (OTPDAO.isAccountLocked(userid, remoteIp)) {
                    response.sendRedirect("locked.html");
                } else {
                    session.setAttribute("userid2fa", userid);
                    session.setAttribute("otperror", "OTP is invalid, please try again.");
                    response.sendRedirect("otp.jsp");
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
