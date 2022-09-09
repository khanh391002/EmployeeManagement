package com.example.employeemanagement;

import connection.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class EmployeeManager implements Initializable {
    @FXML
    protected TableView<Employee> employeeTableView = new TableView<>();

    @FXML
    protected TableColumn<Employee, String> columnId = new TableColumn<>();

    @FXML
    protected TableColumn<Employee, String> columnName = new TableColumn<>();

    @FXML
    protected TableColumn<Employee, String> columnPhone = new TableColumn<>();

    @FXML
    protected TableColumn<Employee, String> columnEmail = new TableColumn<>();

    @FXML
    protected TableColumn<Employee, String> columnDateOfBirth = new TableColumn<>();

    @FXML
    protected TableColumn<Employee, String> columnGender = new TableColumn<>();

    @FXML
    TextField searchText = new TextField();

    @FXML
    ImageView imageAdd = new ImageView();
    @FXML
    ImageView imageEdit = new ImageView();
    @FXML
    ImageView imageSearch = new ImageView();
    @FXML
    ImageView imageRemove = new ImageView();
    @FXML
    ImageView imageReload = new ImageView();


    ObservableList<Employee> observableList = FXCollections.observableArrayList();

    public Employee currentEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            updateTable();
            for (Employee employee: observableList){
                System.out.println(employee.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handAdd(javafx.scene.input.MouseEvent mouseEvent) {
        showDialog("addnew");
    }

    public void handEdit(javafx.scene.input.MouseEvent mouseEvent) {
        showDialog("edit");
    }

    public void handSearch(javafx.scene.input.MouseEvent mouseEvent) {
        observableList.clear();
        searchEmployees();
    }

    public void handRemove(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        removeEmployee();
        observableList.clear();
        updateTable();
    }

    public void handReload(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        observableList.clear();
        updateTable();
        ghiFile();
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

    public void getDataEmployees() throws IOException {
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM empoylees";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                observableList.add(new Employee(resultSet.getString("id"),
                        resultSet.getString("fullName"), resultSet.getString("phone"),
                        resultSet.getString("email"), resultSet.getString("dateofbirth"),
                        resultSet.getString("gender"),resultSet.getString("position"),
                        resultSet.getFloat("salary")));
            }
            statement.close();
            resultSet.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() throws IOException {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        columnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        getDataEmployees();
        employeeTableView.setItems(observableList);
    }

    public void searchEmployees() {
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM empoylees WHERE id like ? or fullName like ? or gender like ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchText.getText() + "%");
            statement.setString(2, "%" + searchText.getText() + "%");
            statement.setString(3, "%" + searchText.getText() + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                observableList.add(new Employee(resultSet.getString("id"),
                        resultSet.getString("fullName"), resultSet.getString("phone"),
                        resultSet.getString("email"), resultSet.getString("dateofbirth"),
                        resultSet.getString("gender"),resultSet.getString("position"),
                        resultSet.getFloat("salary")));
            }
            statement.close();
            resultSet.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        for (Employee employee : observableList) {
            System.out.println(employee.toString());
        }
        employeeTableView.setItems(observableList);
    }
    public void removeEmployee(){
        Employee employee = selectEmployee();
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "DELETE FROM empoylees WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, employee.getId());
            statement.executeUpdate();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee selectEmployee() {
        for (Employee employee : employeeTableView.getSelectionModel().getSelectedItems()) {
            currentEmployee = new Employee(employee.getId(), employee.getName(), employee.getPhone(), employee.getEmail(),
                    employee.getDateOfBirth(), employee.getGender(),employee.getPosition(),employee.getSalary());
        }
        return currentEmployee;
    }

    public ArrayList<Employee> getEmployeesNotPosition(){
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM empoylees WHERE position IS NULL or salary IS NULL";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                employees.add(new Employee(resultSet.getString("id"),
                        resultSet.getString("fullName"), resultSet.getString("phone"),
                        resultSet.getString("email"), resultSet.getString("dateofbirth"),
                        resultSet.getString("gender"),resultSet.getString("position"),
                        resultSet.getFloat("salary")));
            }
            statement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
        return employees;
    }

    public void ghiFile() throws IOException {
        FileWriter myWriter = new FileWriter("employeeNotPosition.txt");
        ArrayList<Employee> employees = getEmployeesNotPosition();
        for (Employee employee : employees){
            System.out.println(employee.toString());
        }
        for (Employee employee : employees) {
            char[] id = employee.getId().toCharArray();
            char[] name = employee.getName().toCharArray();
            char[] phone = employee.getPhone().toCharArray();
            char[] email = employee.getEmail().toCharArray();
            char[] dateOfBirth = employee.getDateOfBirth().toCharArray();
            char[] gender = employee.getGender().toCharArray();
            for (char c : id) {
                myWriter.write(Integer.toBinaryString(c));
            }
            myWriter.write("\n");
            for (char c : name) {
                myWriter.write(Integer.toBinaryString(c));
            }
            myWriter.write("\n");
            for (char c : phone) {
                myWriter.write(Integer.toBinaryString(c));
            }
            myWriter.write("\n");
            for (char c : email) {
                myWriter.write(Integer.toBinaryString(c));
            }
            myWriter.write("\n");
            for (char c : dateOfBirth) {
                myWriter.write(Integer.toBinaryString(c));
            }
            myWriter.write("\n");
            for (char c : gender) {
                myWriter.write(Integer.toBinaryString(c));
            }
            myWriter.write("\n");
        }
        myWriter.close();
    }

}
