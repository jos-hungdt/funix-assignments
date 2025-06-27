package com.cry301x.asm3.common;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

public class AuthSession {
    private static final Logger log = Logger.getLogger(AuthSession.class.getName());

    /**
     * Validate if a session has been authenticated successfully and is still valid
     * Redirect to login page if session is not authenticated or invalid
     *
     * @param req
     * @param resp
     * @return true if session is authenticated successfully, false otherwise
     * @throws IOException
     * @throws ServletException
     */
    public static boolean validate(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
    {
        if (req == null || resp == null) {
            // Throw Exception
            throw new ServletException("In AuthSession.validate: req or resp is null.");
        }

        HttpSession sess = req.getSession(false);
        if (sess == null) {
            // Redirect index.jsp
            resp.sendRedirect("index.jsp");
            return false;
        }

        if (sess.getAttribute("userid") == null) {
            // not authenticated Redirect index.jsp
            resp.sendRedirect("index.jsp");
            return false;
        }

        return true;
    }

    /**
     * Check if 2fa userid attribute is set. If it is not, redirect to specified error url.
     * @param req - HTTP request
     * @param resp - HTTP response
     * @param redirectUrl - redirect URL after validation.
     * @return true if 2fa userid attribute is properly set, false otherwise
     * @throws IOException
     * @throws ServletException
     */
    public static boolean check2FASession(HttpServletRequest req, HttpServletResponse resp, String redirectUrl)
            throws IOException, ServletException  {
        if (req == null || resp == null || redirectUrl == null) {
            // Throw Exception
            throw new ServletException("Request, Response or RedirectUrl is null");
        }

        HttpSession session = req.getSession(false);

        if (session == null) {
            // Redirect redirectUrl
            resp.sendRedirect(redirectUrl);
        }

        String userid2fa = (String) session.getAttribute("userid2fa");
        if (userid2fa == null) {
            // Redirect redirectUrl
            resp.sendRedirect(redirectUrl);
        }

        return true;
    }
}
