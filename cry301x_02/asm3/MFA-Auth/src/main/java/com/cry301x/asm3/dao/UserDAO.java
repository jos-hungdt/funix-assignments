package com.cry301x.asm3.dao;

import com.cry301x.asm3.enums.SecurityLabel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class UserDAO extends BaseDAO {
    private static final Logger log = Logger.getLogger(UserDAO.class.getName());

    public UserDAO() throws SQLException {
        super();
    }

    /**
     * Retrieves the username for a userid
     *
     * @param userid
     * @param remoteip
     * @return username if successful, null if there is an error
     */
    public static String getUserName(String userid, String remoteip) throws SQLException, ClassNotFoundException {
        String fullName = null;
        // Query the full name of user from database
        String query = "SELECT firstname, lastname FROM users WHERE userid=?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fullName = rs.getString(1) +  ", " + rs.getString(2);
            }
        }

        return fullName;
    }

    public static boolean setAccountSecurityLabel(String userid, int label, String remoteip) throws SQLException, ClassNotFoundException {
        String query = "UPDATE users SET label=? WHERE userid=?";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, label);
            stmt.setString(2, userid);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            return false;
        }

        return true;
    }

    public static int getAccountSecurityLabel(String userid) throws SQLException, ClassNotFoundException {
        int label = -1;
        String query = "SELECT label FROM users WHERE userid=?";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                label = rs.getInt("label");
            }
        }

        return label;
    }
}
