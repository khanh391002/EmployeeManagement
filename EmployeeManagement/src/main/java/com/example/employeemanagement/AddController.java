package com.example.employeemanagement;

import connection.MySqlConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class AddController extends EmployeeManager implements Initializable {

    @FXML
    protected TextField txtID = new TextField();
    @FXML
    protected TextField txtFullName= new TextField();
    @FXML
    protected TextField txtPhone= new TextField();
    @FXML
    protected TextField txtEmail= new TextField();
    @FXML
    protected DatePicker birthdayPicker = new DatePicker();
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    @FXML
    MenuItem menuItemGenderNam = new MenuItem("Nam");
    @FXML
    MenuItem menuItemGenderNu = new MenuItem("Nữ");
    @FXML
    MenuButton menuButtonGender = new MenuButton("Giới tính",null,menuItemGenderNam,menuItemGenderNu);

    Employee currentEmployee = new Employee();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    @FXML
    void setAcitonGenderMenuButton(){
        menuItemGenderNam.setOnAction(event -> {
            menuButtonGender.setText(menuItemGenderNam.getText());
            currentEmployee.setGender("Nam");
        });
        menuItemGenderNu.setOnAction(event -> {
            menuButtonGender.setText(menuItemGenderNu.getText());
            currentEmployee.setGender("Nữ");
        });
    }
    public void clickButtonAddEmployee(ActionEvent event) {
        if(event.getSource() == btnCancel){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
        else if(event.getSource() == btnAdd){
            if(txtID.getText().isEmpty() || txtFullName.getText().isEmpty() || txtPhone.getText().isEmpty() ||
                    txtEmail.getText().isEmpty()|| birthdayPicker.getValue() == null){
                showDialog("warning");
            }
            else{
                if(checkExitID(txtID.getText())){
                    showDialog("warning");
                }
                else {
                    insertEmployeeToDb();
                    Stage stage = (Stage) btnAdd.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }
    public void insertEmployeeToDb(){
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "INSERT INTO empoylees(id,fullName,phone,email,dateofbirth,gender) VALUES (?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,txtID.getText());
            statement.setString(2,txtFullName.getText());
            statement.setString(3,txtPhone.getText());
            statement.setString(4,txtEmail.getText());
            statement.setString(5,birthdayPicker.getValue().toString());
            statement.setString(6,currentEmployee.getGender());
            statement.executeUpdate();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean checkExitID(String id){
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM empoylees";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                if(id.equals(resultSet.getString("id"))){
                    return true;
                }
            }
            resultSet.close();
        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
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
}
