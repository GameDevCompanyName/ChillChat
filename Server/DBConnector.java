package ChillChat.Server;

import java.sql.*;
import java.sql.Connection;
import java.util.Random;

public class DBConnector {

    public DBConnector() {

        checkIfDBExists();

    }



    public int checkLoginAttempt(String login, String password) {

        String url = "jdbc:sqlite:ChillChat.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Создана новая БД, если не было старой");
            }

            checkIfTableExists(conn);

            return check(login, password, conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return checkLoginAttempt(login, password);

    }

    private int check(String login, String password, Connection conn) {

        String sql = "SELECT login, password, color FROM Users WHERE login=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()){
                int colorCode = 1 + (new Random().nextInt(7));
                insertNewUser(login, password, colorCode, conn);
                return colorCode;
            }

            while (rs.next()){

                String dbPassword = rs.getString("password");
                if (password.equals(dbPassword)){
                    return rs.getInt("color");
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;

    }

    private void checkIfDBExists() {

        String url = "jdbc:sqlite:ChillChat.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Создана новая БД, если не было старой");
            }

            checkIfTableExists(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void checkIfTableExists(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	login text NOT NULL,\n"
                + "	password text NOT NULL,\n"
                + "	color integer NOT NULL\n"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertNewUser(String login, String password, int color, Connection conn) {

        String sql = "INSERT INTO Users ("
                + "login,"
                + "password,"
                + "color) VALUES(?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setInt(3, color);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
