package com.cry301x.asm3.dao;

import com.cry301x.asm3.crypto.CryptoUtil;
import jakarta.servlet.ServletException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class OTPDAO extends BaseDAO {
    private static final Logger log = Logger.getLogger(OTPDAO.class.getName());

    public OTPDAO() throws SQLException {
        super();
    }

    /**
     * Retrieves the otp secret hexadecimal string from the userid
     *
     * @param userid
     * @param remoteIp
     * @return hexadecimal secret string
     * @throws ServletException
     */
    public static String getOTPSecret(String userid, String remoteIp) throws ServletException, SQLException, ClassNotFoundException {
        String otpSecret = null;
        if (userid == null || remoteIp == null) {
            throw new ServletException("Userid or remote IP is null");
        }

        // Query otp from database
        String query = "SELECT otpsecret FROM users WHERE userid=?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                otpSecret = rs.getString(1);
            }
        }

        return otpSecret;
    }

    /**
     * Retrieves the otp secret hexadecimal string from the userid
     *
     * @param userid
     * @param remoteip
     * @return hexadecimal secret string
     * @throws ServletException
     */
    public static String getBase32OTPSecret(String userid, String remoteip) throws ServletException, SQLException, ClassNotFoundException {
        String hexaString = getOTPSecret(userid, remoteip);
        byte[] hexaCode = CryptoUtil.hexStringToByteArray(hexaString);
        return CryptoUtil.base32Encode(hexaCode);
    }

    /**
     * Check if a user account is locked
     *
     * @param userid
     * @param remoteip
     * @return true if account is locked, false otherwise
     * @throws ServletException
     */
    public static boolean isAccountLocked(String userid, String remoteip) throws ServletException, SQLException, ClassNotFoundException {
        boolean isLock = true;
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
                isLock = rs.getBoolean(1);
            }
        }

        return isLock;
    }


    /**
     * Reset the failed login counts of a user to zero
     * If an account is locked an exception will be thrown
     *
     * @param userid
     * @param remoteIp
     * @throws ServletException
     */
    public static void resetFailLogin(String userid, String remoteIp) throws ServletException, SQLException, ClassNotFoundException {
        if (userid == null || remoteIp == null)  {
            // throw exception;
            throw new ServletException("Userid or remote IP is null");
        }

        // Update failLogin in database to zero
        String query = "UPDATE users SET faillogin=0 WHERE userid=?";
        Connection conn = getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}
