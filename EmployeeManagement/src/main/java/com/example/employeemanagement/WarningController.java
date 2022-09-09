package com.example.employeemanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WarningController implements Initializable {

    @FXML
    private Button buttonClose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void clickButtonClose(ActionEvent event) {
        if(event.getSource() == buttonClose){
            Stage stage = (Stage) buttonClose.getScene().getWindow();
            stage.close();
        }
    }
}
