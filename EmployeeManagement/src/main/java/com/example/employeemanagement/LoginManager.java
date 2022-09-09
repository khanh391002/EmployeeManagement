package com.example.employeemanagement;

import connection.MySqlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginManager implements Initializable {

    @FXML
    protected TextField userName = new TextField();

    @FXML
    protected TextField passWord = new TextField();

    @FXML
    protected Button btnLogin;
    @FXML
    protected Button btnCancel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void clickButtonLogin(ActionEvent event) throws IOException {
        if(event.getSource() == btnCancel){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
        else if(event.getSource() == btnLogin){
            if(userName.getText().isEmpty() || passWord.getText().isEmpty()){
                showDialog("warning");
            }
            else{
                if(checkExistUserNameAndPassWord(userName.getText(),passWord.getText())){
                    showDialog("main");
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.close();
                }
                else {
                    showDialog("warning");
                }
            }
        }
    }
    private void showDialog(String fxml) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml + ".fxml")));
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkExistUserNameAndPassWord(String userName, String password) throws IOException {
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,userName);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if(resultSet.getString("username").equals(userName) && resultSet.getString("password").equals(password)){
                    return true;
                }
            }
            statement.close();
            resultSet.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
