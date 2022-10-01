package com.project.tpfinal3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppModel {

    private final Connection conn;
    private PreparedStatement st;

    public AppModel() {
        conn = MySqlConnection.getConnection();
        st = null;
    }

    public ObservableList<Employe> getAllEmployes() {

        ResultSet rs;
        try {
            st = conn.prepareStatement("select * from employelist");
            rs = st.executeQuery();
            return resultToEmployeList(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean executeInsertQueryInDb(String[] data) {
        try {
            if (!isInDb(data[0])) {
                st = conn.prepareStatement("insert into employelist values(?,lower(?),lower(?),?,?,?);");
                st.setString(1, data[0]);
                st.setString(2, data[1]);
                st.setString(3, data[2]);
                st.setString(4, data[3]);
                st.setString(5, data[4]);
                st.setString(6, data[5]);
                st.executeUpdate();
                return true;
            } else {
                //Prompt Error
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Erreur");
                dialog.setHeaderText("Erreur");
                dialog.setContentText("Un employé existe déjà à cet Id.");
                dialog.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }


    public boolean executeUpdateQueryInDb(String[] data) {
        try {
            if (isInDb(data[0])) {
                st = conn.prepareStatement("update employelist set EmployeNom = lower(?), EmployePrenom = lower(?), EmployeGenre = ?," +
                        " EmployeExperience = ?, EmployeSalaire = ? where IdEmployeList = ?;");
                st.setString(1, data[1]);
                st.setString(2, data[2]);
                st.setString(3, data[3]);
                st.setString(4, data[4]);
                st.setString(5, data[5]);
                st.setString(6, data[0]);
                st.executeUpdate();
                return true;
            } else {

                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Erreur");
                dialog.setHeaderText("Erreur");
                dialog.setContentText("Aucun employé n'existe à cet Id.");
                dialog.showAndWait();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void executeDeleteQueryInDb(String id) {
        try {
            if (isInDb(id)) {
                st = conn.prepareStatement("Delete from Employelist where idEmployeList = ?;");
                st.setString(1, id);
                st.executeUpdate();
            } else {
                Alert dialog = new Alert(Alert.AlertType.INFORMATION);
                dialog.setTitle("Erreur");
                dialog.setHeaderText("Erreur");
                dialog.setContentText("Aucun employé n'existe à cet Id.");
                dialog.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInDb(String value) {
        try {
            st = conn.prepareStatement("select * from Employelist where idEmployeList = ?;");
            st.setString(1, value);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return true;
        }
    }


    public ObservableList<Employe> searchNumberInDb(String value) {
        try {
            st = conn.prepareStatement("select * from Employelist where idEmployeList like ? or EmployeSalaire like ? or EmployeExperience like ?;");
            st.setString(1, value + "%");
            st.setString(2, value + "%");
            st.setString(3, value + "%");
            ResultSet rs = st.executeQuery();
            return resultToEmployeList(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Employe> searchGenderInDb(String value) {
        try {
            st = conn.prepareStatement("select * from Employelist where EmployeGenre = ?;");
            st.setString(1, value);
            ResultSet rs = st.executeQuery();
            return resultToEmployeList(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Employe> searchRestInDb(String value) {
        try {
            st = conn.prepareStatement("select * from Employelist where EmployeNom like ? or EmployePrenom like ?  or EmployeExperience like ?;");
            st.setString(1, value + "%");
            st.setString(2, value + "%");
            st.setString(3, value + "%");
            ResultSet rs = st.executeQuery();
            return resultToEmployeList(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Employe> resultToEmployeList(ResultSet set) {
        ObservableList<Employe> employes = FXCollections.observableArrayList();
        Employe emp;
        try {
            while (set.next()) {
                emp = new Employe(
                        set.getInt("idEmployeList"), set.getString("EmployeNom"), set.getString("EmployePrenom"),
                        set.getString("EmployeGenre"), set.getString("EmployeExperience"), set.getDouble("EmployeSalaire"));
                employes.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employes;
    }

}
