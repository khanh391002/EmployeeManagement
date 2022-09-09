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
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditController implements Initializable {

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
    private Button btnEdit;
    @FXML
    private Button btnCancel;

    @FXML
    MenuItem menuItemGenderNam = new MenuItem("Nam");
    @FXML
    MenuItem menuItemGenderNu = new MenuItem("Nữ");
    @FXML
    MenuButton menuButtonGender = new MenuButton("Giới tính",null,menuItemGenderNam,menuItemGenderNu);

    Employee currentEmployeeEdit = new Employee();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    void setAcitonGenderMenuButton(){
        menuItemGenderNam.setOnAction(event -> {
            menuButtonGender.setText(menuItemGenderNam.getText());
            currentEmployeeEdit.setGender("Nam");
        });
        menuItemGenderNu.setOnAction(event -> {
            menuButtonGender.setText(menuItemGenderNu.getText());
            currentEmployeeEdit.setGender("Nữ");
        });
    }

    public void clickButtonEditEmployee(ActionEvent event) {
        if(event.getSource() == btnCancel){
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }
        else if(event.getSource() == btnEdit){
            if(txtID.getText().isEmpty() || txtFullName.getText().isEmpty() || txtPhone.getText().isEmpty() ||
                    txtEmail.getText().isEmpty()|| birthdayPicker.getValue() == null){
                showDialog("warning");
            }
            else{
                if(checkExitID(txtID.getText())){
                    editEmployee();
                    Stage stage = (Stage) btnEdit.getScene().getWindow();
                    stage.close();
                }
                else {
                    showDialog("warning");
                }
            }
        }
    }
    public void editEmployee(){
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "UPDATE empoylees SET fullName = ?, phone = ?, " +
                    "email = ?, dateofbirth = ?, gender = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,txtFullName.getText());
            statement.setString(2,txtPhone.getText());
            statement.setString(3,txtEmail.getText());
            statement.setString(4,birthdayPicker.getValue().toString());
            statement.setString(5,currentEmployeeEdit.getGender());
            statement.setString(6,txtID.getText());
            statement.executeUpdate();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
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
}
