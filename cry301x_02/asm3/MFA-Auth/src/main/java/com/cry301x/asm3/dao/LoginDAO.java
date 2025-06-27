package com.cry301x.asm3.dao;

import com.cry301x.asm3.common.AppConstants;
import com.cry301x.asm3.crypto.CryptoUtil;
import jakarta.servlet.ServletException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class LoginDAO extends BaseDAO {
    private static final Logger log = Logger.getLogger(LoginDAO.class.getName());

    public LoginDAO() {
        super();
    }

    /**
     * Validates user credential
     *
     * @param userid
     * @param password
     * @param remoteip client ip address
     * @return true if user is valid, false otherwise
     */
    public static boolean validateUser(String userid, String password, String remoteip) throws SQLException, ClassNotFoundException {

        boolean isValid = false;
        String query = "SELECT password,salt,faillogin from users WHERE userid=?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && !isAccountLocked(userid, remoteip)) {
                String salt = rs.getString("salt");
                String pass = rs.getString("password");
                if (CryptoUtil.hashPassword(password, salt).equals(pass)) {
                    isValid = true;
                }
//                else {
//                    incrementFailLogin(userid, remoteip);
//                }
            }
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        return isValid;
    }

    /**
     * Check if a user account is locked
     *
     * @param userid
     * @param remoteip client ip address
     * @return true if account is locked or false otherwise
     * @throws ServletException
     */
    public static boolean isAccountLocked(String userid, String remoteip) throws ServletException, SQLException, ClassNotFoundException {
        boolean isLocked = false;
        if (userid == null || remoteip == null) {
            // throw exception;
            throw new ServletException("Userid or remote IP is null");
        }

        // Query database to check account's status
        String query = "SELECT isLocked FROM users WHERE userid=?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isLocked = rs.getBoolean(1);
            }
        }

        return isLocked;
    }

    /**
     * Increments the failed login count for a user
     * Locked the user account if fail logins exceed threshold.
     *
     * @param userid
     * @param remoteip
     * @throws ServletException
     */
    public static void incrementFailLogin(String userid, String remoteip)
            throws ServletException, SQLException, ClassNotFoundException {
        if (userid == null || remoteip == null) {
            // throw exception;
            throw new ServletException("Userid or remote ip is null");
        }

        int currentFailLogins = 0;
        String query = "SELECT faillogin FROM users WHERE userid=?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentFailLogins = rs.getInt(1);
            }
        }

        int failLogin = currentFailLogins + 1;

        if (failLogin >= AppConstants.MAX_FAIL_LOGIN) {
            String lockAccountQuery = "UPDATE users SET isLocked=1 WHERE userid=?";

            try (PreparedStatement stmt = conn.prepareStatement(lockAccountQuery)) {
                stmt.setString(1, userid);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
        }
        String increaseFailLoginQuery = "UPDATE users SET faillogin=? WHERE userid=?";
        try (PreparedStatement stmt = conn.prepareStatement(increaseFailLoginQuery)) {
            stmt.setInt(1, failLogin);
            stmt.setString(2, userid);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
