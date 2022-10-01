package com.project.tpfinal3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button loginBtn;

    @FXML
    private Label connectedLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final LoginModel loginModel = new LoginModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!loginModel.isDbConnected()) {
            connectedLabel.setText("La base de données n'a pu être connecté");
            connectedLabel.setStyle("-fx-text-fill: #ff0000");
        }

        loginBtn.disableProperty().bind(usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
        passwordField.disableProperty().bind(usernameField.textProperty().isEmpty());


    }

    public void isValidUser(ActionEvent event) {
        try {
            if (loginModel.LoginNow(usernameField.getText(), passwordField.getText())) {


                //Open New viewport
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("App.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();


            } else {
                connectedLabel.setText("Nom utlisateur ou mot de passe invalide");
                connectedLabel.setStyle("-fx-text-fill:  #F02B2B");
                passwordField.setText("");

            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}