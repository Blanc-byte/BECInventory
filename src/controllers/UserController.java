/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.User;
import models.equipmentModel;
import models.reservationModel;
import models.borrowModel;

public class UserController {
    final String DB_URL = "jdbc:mysql://localhost/becinventory";
    final String USER = "root";
    final String PASS = "";
    public int tableToggle = 1;
    public String url;
    public Connection con = null;

    @FXML private Button borrowButton, reserveButton, reserveListButton, profileButton, editProfileButton, saveProfileButton;
    @FXML private TextField searchEquipment, searchBorrow;
    @FXML private TableView<equipmentModel> equipmentTable;
    @FXML private TableView<reservationModel> itemReservedTable;
    @FXML private TableView<borrowModel> BorrowTable;
    @FXML private ObservableList<equipmentModel> equipmentList = FXCollections.observableArrayList();
    @FXML private ObservableList<reservationModel> reservationList = FXCollections.observableArrayList();
    @FXML private ObservableList<borrowModel> borrowList = FXCollections.observableArrayList();
    @FXML private Pane reserve, borrow, profile, itemTablePane, itemReserveTablePane, profilePane, editProfilePane;
    @FXML private Label nameLabel, info;

    @FXML
    private void initialize() throws Exception {
        connect();
        loadData();
        setupInitialUI();
    }
    
    private void setupInitialUI() {
        reserve.setVisible(true);
        itemTablePane.setVisible(true);
        itemReserveTablePane.setVisible(false);
        borrow.setVisible(false);
        profile.setVisible(false);
        genderEditField.setItems(genders);
        yearEditField.setItems(years);
        sectionEditField.setItems(sections);
        depEditField.setItems(courses);
    }

    @FXML
    private void reserveClick() throws Exception{
        loadData();
        togglePaneVisibility(true, false, false);
        itemTablePane.setVisible(true);
        itemReserveTablePane.setVisible(false);
        reserveListButton.setText("Reserved");
    }

    @FXML
    private void BorrowClick() throws Exception{
        loadData();
        togglePaneVisibility(false, true, false);
    }
    
    @FXML 
    private void profileClick() throws Exception{
        loadData();
        togglePaneVisibility(false, false, true);
        profilePane.setVisible(true);
        editProfilePane.setVisible(false);
        updateStackPane(borrowStack, "/icons/borrow.png", countStatus("borrow")); // Example: 5 items borrowed
        updateStackPane(reserveStack, "/icons/reserve.png", countStatus("reserve")); // Example: 3 items reserved
    }
    
     private void togglePaneVisibility(boolean reserveVisible, boolean borrowVisible, boolean profileVisible) {
        reserve.setVisible(reserveVisible);
        borrow.setVisible(borrowVisible);
        profile.setVisible(profileVisible);
    }
    
    @FXML
    private void reserveListClick() throws Exception {
        boolean isReserved = "Reserved".equals(reserveListButton.getText());
        reserveListButton.setText(isReserved ? "Available" : "Reserved");
        info.setText(isReserved ? "Currently Reserved Items" : "Available Items to be Borrowed");
        itemTablePane.setVisible(!isReserved);
        itemReserveTablePane.setVisible(isReserved);
        loadData();
    }
    
    @FXML
    private void editProfileClick() throws Exception {
        loadData();
        profilePane.setVisible(false);
        editProfilePane.setVisible(true);
        setEditProfileField();
    }
    
     @FXML
    private void logOut(ActionEvent event) throws IOException {
        switchToLoginScene(event);
        clearLoginData();
    }

    private void switchToLoginScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/auth/auth.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
        ((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()).close();
    }

    private void clearLoginData() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("id.txt"))) {
            writer.write("");
        }
    }
    
    
    //  Dynamic Searh
    ObservableList<equipmentModel> filteredListEquipment = FXCollections.observableArrayList();
    ObservableList<reservationModel> filteredListReservation = FXCollections.observableArrayList();
    ObservableList<borrowModel> filteredListBorrow = FXCollections.observableArrayList();
    
    // CheckBox
    ObservableList<String> years = FXCollections.observableArrayList("1", "2", "3", "4");
    ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
    ObservableList<String> sections = FXCollections.observableArrayList("A", "B", "C", "D", "E", "F");
    ObservableList<String> courses = FXCollections.observableArrayList("BSIT", "BSBA", "BSA", "BTLEd");
    
    public void search() {
        if(reserveListButton.getText().equals("Reserved")){
          searchMeEquipment();
        }
        else if(reserveListButton.getText().equals("Available")){
          searchMeReservation();
        }
    }
    public void searchMeEquipment(){
        String searchTerm = searchEquipment.getText().toLowerCase();
        // Clear the current filtered list
        filteredListEquipment.clear();

        // Filter the borrowedEquipments list based on the search term
        for (equipmentModel equipment : equipmentList) {
            if (matchesSearchEquipment(equipment, searchTerm)) {
                filteredListEquipment.add(equipment); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        equipmentTable.setItems(filteredListEquipment);
    }
    // Check if the BorrowedEquipments matches the search term
    private boolean matchesSearchEquipment(equipmentModel equipment, String searchTerm) {
        return equipment.getId().toLowerCase().contains(searchTerm) ||
               equipment.getName().toLowerCase().contains(searchTerm) ||
               equipment.getStock().toLowerCase().contains(searchTerm) ||
               equipment.getAvailable().toLowerCase().contains(searchTerm) ||
               equipment.getDateCreated().toLowerCase().contains(searchTerm);
    }
    
    public void searchMeReservation(){
        String searchTerm = searchEquipment.getText().toLowerCase();
        // Clear the current filtered list
        filteredListReservation.clear();

        // Filter the borrowedEquipments list based on the search term
        for (reservationModel reservation : reservationList) {
            if (matchesSearchReservation(reservation, searchTerm)) {
                filteredListReservation.add(reservation); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        itemReservedTable.setItems(filteredListReservation);
    }
    private boolean matchesSearchReservation(reservationModel reservation, String searchTerm) {
        return reservation.getReservationId().toLowerCase().contains(searchTerm) ||
               reservation.getUserName().toLowerCase().contains(searchTerm) ||
               reservation.getEquipmentName().toLowerCase().contains(searchTerm) ||
               reservation.getQuantity().toLowerCase().contains(searchTerm) ||
               reservation.getDateReserve().toLowerCase().contains(searchTerm);
    }
    
     public void searchMeBorrow(){
        String searchTerm = searchBorrow.getText().toLowerCase();
        // Clear the current filtered list
        filteredListBorrow.clear();

        // Filter the borrowedEquipments list based on the search term
        for (borrowModel borrow : borrowList) {
            if (matchesSearchBorrow(borrow, searchTerm)) {
                filteredListBorrow.add(borrow); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        BorrowTable.setItems(filteredListBorrow);
    }
     
     private boolean matchesSearchBorrow(borrowModel borrow, String searchTerm) {
         return borrow.getId().toLowerCase().contains(searchTerm) ||
               borrow.getUserName().toLowerCase().contains(searchTerm) ||
               borrow.getEquipmentName().toLowerCase().contains(searchTerm) ||
               borrow.getSerial_id().toLowerCase().contains(searchTerm) ||
               borrow.getStatus().toLowerCase().contains(searchTerm) ||
               borrow.getBorrowed_at().toLowerCase().contains(searchTerm);
     }
    
    public void getRequest()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM equipments");
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("name");
            String iii = resultSet.getString("stock");
            String iiii = resultSet.getString("available");   
            String iiiii = resultSet.getString("date_created");
            
            equipmentModel equipment = new equipmentModel(i, ii, iii, iiii, iiiii);
            equipmentList.add(equipment);
        }
    }
    public void getReserveRequest(String userId)throws Exception{
        String query = """
            SELECT 
                r.id AS reservation_id,
                u.fname AS user_name,
                e.name AS equipment_name,
                r.quantity,
                r.date_reserve
            FROM 
                reservation r
            JOIN 
                users u ON r.user_id = u.id
            JOIN 
                equipments e ON r.equipment_id = e.id
            WHERE
                u.id = ?;
         """;

        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Set the user ID in the query
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Extract the values from the ResultSet
                String reservationId = resultSet.getString("reservation_id");
                String userName = resultSet.getString("user_name");
                String equipmentName = resultSet.getString("equipment_name");
                String quantity = resultSet.getString("quantity");
                String dateReserve = resultSet.getString("date_reserve");

                // Create a model for each row and add it to the list
                reservationModel reservation = new reservationModel(
                    reservationId,
                    userName,
                    equipmentName,
                    quantity,
                    dateReserve
                );
                reservationList.add(reservation); // Assuming `equipmentList` is your data list
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching reserve requests: " + e.getMessage());
        }
    }
    public void getBorrowRequest(String userId)throws Exception {
        String query = """
            SELECT 
                b.id AS borrow_id,
                u.fname AS user_name,
                e.name AS equipment_name,
                b.serial_id AS serial_id,
                b.borrowed_at AS borrowDate,
                b.status AS status
            FROM 
                borrow b
            JOIN 
                users u ON b.user_id = u.id
            JOIN 
                equipments e ON b.equipment_id = e.id
            WHERE
                u.id = ?;
        """;
        
        try (PreparedStatement statement = con.prepareStatement(query)) {
            // Set the user ID in the query
            statement.setString(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Extract the values from the ResultSet
                String borrowId = resultSet.getString("borrow_id");
                String userName = resultSet.getString("user_name");
                String equipmentName = resultSet.getString("equipment_name");
                String serial_id = resultSet.getString("serial_id");
                String dateReserve = resultSet.getString("borrowDate");
                String status = resultSet.getString("status");

                // Create a model for each row and add it to the list
                borrowModel borrow = new borrowModel(
                    borrowId,
                    userName,
                    equipmentName,
                    serial_id,
                    dateReserve,
                    status
                );
                borrowList.add(borrow);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching reserve requests: " + e.getMessage());
        }
    }
    
    @FXML private TableColumn<equipmentModel, String> itemColumn, quantityColumn, actionColumn;
    public void loadRequestsToTable() throws Exception {  
        equipmentList.clear();
        itemColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
        actionColumn.setCellValueFactory(param -> new SimpleStringProperty("Action"));  // Optional: Set placeholder text
    
    // Define the button inside the action column
    actionColumn.setCellFactory(param -> new TableCell<equipmentModel, String>() {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null); // Remove graphic when the row is empty
        } else {
            equipmentModel equipment = getTableView().getItems().get(getIndex());
            int availableStock = Integer.parseInt(equipment.getAvailable());

            if (availableStock > 0) {
                Button reserveButton = new Button();

                // Load the image
                Image reserveImage = new Image(getClass().getResourceAsStream("/icons/reserve.png"));
                ImageView imageView = new ImageView(reserveImage);

                // Set the image size
                imageView.setFitWidth(16); // Set desired width
                imageView.setFitHeight(16); // Set desired height

                // Set the image as the graphic for the button
                reserveButton.setGraphic(imageView);

                // Add action listener for the button
                reserveButton.setOnAction(event -> {
                    reserveEquipment(equipment); // Call a method to handle the reserve action
                });

                setGraphic(reserveButton); // Set the button as the cell graphic
            } else {
                setGraphic(null); // Remove graphic if stock is unavailable
            }
        }
    }
    });
        
        getRequest();
        equipmentTable.setItems(equipmentList);
        
    }
    
    @FXML private TableColumn<reservationModel, String> itemReserveColumn, quantityReserveColumn, dateColumn;
    public void loadRequestsToReserve() throws Exception {
          User loggedInUser = fetchLoggedInUser();
          reservationList.clear();
          itemReserveColumn.setCellValueFactory(cellData -> cellData.getValue().equipmentNameProperty());
          quantityReserveColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
          dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateReserveProperty());
          
          getReserveRequest(loggedInUser.getId());
          itemReservedTable.setItems(reservationList);
          
    }
    @FXML private TableColumn<borrowModel, String> itemBorrowColumn, serialColumn, dateBorrowColumn, statusColumn;
    public void loadRequestsToBorrow() throws Exception {
         User loggedInUser = fetchLoggedInUser();
         borrowList.clear();
         itemBorrowColumn.setCellValueFactory(cellData -> cellData.getValue().equipmentNameProperty());
         serialColumn.setCellValueFactory(cellData -> cellData.getValue().serial_idProperty());
         dateBorrowColumn.setCellValueFactory(cellData -> cellData.getValue().borrowed_atProperty());
         statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
         
         getBorrowRequest(loggedInUser.getId());
         BorrowTable.setItems(borrowList);
    }
    
    public void loadData () throws Exception{
        loadRequestsToTable();
        loadRequestsToReserve();
        loadRequestsToBorrow();
        loadUser();
        updateStackPane(borrowStack, "/icons/borrow.png", countStatus("borrow")); 
        updateStackPane(reserveStack, "/icons/reserve.png", countStatus("reserve"));
    }
    
    private void reserveEquipment(equipmentModel equipment){
        String equipmentId = equipment.getId();
        String equipmentName = equipment.getName();
        String quantityStr = JOptionPane.showInputDialog("Enter the quantity to reserve:");
        
            if (quantityStr == null) {
                return;
            }

            if (quantityStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Quantity cannot be empty!");
                return; 
            }
        // Validate if the quantity is a valid integer
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity must be greater than zero!");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid quantity! Please enter a valid number.");
            return; // If input is not a valid integer, exit the method
        }
        
        int available = Integer.parseInt(equipment.getAvailable());
        if(quantity > available){
            JOptionPane.showMessageDialog(null, 
                    "Error: You are trying to reserve " + quantity + " items, but only " + available + " are available.");
            return;
        }
         updateDatabaseWithReservation(equipmentId, equipmentName, quantity);
    }
    
    // Method to update the database with the reservation details
    private void updateDatabaseWithReservation(String equipmentId, String equipmentName, int quantity) {
        User loggedInUser = fetchLoggedInUser();
        String updateQuery = "UPDATE equipments " +
                             "SET available = available - ? " +
                             "WHERE id = ? AND available >= ?";
        String insertReserveQuery = "INSERT INTO reservation (user_id, equipment_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            stmt.setInt(1, quantity);       
            stmt.setString(2, equipmentId); 
            stmt.setInt(3, quantity);   

            // Execute the update
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Successfully reserved " + quantity + " of Equipment: " + equipmentName);

                try (PreparedStatement insertStmt = con.prepareStatement(insertReserveQuery)) {
                    insertStmt.setString(1, loggedInUser.getId());
                    insertStmt.setString(2, equipmentId);     
                    insertStmt.setInt(3, quantity);            
                    insertStmt.executeUpdate();                   
                }

                // Update UI or reload tables
                equipmentTable.getItems().clear();
                itemReservedTable.getItems().clear();
                loadData();

            } else {
                // If no rows were updated, it means insufficient stock
                JOptionPane.showMessageDialog(null, "The item you tried to reserve is no longer available in the requested quantity.");
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reserving equipment: " + e.getMessage());
        }
    }

    
    @FXML
    private void updateUserDatabase() {
        User loggedInUser = fetchLoggedInUser();
        String pass;
        if (!validateFields()) {
            return; // Stop execution if validation fails
        }
        
        String yearSection = "", departments = "";
        if(loggedInUser.getRole().equals("student")){
           yearSection = yearEditField.getValue()+""+sectionEditField.getValue();
           departments = depEditField.getValue();
       }else if(loggedInUser.getRole().equals("staff")){
           yearSection ="N/A";
           departments = "N/A";
       }
        
         String updateQuery = "UPDATE `users` SET `fname`=?, `mname`=?, `lname`=?, `year_section`=?, `department`=?, `suffix`=?, `gender`=?, `school_id`=?, `username`=?, `password`=? WHERE id = ? ";
         try (PreparedStatement stmt = con.prepareStatement(updateQuery)) {
            
            stmt.setString(1, fnameField.getText());
            stmt.setString(2, mnameField.getText());
            stmt.setString(3, lastNameField.getText());
            stmt.setString(4, yearSection);
            stmt.setString(5,departments);
            stmt.setString(6, suffixField.getText() != null ? suffixField.getText() : ""); // Handle null suffix
            stmt.setString(7, genderEditField.getValue()); // Get selected value from ChoiceBox
            stmt.setString(8, idEditField.getText());
            stmt.setString(9, usernameEditField.getText());
            stmt.setString(10, confirmPasswordField.getText().isEmpty() ? loggedInUser.getPassword() : confirmPasswordField.getText());
            stmt.setString(11, loggedInUser.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Successfully updated Profile");
                loadUser();
                profilePane.setVisible(true);
                editProfilePane.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "No Found found ");
            }
            
         } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Error Updating user: " + e.getMessage());
         }
    }

    private boolean validateFields() {
        
        User loggedInUser = fetchLoggedInUser();
        // Check if required fields are empty
        if (fnameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
             idEditField.getText().isEmpty() || usernameEditField.getText().isEmpty()) {
             JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
            return false;
        }
        
        if (!fnameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "First name must contain only letters, no numbers or special characters.");
            return false;
        }
        if (!lastNameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Last name must contain only letters, no numbers or special characters.");
            return false;
        }
        if (!mnameField.getText().isEmpty() && !mnameField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "middle name must contain only letters, no numbers or special characters.");
            return false;
        }
        if (!suffixField.getText().isEmpty() && !suffixField.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Suffix must contain only letters, no numbers or special characters.");
            return false;
        }
        
        if (genderEditField.getValue() == null || genderEditField.getValue().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a gender.");
                return false;
        }

        if(!loggedInUser.getRole().equals("staff")){
            
            // Check if year is selected
            if (yearEditField.getValue() == null || yearEditField.getValue().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select Year.");
                return false;
            }
            // Check if section is selected
            if (sectionEditField.getValue() == null || sectionEditField.getValue().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select Section.");
                return false;
            }
            // Check if section is selected
            if (depEditField.getValue() == null || depEditField.getValue().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select Course.");
                return false;
            }
        }

        // Optional: Validate numeric fields (e.g., school ID)
        // Validate school ID format (e.g., 0000-0000)
        String idText = idEditField.getText().trim();
        if (!idText.matches("\\d{4}-\\d{4}")) {
            JOptionPane.showMessageDialog(null, "School ID must be in the format 0000-0000 (e.g., 1234-5678).");
            return false;
        }
        
        // Check if school ID is unique
        if (!isSchoolIdUnique(idEditField.getText())) {
            JOptionPane.showMessageDialog(null, "The School ID is already in use. Please provide a unique School ID.");
            return false;
        }
        
        if(!isUsernameUnique( usernameEditField.getText())) {
            JOptionPane.showMessageDialog(null, "The Username is already in use. Please provide a username.");
        }

        // Validate password
        // Check if either password or confirm password is empty
        if (passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty()) {
            if (passwordField.getText().isEmpty() && confirmPasswordField.getText().isEmpty()) {
            // Keep the old password if both fields are empty
            } else {
                JOptionPane.showMessageDialog(null, "Both password fields must be filled.");
                return false;
            }
        }
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            JOptionPane.showMessageDialog(null, "Password didn't match.");
            return false;
        }


        // All validations passed
        return true;
    }
    
    private boolean isSchoolIdUnique(String schoolId) {
        User loggedInUser = fetchLoggedInUser();
        String query = "SELECT COUNT(*) FROM users WHERE school_id = ? AND id != ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, schoolId);
                statement.setString(2, loggedInUser.getId());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0; // School ID is unique if count is 0
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false; // Return false if an error occurs
    }
    private boolean isUsernameUnique(String username) {
        User loggedInUser = fetchLoggedInUser();
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND id != ?";
            try (PreparedStatement statement = con.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, loggedInUser.getId());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count == 0; // School ID is unique if count is 0
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false; // Return false if an error occurs
    }

    
    private String readUserIdFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/src/id.txt"))) {
            return reader.readLine();
        } catch (IOException e) {
            System.err.println("Error reading id.txt: " + e.getMessage());
            return null;
        }
    }
    /**
     * Fetches the currently logged-in user based on the ID stored in the text file.
     * 
     * @return UserModel object containing user details or null if no user found.
     */
    public User fetchLoggedInUser() {
        String userId = readUserIdFromFile();
        if (userId == null) {
            System.err.println("Failed to read user ID from file.");
            return null;
        }

        return fetchUserFromDatabase(userId);
    }

    private User fetchUserFromDatabase(String userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                    resultSet.getString("id"),
                    resultSet.getString("school_id"),
                    resultSet.getString("fname"),
                    resultSet.getString("mname"),
                    resultSet.getString("lname"),
                    resultSet.getString("suffix"),
                    resultSet.getString("gender"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("department"),
                    resultSet.getString("year_section"),
                    resultSet.getString("role")
                );
            }
        } catch (Exception e) {
            System.err.println("Error fetching user from database: " + e.getMessage());
        }
        return null;
    }
    
    @FXML private TextField userNameField, idField, genderField, departmentField, yearField;
    @FXML private Label depLabel, yearLabel;
    public void loadUser() {
         User loggedInUser = fetchLoggedInUser();
        if (loggedInUser != null && loggedInUser.getRole().equals("student")) {
            nameLabel.setText("Welcome, "+ loggedInUser.getFname());
            userNameField.setText(loggedInUser.getFname()+" "+loggedInUser.getMname()+" "+loggedInUser.getLname()+" "+loggedInUser.getSuffix());
            idField.setText(loggedInUser.getSchoolId());
            genderField.setText(loggedInUser.getGender());
            departmentField.setText(loggedInUser.getDepartment());
            yearField.setText(loggedInUser.getYearSection());
            passwordField.setText("");
            confirmPasswordField.setText("");
        } else if(loggedInUser != null && loggedInUser.getRole().equals("staff")) {
            nameLabel.setText("Welcome, "+ loggedInUser.getFname());
            userNameField.setText(loggedInUser.getFname()+" "+loggedInUser.getMname()+" "+loggedInUser.getLname()+" "+loggedInUser.getSuffix());
            idField.setText(loggedInUser.getSchoolId());
            genderField.setText(loggedInUser.getGender());
            departmentField.setVisible(false);
            yearField.setVisible(false);
            depLabel.setVisible(false);
            yearLabel.setVisible(false);
        }
    }
    
    @FXML private TextField fnameField, mnameField, lastNameField, suffixField, idEditField, usernameEditField, passwordField, confirmPasswordField;
    @FXML private ChoiceBox<String> genderEditField, sectionEditField, yearEditField, depEditField;
    public void setEditProfileField() {
        User loggedInUser = fetchLoggedInUser();
        if (loggedInUser != null) {
            fnameField.setText(loggedInUser.getFname()+"");
            mnameField.setText(loggedInUser.getMname()+"");
            lastNameField.setText(loggedInUser.getLname()+"");
            suffixField.setText(loggedInUser.getSuffix()+"");
            
            String yearSection = loggedInUser.getYearSection(); 
            String year = yearSection.replaceAll("[^0-9]", ""); 
            String section = yearSection.replaceAll("[^A-Za-z]", "");
            
            if(loggedInUser.getRole().equals("staff")){
                 depEditField.setDisable(true);
                 sectionEditField.setDisable(true);
                 yearEditField.setDisable(true);
            }else {
                if (loggedInUser.getDepartment() != null && courses.contains(loggedInUser.getDepartment())) {
                depEditField.setValue(loggedInUser.getDepartment()); // Display the fetched gender
                } else {
                    depEditField.setValue("BSIT"); // Default value if none is found
                }

                if ((loggedInUser.getYearSection() != null && sections.contains(section)) && loggedInUser.getYearSection() != null && years.contains(year) ) {
                    sectionEditField.setValue(section);
                    yearEditField.setValue(year);

                } else {
                    sectionEditField.setValue("A"); 
                    yearEditField.setValue("1");
                }
            }

            
            if (loggedInUser.getGender() != null && genders.contains(loggedInUser.getGender())) {
            genderEditField.setValue(loggedInUser.getGender()); // Display the fetched gender
            } else {
                genderEditField.setValue("Male"); // Default value if none is found
            }
            
            
            idEditField.setText(loggedInUser.getSchoolId()+"");
            usernameEditField.setText(loggedInUser.getUsername()+"");
        } else {
            System.out.println("No user is logged in.");
        }
    }
    
//    Counter for borrow and reserve
    
    @FXML private StackPane borrowStack, reserveStack;
    // Method to dynamically update the StackPane
    private void updateStackPane(StackPane stackPane, String iconPath, int count) {
        // Clear previous children (if any)
        stackPane.getChildren().clear();

        // Icon Image
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitWidth(30); // Adjust as needed
        icon.setFitHeight(30);

        // Badge Counter
        Label badge = new Label(String.valueOf(count));
        badge.getStyleClass().add("badge"); // Use CSS for badge styling

        // Add Icon and Badge to the StackPane
        stackPane.getChildren().addAll(icon, badge);

        // Position the badge at the top-right corner
        StackPane.setMargin(badge, new Insets(-10, -10, 0, 0)); // Adjust positioning
        StackPane.setAlignment(badge, Pos.TOP_RIGHT); // Top-right alignment
    }
    
   public Integer countStatus(String whois) {
        int count = 0;
        User loggedInUser = fetchLoggedInUser(); // Fetch the logged-in user

        // Validate the `whois` input
        if (!whois.equals("borrow") && !whois.equals("reserve")) {
            throw new IllegalArgumentException("Invalid parameter for whois: " + whois);
        }

        // Determine the query based on `whois`
        String query;
        if (whois.equals("borrow")) {
            query = "SELECT COUNT(*) AS count " +
                    "FROM borrow " +
                    "WHERE user_id = ? AND status = 'borrowed';";
        } else {
            query = "SELECT SUM(quantity) AS count " +
                    "FROM reservation " +
                    "WHERE user_id = ?;";
        }

        // Execute the query
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, loggedInUser.getId()); // Set the user_id dynamically
            ResultSet resultSet = statement.executeQuery();

            // Extract the count from the result set
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return count; // Return the valid count or 0 if an error occurred
    }



    public Connection connect() {
        url = "jdbc:mysql://localhost:3306/becinventory";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error connecting to database.");
        }
        return con;
    }
}
