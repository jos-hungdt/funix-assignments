package com.cry301x.asm3.mfaauth;

import com.cry301x.asm3.dao.LoginDAO;
import com.cry301x.asm3.dao.RegisterDAO;
import com.cry301x.asm3.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "accountLabelServlet", value = "/account-label")
public class AccountLabelServlet extends BaseServlet {
    private static final Logger log = Logger.getLogger(AccountLabelServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[/account-label]: accepts POST.");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warning("[/account-label]: session is null, redirecting to index.jsp");
            // no existing session, Redirect to index.jsp
            response.sendRedirect("index.jsp");
            return;
        }

        String userId = request.getParameter("userId");
        int securityLevel = Integer.parseInt(request.getParameter("securityLevel"));

        if (userId == null || securityLevel == 0) {
            log.warning("[/account-label]: userId or securityLevel is null.");
            redirectToErrorPage(request, response, session, "UserId or  SecurityLevel is null.");
            return;
        }

        try {
            if (!RegisterDAO.findUser(userId, request.getRemoteAddr())) {
                session.setAttribute("errorMsg", "Not found user");
                response.sendRedirect("account-label.jsp");
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            if (UserDAO.setAccountSecurityLabel(userId, securityLevel, request.getRemoteAddr())) {
                session.setAttribute("message", "Success set security level for " + userId + ".!");
                response.sendRedirect("account-label.jsp");
                return;
            } else {
                session.setAttribute("errorMsg", "There was an error while trying to set the security level.");
                response.sendRedirect("account-label.jsp");
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
