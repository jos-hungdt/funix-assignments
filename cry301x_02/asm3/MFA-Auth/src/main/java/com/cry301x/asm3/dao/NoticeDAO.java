package com.cry301x.asm3.dao;

import com.cry301x.asm3.models.News;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class NoticeDAO extends BaseDAO {
    private static final Logger log = Logger.getLogger(NoticeDAO.class.getName());

    public static boolean insertNotice(News news) throws SQLException, ClassNotFoundException {
        boolean isSuccess = false;
        String query = "INSERT INTO news (userid, content, date, label) VALUES (?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement insertStmt = conn.prepareStatement(query)) {
            insertStmt.setString(1, news.getUserId());
            insertStmt.setString(2, news.getContent());
            insertStmt.setDate(3, news.getDate());
            insertStmt.setInt(4, news.getLabel());

            insertStmt.execute();
            isSuccess = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    public static ArrayList<News> getNotices(int securityLabel) throws SQLException, ClassNotFoundException {
        ArrayList<News> newsList = new ArrayList<>();

        String query = "SELECT * FROM news WHERE label <= ?";

        Connection conn = getConnection();
        try (PreparedStatement selectStmt = conn.prepareStatement(query)) {
            selectStmt.setInt(1, securityLabel);

            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt(1));
                news.setUserId(rs.getString(2));
                news.setContent(rs.getString(3));
                news.setDate(rs.getDate(4));
                news.setLabel(rs.getInt(5));

                newsList.add(news);
            }
        }

        return newsList;
    }
}
