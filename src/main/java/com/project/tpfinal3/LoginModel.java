package com.project.tpfinal3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginModel {

    private final Connection conn;

    public LoginModel() {
        conn = MySqlConnection.getConnection();
    }

    public boolean isDbConnected() {
        try {
            return !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean LoginNow(String user, String pass) throws SQLException {

        PreparedStatement stm = null;
        ResultSet resultSet = null;

        try {
            stm = conn.prepareStatement("select * from employeuser where User = ? and Password = ?");
            stm.setString(1, user);
            stm.setString(2, pass);
            resultSet = stm.executeQuery();

            return resultSet.next();

        } catch (SQLException ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            assert stm != null;
            stm.close();
            assert resultSet != null;
            resultSet.close();

        }
        return false;

    }
}
