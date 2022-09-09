package com.example.employeemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button exitButton;

    @FXML
    private Button buttonEmployee;

    @FXML
    private Button buttonPotision;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    public void setActionButton(ActionEvent event) throws IOException {
        if(event.getSource() == buttonEmployee){
            loadPage("employeeManager");
        }
        if(event.getSource() == buttonPotision){
            loadPage("potisionManager");
        }

        if(event.getSource() == exitButton){
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }

    public void loadPage(String fxml) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml + ".fxml")));
        borderPane.setCenter(parent);
    }
}