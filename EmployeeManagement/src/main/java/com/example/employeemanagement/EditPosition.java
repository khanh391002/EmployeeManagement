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

public class EditPosition extends PotisionManager implements Initializable {

    @FXML
    protected TextField txtID = new TextField();
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;
    @FXML
    MenuItem menuItemPosition1 = new MenuItem("Nhân viên - 10tr");
    @FXML
    MenuItem menuItemPosition2 = new MenuItem("Quản lý - 20tr");
    @FXML
    MenuButton menuButtonPosition = new MenuButton("Chức vự",null,menuItemPosition1,menuItemPosition2);

    Employee currentEmployeeEdit = new Employee();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
    @FXML
    void setAcitonPositionMenuButton(){
        menuItemPosition1.setOnAction(event -> {
            menuButtonPosition.setText(menuItemPosition1.getText());
            currentEmployeeEdit.setPosition("Nhân viên"); ;
            currentEmployeeEdit.setSalary(10);
        });
        menuItemPosition2.setOnAction(event -> {
            menuButtonPosition.setText(menuItemPosition2.getText());
            currentEmployeeEdit.setPosition("Quản lý"); ;
            currentEmployeeEdit.setSalary(20);
        });
    }

    public void clickButtonEditEmployee(ActionEvent event) {
        if(event.getSource() == btnCancel){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
        else if(event.getSource() == btnEdit){
            if(txtID.getText().isEmpty()){
                showDialog("warning");
            }
             else {
                if (checkExitID(txtID.getText())) {
                    editPotisionEmployee();
                    Stage stage = (Stage) btnCancel.getScene().getWindow();
                    stage.close();
                } else {
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

    public void editPotisionEmployee(){
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "UPDATE empoylees SET position = ?, salary = ? WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,currentEmployeeEdit.getPosition());
            statement.setFloat(2,currentEmployeeEdit.getSalary());
            statement.setString(3,txtID.getText());
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
                if(resultSet.getString("id").equals(id)){
                    return true;
                }
            }
            resultSet.close();
        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }
}
