package com.project.tpfinal3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AppController implements Initializable {
    @FXML
    private Label titleLbl;
    @FXML
    private TextField searchField;
    @FXML
    private Button insertBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private ComboBox<String> comboBoxGender;
    @FXML
    private ComboBox<String> comboBoxExperience;
    @FXML
    private TextField idField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField salaireField;
    @FXML
    private TableView<Employe> colPage;
    @FXML
    private TableColumn<Employe, Integer> idCol;
    @FXML
    private TableColumn<Employe, String> nomCol;
    @FXML
    private TableColumn<Employe, String> prenomCol;
    @FXML
    private TableColumn<Employe, String> genreCol;
    @FXML
    private TableColumn<Employe, String> experienceCol;
    @FXML
    private TableColumn<Employe, String> salaireCol;

    //Array boolean de non validite
    private final boolean[] areNotValid = {true, true, true, false};
    AppModel appModel = new AppModel();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Employe> list = appModel.getAllEmployes();
        populateEmployeTable(list);
        populateComboBox();
        disableButtonsOnInvald();
    }


    public void disconnectToLogin(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateComboBox() {
        //Remplit les boîtes de choix et set le default
        comboBoxGender.setItems(FXCollections.observableArrayList("A", "M", "F"));
        comboBoxGender.getSelectionModel().selectFirst();
        comboBoxExperience.setItems(FXCollections.observableArrayList("0 ans","1 ans", "2 ans", "3 ans", "4 ans", "5 ans", "5+ ans"));
        comboBoxExperience.getSelectionModel().selectFirst();
        salaireField.setText("35000");
    }

    public void disableButtonsOnInvald() {

        deleteBtn.setDisable(true);
        updateBtn.setDisable(true);
        insertBtn.setDisable(true);


        idField.textProperty().addListener((observableValue, s, t1) -> {
            areNotValid[0] = !Pattern.matches("^[0-9]*\\b$", t1);
            deleteBtn.setDisable(areNotValid[0]);
            updateBtn.setDisable(areInvalidFields(areNotValid));
            insertBtn.setDisable(areInvalidFields(areNotValid));
        });

        nomField.textProperty().addListener((observableValue, s, t1) -> {
            areNotValid[1] = t1.equals("");
            updateBtn.setDisable(areInvalidFields(areNotValid));
            insertBtn.setDisable(areInvalidFields(areNotValid));
        });
        prenomField.textProperty().addListener((observableValue, s, t1) -> {
            areNotValid[2] = t1.equals("");
            updateBtn.setDisable(areInvalidFields(areNotValid));
            insertBtn.setDisable(areInvalidFields(areNotValid));
        });
        salaireField.textProperty().addListener((observableValue, s, t1) -> {
            areNotValid[3] = !Pattern.matches("^\\d+(\\.\\d{2})?$", t1);
            updateBtn.setDisable(areInvalidFields(areNotValid));
            insertBtn.setDisable(areInvalidFields(areNotValid));
        });

        comboBoxExperience.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            String choice = comboBoxExperience.getValue();
            switch (choice) {

                case "0 ans" -> salaireField.setText("35000");
                case "1 ans" -> salaireField.setText("45000");
                case "2 ans" -> salaireField.setText("55000");
                case "3 ans" -> salaireField.setText("65000");
                case "4 ans" -> salaireField.setText("75000");
                case "5 ans" -> salaireField.setText("85000");
                case "5+ ans" -> salaireField.setText("95000");
                default -> salaireField.setText("0");
            }
        });


        searchField.textProperty().addListener((observableValue, s, t1) -> {
            ObservableList<Employe> list;

            if (Pattern.matches("^\\d*\\.?\\d*?", t1)) {
                list = appModel.searchNumberInDb(t1);
                populateEmployeTable(list);
            } else if (t1.equals("A") || t1.equals("F") || t1.equals("M")) {
                list = appModel.searchGenderInDb(t1);
                populateEmployeTable(list);
            } else {
                list = appModel.searchRestInDb(t1);
                populateEmployeTable(list);
            }
        });
    }


    public boolean areInvalidFields(boolean[] list) {
        return list[0] || list[1] || list[2] || list[3];
    }

    public void addEmploye() {
        boolean isAdded = appModel.executeInsertQueryInDb(getValuesInField());
        ObservableList<Employe> list = appModel.getAllEmployes();
        populateEmployeTable(list);
        if (isAdded) {
            clearFields();
        }
    }


    public void deleteEmploye() {
        appModel.executeDeleteQueryInDb(idField.getText());
        ObservableList<Employe> list = appModel.getAllEmployes();
        populateEmployeTable(list);
    }

    public void updateEmploye() {
        boolean isUpdated = appModel.executeUpdateQueryInDb(getValuesInField());
        ObservableList<Employe> list = appModel.getAllEmployes();
        populateEmployeTable(list);
        if (isUpdated) {
            clearFields();
        }
    }

    public void populateEmployeTable(ObservableList<Employe> list) {
        //Association data to column
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        experienceCol.setCellValueFactory(new PropertyValueFactory<>("experience"));
        salaireCol.setCellValueFactory(new PropertyValueFactory<>("salaire"));

        //Populate Columns
        colPage.setItems(list);
    }

    private String[] getValuesInField() {
        return new String[]{idField.getText(), nomField.getText(), prenomField.getText(), comboBoxGender.getValue(),
                comboBoxExperience.getValue(), salaireField.getText()};
    }


    public void setEmployeDataToFields() {
        if (colPage.getSelectionModel().getSelectedItem() != null) {
            Employe emp = colPage.getSelectionModel().getSelectedItem();

            idField.setText(emp.getId().toString());
            nomField.setText((emp.getNom()));
            prenomField.setText(emp.getPrenom());
            salaireField.setText(emp.getSalaire().toString());
            comboBoxGender.getSelectionModel().select(emp.getGenre());
            comboBoxExperience.getSelectionModel().select(emp.getExperience());
        }
    }

    @FXML
    private void clearFields() {
        idField.setText("");
        nomField.setText("");
        prenomField.setText("");
        salaireField.setText("");
        comboBoxGender.getSelectionModel().selectFirst();
        comboBoxExperience.getSelectionModel().selectFirst();
    }


}

