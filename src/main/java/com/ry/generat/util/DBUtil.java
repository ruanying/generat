package com.ry.generat.util;

import com.ry.generat.model.Column;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    /**
     * 获取数据表信息
     *
     * @param tableName
     * @return
     */
    public static List<Column> db(String tableScheam, String tableName) {
        List<Column> columns = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String driver = PropertyUtil.getInstance().get("jdbc.mysql.driver");
        String dbUrl = PropertyUtil.getInstance().get("jdbc.mysql.url");
        String dbUser = PropertyUtil.getInstance().get("jdbc.mysql.username");
        String dbPwd = PropertyUtil.getInstance().get("jdbc.mysql.password");
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
            String sql = "select * from information_schema.columns where table_schema = ? and table_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tableScheam);
            ps.setString(2, tableName);
            resultSet = ps.executeQuery();

            Column column;
            while (resultSet.next()) {
                column = new Column();
                String columnName = resultSet.getString("COLUMN_NAME");
                column.setColumn(columnName);
                String dataType = resultSet.getString("DATA_TYPE").toUpperCase();
                if (dataType.equals("INT")) {
                    dataType = "INTEGER";
                } else if (dataType.equals("DATETIME")) {
                    dataType = "TIMESTAMP";
                } else if (dataType.equals("TEXT")) {
                    dataType = "LONGVARCHAR";
                } else if (dataType.equals("LONGTEXT")) {
                    dataType = "LONGVARCHAR";
                }
                column.setJdbcType(dataType);
                column.setProperty(Underline2Camel.underline2Camel(columnName, true));
                column.setUpProperty(Underline2Camel.underline2Camel(columnName, false));
                column.setComment(resultSet.getString("COLUMN_COMMENT"));
                columns.add(column);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return columns;
    }

    /**
     * 获取数据表注释
     *
     * @param tableName
     * @return
     */
    public static String tableComment(String tableScheam, String tableName) {
        String tableComment = "";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        String driver = PropertyUtil.getInstance().get("jdbc.mysql.driver");
        String dbUrl = PropertyUtil.getInstance().get("jdbc.mysql.url");
        String dbUser = PropertyUtil.getInstance().get("jdbc.mysql.username");
        String dbPwd = PropertyUtil.getInstance().get("jdbc.mysql.password");
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
            String sql = "select * from information_schema.tables where table_schema = ? and table_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, tableScheam);
            ps.setString(2, tableName);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                tableComment = resultSet.getString("TABLE_COMMENT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableComment;
    }

}
