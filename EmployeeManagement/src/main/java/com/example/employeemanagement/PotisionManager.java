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

public class PotisionManager implements Initializable {
    @FXML
    protected TableView<Employee> employeeTableView = new TableView<>();

    @FXML
    private TableColumn<Employee, String> columnId = new TableColumn<>();

    @FXML
    private TableColumn<Employee, String> columnName = new TableColumn<>();

    @FXML
    private TableColumn<Employee, String> columnEmail = new TableColumn<>();

    @FXML
    private TableColumn<Employee, String> columnPosition = new TableColumn<>();

    @FXML
    private TableColumn<Employee, String> columnGender = new TableColumn<>();
    @FXML
    private  TableColumn<Employee, Float> columnSalary = new TableColumn<>();

    @FXML
    TextField searchText = new TextField();

    @FXML
    ImageView imageEdit = new ImageView();
    @FXML
    ImageView imageSearch = new ImageView();
    @FXML
    ImageView imageRemove = new ImageView();
    @FXML
    ImageView imageReload = new ImageView();


    //ObservableList: Một danh sách cho phép người nghe theo dõi các thay đổi khi chúng xảy ra
    ObservableList<Employee> observableList = FXCollections.observableArrayList();

    Employee currentEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            updateTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handEdit(javafx.scene.input.MouseEvent mouseEvent) {
        showDialog("editPosition");
    }

    public void handSearch(javafx.scene.input.MouseEvent mouseEvent) {
        observableList.clear();
        searchEmployees();
    }

    public void handRemove(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        selectRemoveEmployee();
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
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        columnSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        getDataEmployees();
        employeeTableView.setItems(observableList);
    }

    public void searchEmployees() {
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM empoylees WHERE id like ? or fullName like ? or position like ? or gender like ? or salary >= ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchText.getText() + "%");
            statement.setString(2, "%" + searchText.getText() + "%");
            statement.setString(3, "%" + searchText.getText() + "%");
            statement.setString(4, "%" + searchText.getText() + "%");
            statement.setFloat(5, Float.parseFloat(searchText.getText()));
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

    public void selectRemoveEmployee() {
        for (Employee employee : employeeTableView.getSelectionModel().getSelectedItems()) {
            currentEmployee = new Employee(employee.getId(), employee.getName(), employee.getPhone(), employee.getEmail(),
                    employee.getDateOfBirth(), employee.getGender(), employee.getPosition(), employee.getSalary());
            System.out.println(1);
        }
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "UPDATE empoylees SET position = NULL, salary = NULL WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, currentEmployee.getId());
            statement.executeUpdate();
            statement.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> getEmployeesPosition(){
        ArrayList<Employee> employees = new ArrayList<>();
        try {
            Connection connection = MySqlConnection.getMySQLConnection();
            String query = "SELECT * FROM empoylees WHERE position IS NOT NULL";
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
        FileWriter myWriter = new FileWriter("employeePosition.txt");
        ArrayList<Employee> employees = getEmployeesPosition();
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
