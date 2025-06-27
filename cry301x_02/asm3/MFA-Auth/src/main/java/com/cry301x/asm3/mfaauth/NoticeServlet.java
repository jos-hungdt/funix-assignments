package com.cry301x.asm3.mfaauth;

import com.cry301x.asm3.dao.NoticeDAO;
import com.cry301x.asm3.dao.UserDAO;
import com.cry301x.asm3.models.News;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "noticeServlet", value = "/notices")
public class NoticeServlet extends BaseServlet {
    private static final Logger log = Logger.getLogger(NoticeServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("[/notices]: accepts POST.");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warning("[/notices]: session is null, redirecting to index.jsp");
            // no existing session, Redirect to index.jsp
            response.sendRedirect("index.jsp");
            return;
        }

        String userId = (String)session.getAttribute("userid");
        String content = request.getParameter("noticeContent");
        int label = Integer.parseInt(request.getParameter("securityLevel"));

        if (content == null) {
            log.warning("[/notices]: content is null, redirecting to post-notices.jsp");
            response.sendRedirect("post-notices.jsp");
            return;
        }

        try {
            if (label < UserDAO.getAccountSecurityLabel(userId)) {
                session.setAttribute("errorMsg", "Error! Message's security level cannot be lower than your level.");
                response.sendRedirect("post-notices.jsp");
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            redirectToErrorPage(request, response, session, e.getMessage());
            return;
        }

        News news = new News();
        news.setUserId(userId);
        news.setContent(content);
        news.setLabel(label);
        news.setDate(new Date((new java.util.Date()).getTime()));

        try {
            if (NoticeDAO.insertNotice(news)) {
                session.setAttribute("message", "Success!!!");
                response.sendRedirect("post-notices.jsp");
                return;
            } else {
                session.setAttribute("errorMsg", "Cannot post the notice due to a server error.");
                response.sendRedirect("post-notices.jsp");
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            redirectToErrorPage(request, response, session, e.getMessage());
            return;
        }
    }
}
