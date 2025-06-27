package com.cry301x.asm3.dao;

import com.cry301x.asm3.crypto.CryptoUtil;
import com.cry301x.asm3.enums.SecurityLabel;

import java.sql.*;

public class RegisterDAO extends BaseDAO {
    /**
     * Check user existence
     *
     * @param userid - userid to check exist.
     * @param remoteIp client ip address
     * @return true if user is existent, false otherwise
     */
    public static boolean findUser(String userid, String remoteIp) throws SQLException, ClassNotFoundException {
        boolean isFound = false;
        Connection conn = getConnection();
        String query = "SELECT EXISTS(SELECT * from users WHERE userid=?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getBoolean(1)) {
                isFound = true;
            }
        }

        return isFound;
    }

    public static boolean addUser(String firstName, String lastName, String userid, String password, String salt, String otpSecret, String remoteIp) throws SQLException, ClassNotFoundException {
        boolean isSuccess = false;

        // Hash password with salt
        String hashedPassword = CryptoUtil.hashPassword(password, salt);

        // Insert new record to database. If operation is success set isSuccess to true
        String query = "INSERT INTO users (userid, firstname, lastname, salt, password, isLocked, faillogin, otpsecret, label) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = getConnection();
        try (PreparedStatement insertStmt = conn.prepareStatement(query)) {
            insertStmt.setString(1, userid);
            insertStmt.setString(2, firstName);
            insertStmt.setString(3, lastName);
            insertStmt.setString(4, salt);
            insertStmt.setString(5, hashedPassword);
            insertStmt.setBoolean(6, false);
            insertStmt.setBoolean(7, false);
            insertStmt.setString(8, otpSecret);
            insertStmt.setInt(9, SecurityLabel.Unclassified.getValue());

            insertStmt.execute();
            isSuccess = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }
}
