package ChillChat.Server;

import java.sql.*;
import java.sql.Connection;
import java.util.Random;

public class DBConnector {

    private Connection connection;

    public DBConnector() throws SQLException {
        setConnection();
    }

    public void setConnection() throws SQLException {
        String url = "jdbc:sqlite:ChillChat.db";
        connection = DriverManager.getConnection(url);
        if (connection != null) {
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("Подключен к БД");
        }
        checkIfTableExists();
    }

    public int checkLoginAttempt(String login, String password) {

        String sql = "SELECT login, password, color FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                int colorCode = 1 + (new Random().nextInt(7));
                insertNewUser(login, password, colorCode);
                return colorCode;
            }
            while (rs.next()) {
                String dbPassword = rs.getString("password");
                if (password.equals(dbPassword)) {
                    return rs.getInt("color");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    private void checkIfTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	login VARCHAR(20) NOT NULL UNIQUE,\n"
                + "	password VARCHAR(20) NOT NULL,\n"
                + "	color INTEGER(3) NOT NULL,\n"
                + " role INTEGER(3) NOT NULL DEFAULT '1',\n"
                + " regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Таблица Users подключена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertNewUser(String login, String password, int color) {

        String sql = "INSERT INTO Users ("
                + "login,"
                + "password,"
                + "color) VALUES(?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setInt(3, color);
            pstmt.executeUpdate();
            System.out.println("Пользователь " + login + " добавлен в таблицу");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Сменить пароль юзера
    public void updateUserPassword(String login, String password) {

        String sql = "UPDATE Users SET password = ? WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, password);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            System.out.println("Обновление пароля пользователя " + login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Сменить роль юзера
    public void updateUserRole(String login, int role) {

        String sql = "UPDATE Users SET role = ? WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, role);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            System.out.println("Обновление роли пользователя " + login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Сменить цвет юзера
    public void updateUserColor(String login, int color) {

        String sql = "UPDATE Users SET color = ? WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, color);
            pstmt.setString(2, login);
            pstmt.executeUpdate();
            System.out.println("Обновление цвета пользователя " + login);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Проверить наличие юзера
    public boolean searchForUser(String login) {
        String sql = "SELECT * FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                return false;
            }
            else
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getUserColor(String login){
        String sql = "SELECT color FROM Users WHERE login=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("color");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 2;
    }
}


