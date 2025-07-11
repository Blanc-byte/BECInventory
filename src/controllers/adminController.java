/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.BorrowedEquipments;
import models.RemarkModel;
import models.StockRecord;
import models.User;
import models.equipmentModel;
import models.reservation;
import models.serialModel;

/**
 *
 * @author Administrator
 */
public class adminController {
    final String DB_URL = "jdbc:mysql://localhost/becinventory";
    final String USER = "root";
    final String PASS = "";
    public String url;
    public Connection con = null;
    
    @FXML private TextField search,historySearch, searchReservation, searchEquipment, searchUser, searchField;//, nameOfEquipment, stocksAvailable
    @FXML private Pane userPane;
//    @FXML private Button add, save;
    @FXML private PasswordField password;
    public void initialize()throws Exception{
        connect();
        loadBorrowersTable();
        loadReservationTable();
        loadEquipmentsTable();
        loadEquipmentsTable2();
        loadUsersTable();
        loadHistoryTable();
        setDesign();
        fillUserDetails();
        productionLoad();

    }
    @FXML
    private void handleSearchEquipment(){
        String filter = searchField.getText().toLowerCase().trim();
        FilteredList<equipmentModel> filteredData = new FilteredList<>(Equipments, p -> true);
        SortedList<equipmentModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(equipmentsTable.comparatorProperty());
        equipmentsTable.setItems(sortedData);
        filteredData.setPredicate(equipment -> {
            if (filter.isEmpty()) {
                return true;
            }
            return equipment.getName().toLowerCase().contains(filter)
                || equipment.getStock().toLowerCase().contains(filter)
                || equipment.getAvailable().toLowerCase().contains(filter)
                || equipment.getDateCreated().toLowerCase().contains(filter)
                || equipment.getStockRoom().toLowerCase().contains(filter);
        });
    }
    @FXML
    private void handleAddEquipment() throws Exception{
        // 1. Create custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Equipment");

        // 2. Create input fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label stockRoomLabel = new Label("Stock Room:");
        ComboBox<Integer> stockRoomCombo = new ComboBox<>();
        stockRoomCombo.getItems().addAll(1, 2);
        stockRoomCombo.getSelectionModel().selectFirst();

        // 3. Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(stockRoomLabel, 0, 1);
        grid.add(stockRoomCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // 4. Add buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // 5. Show dialog and handle result
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = nameField.getText().trim();
            int stockRoom = stockRoomCombo.getValue();

            if (name.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Name cannot be empty.");
                return;
            }

            insertEquipment(name, stockRoom);
            loadEquipmentsTable2();
        }
    }
        
    private void insertEquipment(String name, int stockRoom) throws Exception {
        java.sql.Statement statement = con.createStatement();

        // 1️⃣ Check if the equipment name already exists
        ResultSet rs = statement.executeQuery(
            "SELECT COUNT(*) AS count FROM equipments WHERE name = '" + name + "'"
        );

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("count");
        }

        rs.close();

        if (count > 0) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Equipment",
                      "An equipment with this name already exists in the database!");
        } else {
            // 2️⃣ Insert if not exists
            statement.executeUpdate(
                "INSERT INTO equipments (name, stockRoom) VALUES ('" + name + "', " + stockRoom + ")",
                statement.RETURN_GENERATED_KEYS 
            );
            // ✅ Get the new equipment ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int newEquipmentID = -1;
            if (generatedKeys.next()) {
                newEquipmentID = generatedKeys.getInt(1);
            }
            generatedKeys.close();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                      "Equipment added successfully! Now add inventory details.");

            statement.close();

            // ✅ Open dialog for additional inventory details
            addInventoryDetails(newEquipmentID);
//            showAlert(Alert.AlertType.INFORMATION, "Success",
//                      "Equipment added successfully!");
        }
        statement.close();
    }
    private void addInventoryDetails(int equipmentID) throws Exception {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Inventory Details");
        dialog.setHeaderText("Enter inventory info for equipment ID: " + equipmentID);

        // Layout with input fields
        Label propLabel = new Label("Property No:");
        TextField propField = new TextField();

        Label descLabel = new Label("Description:");
        TextField descField = new TextField();

        Label serialLabel = new Label("Serial No:");
        TextField serialField = new TextField();

        Label dateLabel = new Label("Date Purchased (YYYY-MM-DD):");
        TextField dateField = new TextField();

        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();

        Label parLabel = new Label("PAR No:");
        TextField parField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(propLabel, 0, 0); grid.add(propField, 1, 0);
        grid.add(descLabel, 0, 1); grid.add(descField, 1, 1);
        grid.add(serialLabel, 0, 2); grid.add(serialField, 1, 2);
        grid.add(dateLabel, 0, 3); grid.add(dateField, 1, 3);
        grid.add(amountLabel, 0, 4); grid.add(amountField, 1, 4);
        grid.add(parLabel, 0, 5); grid.add(parField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Get values
            String propertyNum = propField.getText().trim();
            String description = descField.getText().trim();
            String serialNum = serialField.getText().trim();
            String datePurchased = dateField.getText().trim();
            String amount = amountField.getText().trim();
            String parNum = parField.getText().trim();

            // ✅ Insert into inventory
            java.sql.Statement statement = con.createStatement();
            String insertInventory = String.format(
                "INSERT INTO inventory (equipment_id, date_purchased, property_num, description, serial_num, amount, par_num) "
              + "VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s')",
                equipmentID, datePurchased, propertyNum, description, serialNum, amount, parNum
            );
            statement.executeUpdate(insertInventory);
            statement.close();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Inventory details added!");
        }
    }




    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void productionLoad()throws Exception{
        homeClick();
        navigation.setVisible(true);
    }
    @FXML private TextField d1,d2,d3,d4,d5,d9,d10;
    @FXML private ChoiceBox d6,d7,d8;
    public void addBorrower()throws Exception{
        java.sql.Statement statement = con.createStatement();
        if(d8.getValue() != null){
            if(d8.getValue().equals("faculty")){
                if(!(
                        d2.getText().equals("") || d4.getText().equals("") || 
                        d8.getValue() == null 
                )){
                    statement.executeUpdate(""+"INSERT INTO `users`(`fname`, `mname`, `lname`, `suffix`, `gender`, `role`)"
                            + "VALUES ("
                            + "'"+d2.getText()+"',"
                            + "'"+d3.getText()+"',"
                            + "'"+d4.getText()+"',"
                            + "'"+d5.getText()+"',"
                            + "'"+d6.getValue()+"',"
                            + "'"+d8.getValue()+"'"
                            + ")");
                    d1.setText("");d2.setText("");d3.setText("");d4.setText("");d5.setText("");d9.setText("");d10.setText("");
                    d6.setValue("");d7.setValue("");d8.setValue("");
                    JOptionPane.showMessageDialog(null, "SUCCESSFULLY ADDED");
                    addBorrowersPane.setVisible(false);borrow.setVisible(true);
                    loadBorrowersTable();

                }else{
                    JOptionPane.showMessageDialog(null, "COMPLETE THE DETAILS");
                }
            }else{
                if(!(
                        d1.getText().equals("") || d2.getText().equals("") || d4.getText().equals("") || 
                        d9.getText().equals("") || d10.getText().equals("") ||
                        d6.getValue() == null || d7.getValue() == null || d8.getValue() == null 
                )){
                    statement.executeUpdate(""+"INSERT INTO `users`(`school_id`, `fname`, `mname`, `lname`, `suffix`, `gender`, `department`, `year_section`, `role`)"
                            + "VALUES ("
                            + "'"+d1.getText()+"',"
                            + "'"+d2.getText()+"',"
                            + "'"+d3.getText()+"',"
                            + "'"+d4.getText()+"',"
                            + "'"+d5.getText()+"',"
                            + "'"+d6.getValue()+"',"
                            + "'"+d7.getValue()+"',"
                            + "'"+d9.getText()+d10.getText()+"',"
                            + "'"+d8.getValue()+"'"
                            + ")");
                    d1.setText("");d2.setText("");d3.setText("");d4.setText("");d5.setText("");d9.setText("");d10.setText("");
                    d6.setValue("");d7.setValue("");d8.setValue("");
                    JOptionPane.showMessageDialog(null, "SUCCESSFULLY ADDED");
                    addBorrowersPane.setVisible(false);borrow.setVisible(true);
                    loadBorrowersTable();

                }else{
                    JOptionPane.showMessageDialog(null, "COMPLETE THE DETAILS");
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "COMPLETE THE DETAILS");
        }
        
        
    }
    ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
    ObservableList<String> dept = FXCollections.observableArrayList("BSIT", "BSA", "BSBA", "BTLEd");
    ObservableList<String> roles = FXCollections.observableArrayList("student", "faculty");
    public void fillUserDetails(){
        d6.setItems(genders);
        d7.setItems(dept);
        d8.setItems(roles);
    }
    public void addBorrowersClick(){
        userPane.setVisible(false);
        addBorrowersPane.setVisible(true);
        selectedSerials.clear();
    }
    
    String BECpassword = "dorsu-bec2024";
    public void checkPassword(){
        if(password.getText().equals(BECpassword)){
            logInPane.setVisible(false);
            navigation.setVisible(true);
            home.setVisible(true);
        }
    }
    
    
//    public void checkFirstTheStocks()throws Exception{
//        if(nameOfEquipment.getText().equals("") || stocksAvailable.getText().equals("")){
//            JOptionPane.showMessageDialog(null, "COMPLETE THE DETAILS");
//        }else{
//            java.sql.Statement statement = con.createStatement();
//            statement.executeUpdate("INSERT INTO `equipments`(`name`, `stock`, `available`) "
//                    + "VALUES ('"+nameOfEquipment.getText()+"','"+stocksAvailable.getText()+"','"+stocksAvailable.getText()+"')");
//            
//            
//            String ii = "";
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM equipments WHERE name ='"+nameOfEquipment.getText()+"'");
//            while(resultSet.next()){
//                ii = resultSet.getString("id");
//
//            }
//            
//            for(int a=0; a<Integer.parseInt(stocksAvailable.getText()); a++ ){
//                statement.executeUpdate("INSERT INTO `serial`(`serial_num`, `equipment_id`) "
//                    + "VALUES ('"+nameOfEquipment.getText()+"-0"+a+"','"+ii+"')");
//            }
//            
//            nameOfEquipment.setText("");stocksAvailable.setText("");
//            loadEquipmentsTable2();
//            JOptionPane.showMessageDialog(null, "SUCCESSFULLY ADDED");
//        }
//    }
//    public void handleQuantity() {
//        String input = stocksAvailable.getText();
//
//        if (input.matches("\\d+")) { 
//            System.out.println("Quantity: " + input);
//        } else {
//            System.out.println("Invalid input. Only numbers are allowed.");
//            stocksAvailable.clear();
//        }
//    }
    
    String availableStock = "", equipmentID="", usrID="";
    
    public void deleteEquipment()throws Exception{
        java.sql.Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM serial WHERE equipment_id = '"+equipmentID+"'");
        statement.executeUpdate("DELETE FROM equipments WHERE id = '"+equipmentID+"'");
        loadEquipmentsTable2();
    }
    
//    public void editEquipment()throws Exception{
//        java.sql.Statement statement = con.createStatement();
//        statement.executeUpdate("UPDATE equipments SET name = '"+nameOfEquipment.getText()+"', "
//                            + "stock='"+stocksAvailable.getText()+"',"
//                            + "available='"+stocksAvailable.getText()+"' WHERE id = '"+equipmentID+"'"
//        );
//        
//        
//        
//        save.setDisable(true);
//        add.setDisable(false);
//        statement.executeUpdate("DELETE FROM serial WHERE equipment_id = '"+equipmentID+"'");
//        
//        for(int a=0; a<Integer.parseInt(stocksAvailable.getText()); a++ ){
//            statement.executeUpdate("INSERT INTO `serial`(`serial_num`, `equipment_id`) "
//                + "VALUES ('"+nameOfEquipment.getText()+"-0"+a+"','"+equipmentID+"')");
//
//        }
//        nameOfEquipment.setText("");stocksAvailable.setText("");
//        loadEquipmentsTable2();
//    }
    
    boolean borrowedSuccess=true;
    public void confirmClick()throws Exception{
        updateEquipmentAndBorrow();
        loadEquipmentsTable();
        borrow.setVisible(true);
        userPane.setVisible(false);
    }
    
    public void updateEquipmentAndBorrow() throws Exception {
        java.sql.Statement statement = con.createStatement();

        // 1️⃣ Check if there are any borrowed rows for this user
        ResultSet rs = statement.executeQuery(
            "SELECT * FROM borrow WHERE user_id = '" + usrID + "' AND status = 'borrowed'"
        );

        boolean hasPendingBorrow = rs.next();
        rs.close();

        boolean proceed = true;

        if (hasPendingBorrow) {
            // ❗ Prompt admin for decision
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Pending Borrow");
            confirm.setHeaderText("This user already has borrowed equipment.");
            confirm.setContentText("Do you want to allow borrowing again?");

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirm.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == noButton) {
                proceed = false; // Admin said no
                showAlert(Alert.AlertType.INFORMATION, "Unsuccessful Transaction",
                          "Borrow canceled because the user has pending borrowed equipment.");
            }
        }

        if (proceed) {
            int left = Integer.parseInt(availableStock) - selectedSerials.size();
            statement.executeUpdate("UPDATE equipments SET available='" + left + "' WHERE id ='" + equipmentID + "'");
            getAvailSer(equipmentID);

            for (serialModel sMs : selectedSerials) {
                statement.executeUpdate("INSERT INTO borrow(user_id, equipment_id, serial_id, status) "
                                      + "VALUES ('" + usrID + "', '" + equipmentID + "', '" + sMs.getId() + "', 'borrowed')");
                statement.executeUpdate("UPDATE serial SET status='unavailable' WHERE serial_num = '" + sMs.getserialNumber() + "'");
            }

            selectedSerials.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Equipment successfully borrowed!");
        }

        statement.close();
    }

    
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> a1,a2, a3, a4, a5, a6, a7;
    @FXML private TableColumn<User, Void> a8;
    public void loadUsersTable()throws Exception{
        users.clear();
        a1.setCellValueFactory(cellData -> cellData.getValue().fnameProperty());
        a2.setCellValueFactory(cellData -> cellData.getValue().mnameProperty());
        a3.setCellValueFactory(cellData -> cellData.getValue().lnameProperty());
        a4.setCellValueFactory(cellData -> cellData.getValue().suffixProperty());
        a5.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        a6.setCellValueFactory(cellData -> cellData.getValue().yearSectionProperty());
        a7.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        
        getUsers();
        
        
        userTable.setItems(users);
        
        a8.setCellFactory(col -> new TableCell<>() {
            private final Button confirmButton = new Button();

            {
                ImageView cancelIcon = new ImageView(new Image("icons/select.png")); // Replace with your icon file
                cancelIcon.setFitHeight(23); 
                cancelIcon.setFitWidth(23);

                confirmButton.setGraphic(cancelIcon);
                confirmButton.setContentDisplay(ContentDisplay.LEFT); 
                confirmButton.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
                
                confirmButton.setOnAction(event -> {
                    User request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId());
                     
                    try {
                        usrID=request.getId();
                        confirmClick();
//                        usersss.setText(request.getFname()+" "+request.getMname()+" "+request.getLname()+" "+request.getSuffix());
                        //loadReservationTable();
//                        getRequest(); 
//                        pendingTable.setItems(requestsPending); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(confirmButton);
                }
            }
        });
    }
    
    ObservableList<User> users = FXCollections.observableArrayList();
    public void getUsers()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("fname");
            String iii = resultSet.getString("mname");
            String iiii = resultSet.getString("lname");
            String iiiii = resultSet.getString("suffix");
            String i1 = resultSet.getString("department");
            String i2 = resultSet.getString("year_section");
            String i3 = resultSet.getString("role");
            String i4 = resultSet.getString("school_id");
            
            users.add(new User(i,i4,ii,iii,iiii,iiiii,i1,i2,i3));
            
        }
    }
    ObservableList<User> filteredListUsers = FXCollections.observableArrayList();
    public void searchMeUsers(){
        String searchTerm = searchUser.getText().toLowerCase();
        // Clear the current filtered list
        filteredListUsers.clear();

        // Filter the borrowedEquipments list based on the search term
        for (User equipment : users) {
            if (matchesSearchUsers(equipment, searchTerm)) {
                filteredListUsers.add(equipment); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        userTable.setItems(filteredListUsers);

    }
    // Check if the BorrowedEquipments matches the search term
    private boolean matchesSearchUsers(User equipment, String searchTerm) {
        return equipment.getFname().toLowerCase().contains(searchTerm) ||
               equipment.getMname().toLowerCase().contains(searchTerm) ||
               equipment.getLname().toLowerCase().contains(searchTerm) ||
               equipment.getSchoolId().toLowerCase().contains(searchTerm) ||
               equipment.getSuffix().toLowerCase().contains(searchTerm) ||
               equipment.getDepartment().toLowerCase().contains(searchTerm) ||
               equipment.getYearSection().toLowerCase().contains(searchTerm) ||
               equipment.getRole().toLowerCase().contains(searchTerm);
    }
    
    
    @FXML private TableView<equipmentModel> equipmentsTable;
    @FXML private TableColumn<equipmentModel, String> s1,s2, s3, s4, stockRoom;
    @FXML private TableColumn<equipmentModel, Void> s5;
    public void loadEquipmentsTable2()throws Exception{
        Equipments.clear();
        s1.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        s2.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        s3.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
        s4.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        stockRoom.setCellValueFactory(cellData -> cellData.getValue().stockRoomProperty());
        
        getEquipments();
        
        
        equipmentsTable.setItems(Equipments);
        
        s5.setCellFactory(col -> new TableCell<>() {
            private final Button viewButton = new Button();
            private final Button editButton = new Button();
            private final Button detailsButton = new Button();
            private final Button addStockButton = new Button();
            private final HBox buttonContainer = new HBox(5);

            {
                ImageView cancelIcon0 = new ImageView(new Image("icons/magnifying-glass.png")); // Replace with your icon file
                cancelIcon0.setFitHeight(23); 
                cancelIcon0.setFitWidth(23);
                
                viewButton.setGraphic(cancelIcon0);
                viewButton.setContentDisplay(ContentDisplay.LEFT); 
                viewButton.setStyle("-fx-background-color:transparent;");
                
                ImageView cancelIcon = new ImageView(new Image("icons/edit.png")); // Replace with your icon file
                cancelIcon.setFitHeight(23); 
                cancelIcon.setFitWidth(23);
                
                editButton.setGraphic(cancelIcon);
                editButton.setContentDisplay(ContentDisplay.LEFT); 
                editButton.setStyle("-fx-background-color:transparent;");
                
                ImageView cancelIcon2 = new ImageView(new Image("icons/reserve.png")); // Replace with your icon file
                cancelIcon2.setFitHeight(23); 
                cancelIcon2.setFitWidth(23);
                
                detailsButton.setGraphic(cancelIcon2);
                detailsButton.setContentDisplay(ContentDisplay.LEFT); 
                detailsButton.setStyle("-fx-background-color:transparent;");
                
                ImageView cancelIcon3 = new ImageView(new Image("icons/add.png")); // Replace with your icon file
                cancelIcon3.setFitHeight(60); 
                cancelIcon3.setFitWidth(60);
                
                addStockButton.setGraphic(cancelIcon3);
                addStockButton.setContentDisplay(ContentDisplay.LEFT); 
                addStockButton.setStyle("-fx-background-color:transparent;");
                
                buttonContainer.getChildren().addAll(viewButton, editButton, detailsButton, addStockButton);
                buttonContainer.setAlignment(Pos.CENTER);
                
                viewButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    try {
                        showStockHistory(request.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                
                editButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId() + " ---- " + request.getAvailable());

                    try {
                        String equipmentID = request.getId();
                        String oldName = request.getName();

                        // 1️⃣ Prompt for new name
                        TextInputDialog nameDialog = new TextInputDialog(oldName);
                        nameDialog.setTitle("Edit Equipment");
                        nameDialog.setHeaderText("Edit name for Equipment ID: " + equipmentID);
                        nameDialog.setContentText("Enter new name:");
                        Optional<String> nameResult = nameDialog.showAndWait();
                        if (!nameResult.isPresent()) {
                            return; // cancelled
                        }
                        String newName = nameResult.get().trim();
                        if (newName.isEmpty()) {
                            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Name cannot be empty.");
                            return;
                        }

                        // 2️⃣ Prompt for stockRoom choice (1 or 2)
                        ChoiceDialog<Integer> roomDialog = new ChoiceDialog<>(1, 1, 2);
                        roomDialog.setTitle("Edit Stock Room");
                        roomDialog.setHeaderText("Choose stockRoom for " + newName);
                        roomDialog.setContentText("Select stockRoom (1 or 2):");
                        Optional<Integer> roomResult = roomDialog.showAndWait();
                        if (!roomResult.isPresent()) {
                            return; // cancelled
                        }
                        int newStockRoom = roomResult.get();
                        // ✅ Always use two separate statements
                        java.sql.Statement updateStatement = con.createStatement();
                        java.sql.Statement readStatement = con.createStatement();

                        // ✅ 1. Update equipments table
                        String updateEquipment = "UPDATE equipments SET name = '" + newName + "', stockRoom = " + newStockRoom
                                               + " WHERE id = " + equipmentID;
                        updateStatement.executeUpdate(updateEquipment);

                        // ✅ 2. Get all related serials
                        ResultSet rs = readStatement.executeQuery(
                            "SELECT id, serial_num FROM serial WHERE equipment_id = " + equipmentID
                        );

                        // ✅ 3. Update each serial_num using the updateStatement
                        while (rs.next()) {
                            int serialId = rs.getInt("id");
                            String oldSerialNum = rs.getString("serial_num");

                            String[] parts = oldSerialNum.split("-");
                            String newSerialNum = newName + "-" + parts[1];

                            updateStatement.executeUpdate(
                                "UPDATE serial SET serial_num = '" + newSerialNum + "' WHERE id = " + serialId
                            );
                        }

                        // ✅ Clean up
                        rs.close();
                        readStatement.close();
                        updateStatement.close();

                        showAlert(Alert.AlertType.INFORMATION, "Success", "Equipment updated successfully!");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                detailsButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId() + " ---- " + request.getAvailable());

                    try {
                        String equipmentID = request.getId();
                        String equipmentName = request.getName();

                        // 1️⃣ Ask user: View Inventory or Remarks?
                        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Inventory Details", "Inventory Details", "Remarks");
                        choiceDialog.setTitle("View Details");
                        choiceDialog.setHeaderText("Choose what to view for: " + equipmentName);
                        choiceDialog.setContentText("Select option:");

                        Optional<String> choiceResult = choiceDialog.showAndWait();
                        if (!choiceResult.isPresent()) {
                            return; // Cancelled
                        }

                        String selectedOption = choiceResult.get();

                        java.sql.Statement statement = con.createStatement();

                        if (selectedOption.equals("Inventory Details")) {
                            // ✅ Show Inventory Details
                            ResultSet rs = statement.executeQuery(
                                "SELECT * FROM inventory WHERE equipment_id = '" + equipmentID + "'"
                            );
                            if (rs.next()) {
                                Dialog<Void> dialog = new Dialog<>();
                                dialog.setTitle("Inventory Details for: " + equipmentName);
                                dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

                                GridPane grid = new GridPane();
                                grid.setHgap(10);
                                grid.setVgap(10);
                                grid.setPadding(new Insets(10));
                                grid.setPrefWidth(500);
                                int row = 0;
                                do {
                                    Label dateLabel = new Label("Date Purchased:");
                                    Label dateValue = new Label(rs.getString("date_purchased"));

                                    Label propNumLabel = new Label("Property No:");
                                    Label propNumValue = new Label(rs.getString("property_num"));

                                    Label descLabel = new Label("Description:");
                                    Label descValue = new Label(rs.getString("description"));
                                    descValue.setWrapText(true); // Wrap if long

                                    Label serialLabel = new Label("Serial No:");
                                    Label serialValue = new Label(rs.getString("serial_num"));

                                    Label amountLabel = new Label("Amount:");
                                    Label amountValue = new Label(rs.getString("amount"));

                                    Label parLabel = new Label("PAR No:");
                                    Label parValue = new Label(rs.getString("par_num"));

                                    grid.add(dateLabel, 0, row);
                                    grid.add(dateValue, 1, row++);
                                    grid.add(propNumLabel, 0, row);
                                    grid.add(propNumValue, 1, row++);
                                    grid.add(descLabel, 0, row);
                                    grid.add(descValue, 1, row++);
                                    grid.add(serialLabel, 0, row);
                                    grid.add(serialValue, 1, row++);
                                    grid.add(amountLabel, 0, row);
                                    grid.add(amountValue, 1, row++);
                                    grid.add(parLabel, 0, row);
                                    grid.add(parValue, 1, row++);

                                    // Add separator between multiple records if needed
                                    if (!rs.isLast()) {
                                        grid.add(new Separator(), 0, row++, 2, 1);
                                    }

                                } while (rs.next());

                                ScrollPane scrollPane = new ScrollPane(grid);
                                scrollPane.setFitToWidth(true);
                                dialog.getDialogPane().setContent(scrollPane);

                                dialog.showAndWait();
                            

                            } else {
                                // ❗ No inventory found → ask to add
                                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                                confirm.setTitle("Add Inventory Details");
                                confirm.setHeaderText("No inventory details found for this equipment.");
                                confirm.setContentText("Do you want to add inventory details now?");

                                ButtonType yesButton = new ButtonType("Yes");
                                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                                confirm.getButtonTypes().setAll(yesButton, noButton);

                                Optional<ButtonType> result = confirm.showAndWait();
                                if (result.isPresent() && result.get() == yesButton) {
                                    addInventoryDetails(Integer.parseInt(equipmentID));
                                }
                            }

                            rs.close();

                        } else if (selectedOption.equals("Remarks")) {
                            // ✅ Show Remarks in a TableView in a Dialog

                            ResultSet rs = statement.executeQuery(
                                "SELECT remark, date FROM remarks WHERE equipment_id = '" + equipmentID + "' ORDER BY date DESC"
                            );

                            ObservableList<RemarkModel> remarksList = FXCollections.observableArrayList();
                            while (rs.next()) {
                                remarksList.add(new RemarkModel(rs.getString("remark"), rs.getString("date")));
                            }
                            rs.close();

                            if (remarksList.isEmpty()) {
                                showAlert(Alert.AlertType.INFORMATION, "Remarks", "No remarks found for this equipment.");
                                return;
                            }

                            TableView<RemarkModel> remarksTable = new TableView<>();
                            TableColumn<RemarkModel, String> remarkCol = new TableColumn<>("Remark");
                            remarkCol.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());

                            TableColumn<RemarkModel, String> dateCol = new TableColumn<>("Date");
                            dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

                            remarksTable.getColumns().addAll(remarkCol, dateCol);
                            remarksTable.setItems(remarksList);
                            remarksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                            // Wrap text for long remarks
                            remarkCol.setCellFactory(tc -> {
                                TableCell<RemarkModel, String> cell = new TableCell<>() {
                                    private final Text text = new Text();
                                    {
                                        text.wrappingWidthProperty().bind(remarkCol.widthProperty());
                                        setGraphic(text);
                                    }

                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        text.setText(empty ? "" : item);
                                    }
                                };
                                return cell;
                            });
                            remarkCol.setPrefWidth(500);  // 300 pixels for Remark
                            dateCol.setPrefWidth(150);    // 150 pixels for Date


                            // Wrap table in scrollable dialog
                            Dialog<Void> tableDialog = new Dialog<>();
                            tableDialog.setTitle("Remarks for: " + equipmentName);
                            tableDialog.getDialogPane().setContent(new VBox(remarksTable));
                            tableDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                            tableDialog.showAndWait();
                        }

                        statement.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


                addStockButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    String equipmentId = request.getId();
                    String equipmentName = request.getName();

                    System.out.println("Selected equipment: " + equipmentId + " - " + equipmentName);

                    try {
                        // 🔑 First, ask what to do
                        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Add Stock", "Add Stock", "Add Remark");
                        choiceDialog.setTitle("Add Stock or Remark");
                        choiceDialog.setHeaderText("What would you like to do for: " + equipmentName + "?");
                        choiceDialog.setContentText("Choose action:");

                        Optional<String> choiceResult = choiceDialog.showAndWait();
                        if (choiceResult.isEmpty()) {
                            return; // cancelled
                        }

                        String choice = choiceResult.get();
                        java.sql.Statement statement = con.createStatement();

                        if (choice.equals("Add Stock")) {
                            // ✔️ Do your existing stock logic:
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Add Stock");
                            dialog.setHeaderText("Add stock for: " + equipmentName);
                            dialog.setContentText("Enter quantity:");

                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                int quantity = Integer.parseInt(result.get().trim());
                                if (quantity <= 0) {
                                    showAlert(Alert.AlertType.WARNING, "Invalid Input", "Quantity must be greater than 0.");
                                    return;
                                }

                                // 1) Insert into stocks history
                                String insertStock = "INSERT INTO stocks (equipment_id, stocks, dated_added) "
                                        + "VALUES (" + equipmentId + ", " + quantity + ", NOW())";
                                statement.executeUpdate(insertStock);

                                // 2) Get last serial
                                String getLastSerial = "SELECT serial_num FROM serial "
                                        + "WHERE equipment_id = " + equipmentId + " ORDER BY id DESC LIMIT 1";
                                ResultSet rs = statement.executeQuery(getLastSerial);
                                int lastNumber = 0;
                                if (rs.next()) {
                                    String lastSerial = rs.getString("serial_num");
                                    String[] parts = lastSerial.split("-");
                                    if (parts.length == 2) {
                                        lastNumber = Integer.parseInt(parts[1]);
                                    }
                                }

                                // 3) Insert new serials
                                for (int i = 1; i <= quantity; i++) {
                                    int newNumber = lastNumber + i;
                                    String serialNum = equipmentName + "-" + newNumber;
                                    String insertSerial = "INSERT INTO serial (serial_num, equipment_id) "
                                            + "VALUES ('" + serialNum + "', " + equipmentId + ")";
                                    statement.executeUpdate(insertSerial);
                                }

                                // 4) Update equipment stock counts
                                String updateEquipment = "UPDATE equipments "
                                        + "SET stock = (SELECT COUNT(*) FROM serial WHERE equipment_id = " + equipmentId + " AND status = 'available'), "
                                        + "available = (SELECT COUNT(*) FROM serial WHERE equipment_id = " + equipmentId + ") "
                                        + "WHERE id = " + equipmentId;
                                statement.executeUpdate(updateEquipment);

                                showAlert(Alert.AlertType.INFORMATION, "Success", "Stock added successfully!");
                                loadEquipmentsTable2();
                            }

                        } else if (choice.equals("Add Remark")) {
                            // ✔️ New: add remark
                            // ✅ Create custom dialog with TextArea
                            Dialog<String> remarkDialog = new Dialog<>();
                            remarkDialog.setTitle("Add Remark");
                            remarkDialog.setHeaderText("Add remark for: " + equipmentName);

                            // ✅ Add OK + Cancel buttons
                            ButtonType okButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                            remarkDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                            // ✅ Use a TextArea for multiline input
                            TextArea textArea = new TextArea();
                            textArea.setPromptText("Enter remark here...");
                            textArea.setPrefRowCount(5);    // more rows = bigger box
                            textArea.setPrefColumnCount(40); // wider
                            textArea.setWrapText(true);

                            remarkDialog.getDialogPane().setContent(textArea);

                            // ✅ Convert the result
                            remarkDialog.setResultConverter(dialogButton -> {
                                if (dialogButton == okButtonType) {
                                    return textArea.getText();
                                }
                                return null;
                            });

                            Optional<String> remarkResult = remarkDialog.showAndWait();

                            if (remarkResult.isPresent()) {
                                String remarkText = remarkResult.get().trim();
                                if (remarkText.isEmpty()) {
                                    showAlert(Alert.AlertType.WARNING, "Invalid Input", "Remark cannot be empty.");
                                    return;
                                }

                                String insertRemark = "INSERT INTO remarks (equipment_id, remark) "
                                        + "VALUES (" + equipmentId + ", '" + remarkText + "')";
                                statement.executeUpdate(insertRemark);

                                showAlert(Alert.AlertType.INFORMATION, "Success", "Remark added successfully!");
                            }

                        }

                        statement.close();

                    } catch (NumberFormatException nfe) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });


            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonContainer); // Set the container with both buttons
                }
            }
        });
    }
    @FXML
    private void viewStockHistory() throws Exception{
        showStockHistory(null);
    }
    private void showStockHistory(String equipmentID) throws Exception {
        java.sql.Statement statement = con.createStatement();

        String sql;
        if (equipmentID == null || equipmentID.isEmpty()) {
            sql = "SELECT s.id, s.equipment_id, e.name AS equipment_name, s.stocks, s.dated_added "
                + "FROM stocks s JOIN equipments e ON s.equipment_id = e.id "
                + "ORDER BY s.dated_added DESC";
        } else {
            sql = "SELECT s.id, s.equipment_id, e.name AS equipment_name, s.stocks, s.dated_added "
                + "FROM stocks s JOIN equipments e ON s.equipment_id = e.id "
                + "WHERE s.equipment_id = '" + equipmentID + "' "
                + "ORDER BY s.dated_added DESC";
        }

        ResultSet rs = statement.executeQuery(sql);

        TableView<StockRecord> table = new TableView<>();

        TableColumn<StockRecord, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> data.getValue().idProperty());

        TableColumn<StockRecord, String> eqIdCol = new TableColumn<>("Equipment ID");
        eqIdCol.setCellValueFactory(data -> data.getValue().equipmentIdProperty());

        TableColumn<StockRecord, String> eqNameCol = new TableColumn<>("Equipment Name");
        eqNameCol.setCellValueFactory(data -> data.getValue().equipmentNameProperty());

        TableColumn<StockRecord, String> stocksCol = new TableColumn<>("Stocks Added");
        stocksCol.setCellValueFactory(data -> data.getValue().stocksProperty());

        TableColumn<StockRecord, String> dateCol = new TableColumn<>("Date Added");
        dateCol.setCellValueFactory(data -> data.getValue().dateAddedProperty());

        table.getColumns().addAll(idCol, eqIdCol, eqNameCol, stocksCol, dateCol);

        ObservableList<StockRecord> stockHistory = FXCollections.observableArrayList();
        while (rs.next()) {
            stockHistory.add(new StockRecord(
                rs.getString("id"),
                rs.getString("equipment_id"),
                rs.getString("equipment_name"),
                rs.getString("stocks"),
                rs.getString("dated_added")
            ));
        }
        table.setItems(stockHistory);

        rs.close();
        statement.close();

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(800);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Stock History");
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    ObservableList<serialModel> availSerials = FXCollections.observableArrayList();
    public void getAllAvailableSerialNumbers()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
                  "SELECT * FROM serial "
                + "WHERE status = 'available' AND equipment_id = '"+equipmentID+"'"
        );
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("serial_num");
            
            availSerials.add(new serialModel(i,ii));
        }
    }
    
    @FXML private TableView<serialModel> serialTable;
    @FXML private TableColumn<serialModel, String> serialCol;
    @FXML private TableColumn<serialModel, Void> actionsCol;
    ObservableList<serialModel> selectedSerials = FXCollections.observableArrayList();

    public void loadSerialTable()throws Exception{
        availSerials.clear();
        serialCol.setCellValueFactory(cellData -> cellData.getValue().serialNumberProperty());
        getAllAvailableSerialNumbers();
        serialTable.setItems(availSerials);
        
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button confirmButton = new Button("SELECT");
            {
                confirmButton.setOnAction(event -> {
                    serialModel selectedItem = getTableView().getItems().get(getIndex());
                    if (selectedSerials.contains(selectedItem)) {
                        selectedSerials.remove(selectedItem);
                        confirmButton.setText("SELECT");
                    } else {
                        selectedSerials.add(selectedItem);
                        confirmButton.setText("SELECTED");
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    serialModel currentItem = getTableView().getItems().get(getIndex());
                    confirmButton.setText(selectedSerials.contains(currentItem) ? "SELECTED" : "SELECT");
                    setGraphic(confirmButton);
                }
            }
        });
    }
    public void confirmSerialNumbersClick()throws Exception{
        if(!selectedSerials.isEmpty()){
            availableSerialPane.setVisible(false);
            userPane.setVisible(true);
            loadUsersTable();
        }else{
            showAlert(Alert.AlertType.INFORMATION, "Failed", "Pick an item!");
        }
        
    }
    
    
    @FXML private Pane availableSerialPane;
    @FXML private TableView<equipmentModel> borrowsTable;
    @FXML private TableColumn<equipmentModel, String> z1,z2, z3, z4;
    @FXML private TableColumn<equipmentModel, Void> z5;
    public void loadEquipmentsTable()throws Exception{
        Equipments.clear();
        z1.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        z2.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        z3.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
        z4.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        getEquipments();
        borrowsTable.setItems(Equipments);
        z5.setCellFactory(col -> new TableCell<>() {
            private final Button confirmButton = new Button();

            {
                ImageView cancelIcon = new ImageView(new Image("icons/borrow.png")); // Replace with your icon file
                cancelIcon.setFitHeight(23); 
                cancelIcon.setFitWidth(23);
                
                confirmButton.setGraphic(cancelIcon);
                confirmButton.setContentDisplay(ContentDisplay.LEFT); 
                confirmButton.setStyle("-fx-background-color:transparent;");
                
                confirmButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId() +"----"+request.getAvailable());
                     
                    try {
                        equipmentID=request.getId();
                        availableStock=request.getAvailable();
                        borrow.setVisible(false);
                        availableSerialPane.setVisible(true);
//                        userPane.setVisible(true);
                        loadSerialTable();
//                        loadUsersTable();
                        //loadReservationTable();
//                        getRequest(); 
//                        pendingTable.setItems(requestsPending); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(confirmButton);
                }
            }
        });
    }
    
    ObservableList<equipmentModel> Equipments = FXCollections.observableArrayList();
    public void getEquipments()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
        "SELECT * FROM equipments");
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("name");
            String iii = resultSet.getString("stock");
            String iiii = resultSet.getString("available");
            String iiiii = resultSet.getString("date_created");
            String iiiiii = resultSet.getString("stockRoom");
            
            Equipments.add(new equipmentModel(i,ii,iii,iiii,iiiii,iiiiii));
            
        }
    }
    ObservableList<equipmentModel> filteredListEquipments = FXCollections.observableArrayList();
    public void searchMeEquipments(){
        String searchTerm = searchEquipment.getText().toLowerCase();
        // Clear the current filtered list
        filteredListEquipments.clear();

        // Filter the borrowedEquipments list based on the search term
        for (equipmentModel equipment : Equipments) {
            if (matchesSearchEquipments(equipment, searchTerm)) {
                filteredListEquipments.add(equipment); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        borrowsTable.setItems(filteredListEquipments);

    }
    // Check if the BorrowedEquipments matches the search term
    private boolean matchesSearchEquipments(equipmentModel equipment, String searchTerm) {
        return equipment.getId().toLowerCase().contains(searchTerm) ||
               equipment.getName().toLowerCase().contains(searchTerm) ||
               equipment.getStock().toLowerCase().contains(searchTerm) ||
               equipment.getAvailable().toLowerCase().contains(searchTerm) ||
               equipment.getDateCreated().toLowerCase().contains(searchTerm);
    }
    
    
    
    @FXML private TableView<reservation> reservationTable;
    @FXML private TableColumn<reservation, String> x1,x2, x3, x4, x5, x6,x7;
    @FXML private TableColumn<reservation, Void> x8;
    public void loadReservationTable()throws Exception{
        reservationEquipments.clear();
        x1.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
        x2.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        x3.setCellValueFactory(cellData -> cellData.getValue().yearSectionProperty());
        x4.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        x5.setCellValueFactory(cellData -> cellData.getValue().equipmentNameProperty());
        x6.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        x7.setCellValueFactory(cellData -> cellData.getValue().reserveAtProperty());
        
        getReservation();
        
        
        reservationTable.setItems(reservationEquipments);
        
        x8.setCellFactory(col -> new TableCell<>() {
            private final Button confirmButton = new Button();

            {
                ImageView cancelIcon = new ImageView(new Image("icons/confirm.png")); // Replace with your icon file
                cancelIcon.setFitHeight(23); 
                cancelIcon.setFitWidth(23);
                
                confirmButton.setGraphic(cancelIcon);
                confirmButton.setContentDisplay(ContentDisplay.LEFT); 
                confirmButton.setStyle("-fx-background-color:transparent;");
                
                confirmButton.setOnAction(event -> {
                    reservation request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId());
                    
//                    deleteRequest(request); 
                    try {
                        updateReservation(request.getquantity(), request.getuserid(), request.getequipmentid(),request.getId());
                        loadReservationTable();
                        
//                        getRequest(); 
//                        pendingTable.setItems(requestsPending); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(confirmButton);
                }
            }
        });
    }
    
    ObservableList<reservation> reservationEquipments = FXCollections.observableArrayList();
    public void getReservation()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
        "SELECT r.id, r.user_id, r.equipment_id, CONCAT(u.fname, ' ', u.mname, ' ', u.lname, ' ', u.suffix) as fullname, u.department, u.year_section, u.role, e.name, r.quantity, r.date_reserve\n" +
        "FROM reservation r\n" +
        "JOIN users u ON u.id = r.user_id\n" +
        "JOIN equipments e ON r.equipment_id = e.id;");
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("fullname");
            String iii = resultSet.getString("department");
            String iiii = resultSet.getString("year_section");
            String iiiii = resultSet.getString("role");
            String iiiiii = resultSet.getString("name");
            String iiiiiii = resultSet.getString("date_reserve");
            String iiiiiiii = resultSet.getString("quantity");
            String i1 = resultSet.getString("user_id");
            String i2 = resultSet.getString("equipment_id");
            
            reservationEquipments.add(new reservation(i,ii,iii,iiii,iiiii,iiiiii,iiiiiii,iiiiiiii,i1,i2));
            
        }
    }
    
    public void updateReservation(String quantity, String userid, String equipmentid, String reservationid)throws Exception{
        getAvailSer(equipmentid);
        java.sql.Statement statement = con.createStatement();
        for(int a=0; a<Integer.parseInt(quantity); a++){
            statement.executeUpdate("INSERT INTO `borrow`(`user_id`, `equipment_id`, `serial_id`) \n" +
                                    "VALUES ('"+userid+"',"
                                            + "'"+equipmentid+"',"
                                            + "'"+availSer.get(0)+"')");
            
            statement.executeUpdate("UPDATE `serial` SET `status`='unavailable' WHERE id = '"+availSer.get(0)+"'");
            
            availSer.remove(0);
            
        }
        
        
        statement.executeUpdate("DELETE FROM `reservation` WHERE id ='"+reservationid+"'");
        
    }
    
    ObservableList<String> availSer = FXCollections.observableArrayList();
    public void getAvailSer(String id)throws Exception{
        
        java.sql.Statement statement = con.createStatement();
        System.out.println(id);
        ResultSet resultSet = statement.executeQuery(
        "SELECT * FROM serial WHERE status = 'available' AND equipment_id = '"+id+"'");
        while(resultSet.next()){
            
            String i = resultSet.getString("id");
            availSer.add(i);
        }
    }
    
    ObservableList<reservation> filteredListReservation = FXCollections.observableArrayList();
    public void searchMeReservation(){
        String searchTerm = searchReservation.getText().toLowerCase();
        // Clear the current filtered list
        filteredListReservation.clear();

        // Filter the borrowedEquipments list based on the search term
        for (reservation equipment : reservationEquipments) {
            if (matchesSearchReservation(equipment, searchTerm)) {
                filteredListReservation.add(equipment); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        reservationTable.setItems(filteredListReservation);

    }
    // Check if the BorrowedEquipments matches the search term
    private boolean matchesSearchReservation(reservation equipment, String searchTerm) {
        return equipment.getFullname().toLowerCase().contains(searchTerm) ||
               equipment.getDepartment().toLowerCase().contains(searchTerm) ||
               equipment.getYearSection().toLowerCase().contains(searchTerm) ||
               equipment.getEquipmentName().toLowerCase().contains(searchTerm) ||
               equipment.getRole().toLowerCase().contains(searchTerm) ||
               equipment.getquantity().toLowerCase().contains(searchTerm) ||
               equipment.getreserveAt().toLowerCase().contains(searchTerm);
    }
    
    
    @FXML private TableView<BorrowedEquipments> borrowersTable;
    @FXML private TableColumn<BorrowedEquipments, String> c1,c2, c3, c4, c5, c6,c7,c9;
    @FXML private TableColumn<BorrowedEquipments, Void> c8;
    public void loadBorrowersTable()throws Exception{
        borrowedEquipments.clear();
        c1.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
        c2.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        c3.setCellValueFactory(cellData -> cellData.getValue().yearSectionProperty());
        c4.setCellValueFactory(cellData -> cellData.getValue().equipmentNameProperty());
        c5.setCellValueFactory(cellData -> cellData.getValue().borrowedAtProperty());
        c6.setCellValueFactory(cellData -> cellData.getValue().serialNumProperty());
        c7.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        c9.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        getBorrowers();
        
        
        borrowersTable.setItems(borrowedEquipments);
        
        c8.setCellFactory(col -> new TableCell<>() {
            private final Button returnButton = new Button();

            {
                ImageView cancelIcon = new ImageView(new Image("icons/return.png")); // Replace with your icon file
                cancelIcon.setFitHeight(23); 
                cancelIcon.setFitWidth(23);
                
                returnButton.setGraphic(cancelIcon);
                returnButton.setContentDisplay(ContentDisplay.LEFT); 
                returnButton.setStyle("-fx-background-color:transparent;");

                returnButton.setOnAction(event -> {
                    BorrowedEquipments request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId());
                    
//                    deleteRequest(request); 
                    try {
                        updateBorrow(request.getId(), request.getSerialNum());
                        loadBorrowersTable();
//                        getRequest(); 
//                        pendingTable.setItems(requestsPending); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(returnButton);
                }
            }
        });
    }
    
    ObservableList<BorrowedEquipments> borrowedEquipments = FXCollections.observableArrayList();
    public void getBorrowers()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
        "SELECT b.id, CONCAT(u.fname, ' ', u.mname, ' ', u.lname, ' ', u.suffix) as fullname, u.department, u.year_section, u.role, e.name, b.borrowed_at, s.serial_num, u.role\n" +
        "FROM borrow b\n" +
        "JOIN users u ON u.id = b.user_id\n" +
        "JOIN equipments e ON b.equipment_id = e.id\n" +
        "JOIN serial s ON b.serial_id = s.id WHERE b.status = 'borrowed';");
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("fullname");
            String iii = resultSet.getString("department");
            String iiii = resultSet.getString("year_section");
            String iiiii = resultSet.getString("role");
            String iiiiii = resultSet.getString("name");
            String iiiiiii = resultSet.getString("borrowed_at");
            String iiiiiiii = resultSet.getString("serial_num");
            borrowedEquipments.add(new BorrowedEquipments(i,ii,iii,iiii,iiiii,iiiiii,iiiiiii,iiiiiiii));
            
        }
    }
    
    public void updateBorrow(String id, String serid)throws Exception{
        System.out.println(serid+"====");
        java.sql.Statement statement = con.createStatement();
        statement.executeUpdate("UPDATE borrow SET status = 'returned', returned_at = NOW() WHERE id = '" + id + "';");
        
        ResultSet resultSet = statement.executeQuery("SELECT * FROM serial WHERE serial_num = '" + serid + "';");
        String equipID = "";
        while(resultSet.next()){
            equipID = resultSet.getString("equipment_id");
        }
        
        statement.executeUpdate("UPDATE equipments SET available = available+1 WHERE id = '" + equipID + "';");
        
        statement.executeUpdate("UPDATE serial SET status = 'available' WHERE serial_num = '" + serid + "';");
    }
    
    
    ObservableList<BorrowedEquipments> filteredList = FXCollections.observableArrayList();
    public void searchMe(){
        String searchTerm = search.getText().toLowerCase();
        // Clear the current filtered list
        filteredList.clear();

        // Filter the borrowedEquipments list based on the search term
        for (BorrowedEquipments equipment : borrowedEquipments) {
            if (matchesSearch(equipment, searchTerm)) {
                filteredList.add(equipment); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        borrowersTable.setItems(filteredList);

    }
    // Check if the BorrowedEquipments matches the search term
    private boolean matchesSearch(BorrowedEquipments equipment, String searchTerm) {
        return equipment.getFullname().toLowerCase().contains(searchTerm) ||
               equipment.getDepartment().toLowerCase().contains(searchTerm) ||
               equipment.getYearSection().toLowerCase().contains(searchTerm) ||
               equipment.getEquipmentName().toLowerCase().contains(searchTerm) ||
               equipment.getSerialNum().toLowerCase().contains(searchTerm)||
               equipment.getBorrowedAt().toLowerCase().contains(searchTerm);
    }
    
    
    @FXML private TableView<BorrowedEquipments> historyTable;
    @FXML private TableColumn<BorrowedEquipments, String> c11,c21, c31, c41, c51, c61,c71;
    public void loadHistoryTable()throws Exception{
        history.clear();
        c11.setCellValueFactory(cellData -> cellData.getValue().fullnameProperty());
        c21.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        c31.setCellValueFactory(cellData -> cellData.getValue().yearSectionProperty());
        c41.setCellValueFactory(cellData -> cellData.getValue().equipmentNameProperty());
        c51.setCellValueFactory(cellData -> cellData.getValue().borrowedAtProperty());
        c61.setCellValueFactory(cellData -> cellData.getValue().serialNumProperty());
        c71.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        getHistory();
        historyTable.setItems(history);
        
    }
    
    ObservableList<BorrowedEquipments> history = FXCollections.observableArrayList();
    public void getHistory()throws Exception{
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(
        "SELECT b.id, CONCAT(u.fname, ' ', u.mname, ' ', u.lname, ' ', u.suffix) as fullname, u.department, u.year_section, u.role, e.name, b.borrowed_at, s.serial_num, u.role\n" +
        "FROM borrow b\n" +
        "JOIN users u ON u.id = b.user_id\n" +
        "JOIN equipments e ON b.equipment_id = e.id\n" +
        "JOIN serial s ON b.serial_id = s.id WHERE b.status = 'returned';");
        while(resultSet.next()){
            String i = resultSet.getString("id");
            String ii = resultSet.getString("fullname");
            String iii = resultSet.getString("department");
            String iiii = resultSet.getString("year_section");
            String iiiii = resultSet.getString("role");
            String iiiiii = resultSet.getString("name");
            String iiiiiii = resultSet.getString("borrowed_at");
            String iiiiiiii = resultSet.getString("serial_num");
            history.add(new BorrowedEquipments(i,ii,iii,iiii,iiiii,iiiiii,iiiiiii,iiiiiiii));
            
        }
    }
    
    
    ObservableList<BorrowedEquipments> filteredHistory = FXCollections.observableArrayList();
    public void searchMeHistory(){
        String searchTerm = historySearch.getText().toLowerCase();
        // Clear the current filtered list
        filteredHistory.clear();

        // Filter the borrowedEquipments list based on the search term
        for (BorrowedEquipments equipment : history){
            if (matchesSearchHistory(equipment, searchTerm)) {
                filteredHistory.add(equipment); // Add matching items to the filtered list
            }
        }

        // Update the TableView with the filtered list
        historyTable.setItems(filteredHistory);

    }
    // Check if the BorrowedEquipments matches the search term
    private boolean matchesSearchHistory(BorrowedEquipments equipment, String searchTerm) {
        return equipment.getFullname().toLowerCase().contains(searchTerm) ||
               equipment.getDepartment().toLowerCase().contains(searchTerm) ||
               equipment.getYearSection().toLowerCase().contains(searchTerm) ||
               equipment.getSerialNum().toLowerCase().contains(searchTerm) ||
               equipment.getDepartment().toLowerCase().contains(searchTerm) ||
               equipment.getRole().toLowerCase().contains(searchTerm) ||
               equipment.getBorrowedAt().toLowerCase().contains(searchTerm);
    }
    
    public Connection connect() {
        url = "jdbc:mysql://localhost:3306/becinventory";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
        return con;
    
    }
    @FXML private Pane home, reservation, borrow, historyPane, equipmentsPane, logInPane, navigation, addBorrowersPane;
    @FXML private Button homebtn, reservationbtn, borrowbtn, historybtn,equipmentbtn; 

    private void resetButtonStyles() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: black;";
        homebtn.setStyle(defaultStyle);
        reservationbtn.setStyle(defaultStyle);
        borrowbtn.setStyle(defaultStyle);
        historybtn.setStyle(defaultStyle);
        equipmentbtn.setStyle(defaultStyle);
    }

    private void setActiveButtonStyle(Button button) {
        button.setStyle("-fx-background-color: #00a693; -fx-text-fill: white;");
    }

    // Navigation
    public void homeClick() throws Exception{
        resetButtonStyles();
        setActiveButtonStyle(homebtn);
        home.setVisible(true);
        reservation.setVisible(false);
        borrow.setVisible(false);
        historyPane.setVisible(false);
        userPane.setVisible(false);
        equipmentsPane.setVisible(false);
        availableSerialPane.setVisible(false);
        addBorrowersPane.setVisible(false);
        loadBorrowersTable();
        selectedSerials.clear();
    }

    public void reservationClick(ActionEvent event) throws Exception{
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
//        resetButtonStyles();
//        setActiveButtonStyle(reservationbtn);
//        home.setVisible(false);
//        addBorrowersPane.setVisible(true);
//        borrow.setVisible(false);
//        historyPane.setVisible(false);
//        userPane.setVisible(false);
//        equipmentsPane.setVisible(false);
//        availableSerialPane.setVisible(false);
//        loadReservationTable();
//        selectedSerials.clear();
    }

    public void borrowClick() throws Exception{
        resetButtonStyles();
        setActiveButtonStyle(borrowbtn);
        home.setVisible(false);
        reservation.setVisible(false);
        borrow.setVisible(true);
        historyPane.setVisible(false);
        userPane.setVisible(false);
        equipmentsPane.setVisible(false);
        availableSerialPane.setVisible(false);
        addBorrowersPane.setVisible(false);
        loadEquipmentsTable();
        selectedSerials.clear();
    }

    public void historyClick() throws Exception{
        resetButtonStyles();
        setActiveButtonStyle(historybtn);
        home.setVisible(false);
        reservation.setVisible(false);
        borrow.setVisible(false);
        historyPane.setVisible(true);
        userPane.setVisible(false);
        equipmentsPane.setVisible(false);
        availableSerialPane.setVisible(false);
        addBorrowersPane.setVisible(false);
        loadHistoryTable();
        selectedSerials.clear();
    }
    public void equipmentsClick() throws Exception{
        resetButtonStyles();
        setActiveButtonStyle(equipmentbtn);
        home.setVisible(false);
        reservation.setVisible(false);
        borrow.setVisible(false);
        historyPane.setVisible(false);
        userPane.setVisible(false);
        equipmentsPane.setVisible(true);
        availableSerialPane.setVisible(false);
        addBorrowersPane.setVisible(false);
        loadEquipmentsTable2();
        selectedSerials.clear();
    }
    
    // Dire Mag Design
    public void setDesign()throws Exception{
        homeClick();
        homebtn.setStyle("-fx-background-color: #00a693; -fx-text-fill: white;");
        String paneDesign = "-fx-background-color:linear-gradient(to bottom right, #272b3f, #256b52);";
        home.setStyle(paneDesign);
        borrow.setStyle(paneDesign);
        reservation.setStyle(paneDesign);
        userPane.setStyle(paneDesign);
        historyPane.setStyle(paneDesign);
        availableSerialPane.setStyle(paneDesign);
        equipmentsPane.setStyle(paneDesign);
        logInPane.setStyle(paneDesign);
        addBorrowersPane.setStyle(paneDesign);
        // Table styling for the body (white background, no borders)
        String tableDesign = 
            "-fx-border-color: transparent; " +
            "-fx-background-radius: 15;"+
            "-fx-background-color:linear-gradient(to bottom right, #272b3f, #256b52);"; 

        // Apply table body design
        reservationTable.setStyle(tableDesign);
        borrowersTable.setStyle(tableDesign);
        borrowsTable.setStyle(tableDesign);
        userTable.setStyle(tableDesign);
        historyTable.setStyle(tableDesign);
        equipmentsTable.setStyle(tableDesign);
        serialTable.setStyle(tableDesign);

        // Styling for headers (Persian Green)
        String headerStyle = 
            "-fx-background-color: white; " + 
            "-fx-text-fill: black; " + 
            "-fx-font-weight: bold; " +
            "-fx-alignment: center; ";

        // Apply header styling to each column
        borrowersTable.getColumns().forEach(column -> {
            column.setStyle(headerStyle);
        });
        borrowsTable.getColumns().forEach(column -> {
            column.setStyle(headerStyle);
        });
        userTable.getColumns().forEach(column -> {
            column.setStyle(headerStyle);
        });
        historyTable.getColumns().forEach(column -> {
            column.setStyle(headerStyle);
        });
        reservationTable.getColumns().forEach(column -> {
            column.setStyle(headerStyle);
        });
        equipmentsTable.getColumns().forEach(column -> {
            column.setStyle(headerStyle);
        });
        
        
        
        ImageView cancelIcon = new ImageView(new Image("icons/add.png")); // Replace with your icon file
        cancelIcon.setFitHeight(30); 
        cancelIcon.setFitWidth(30);

//        add.setGraphic(cancelIcon);
//        add.setContentDisplay(ContentDisplay.LEFT); 
//        add.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon2 = new ImageView(new Image("icons/save.png")); // Replace with your icon file
        cancelIcon2.setFitHeight(30); 
        cancelIcon2.setFitWidth(30);

//        save.setGraphic(cancelIcon2);
//        save.setContentDisplay(ContentDisplay.LEFT); 
//        save.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon3 = new ImageView(new Image("icons/confirm.png")); // Replace with your icon file
        cancelIcon3.setFitHeight(30); 
        cancelIcon3.setFitWidth(30);
        
        
//        add.setDisable(false);
//        save.setDisable(true);
        
        
        
        ImageView cancelIcon4 = new ImageView(new Image("icons/home.png")); // Replace with your icon file
        cancelIcon4.setFitHeight(25); 
        cancelIcon4.setFitWidth(25);

        homebtn.setGraphic(cancelIcon4);
        homebtn.setContentDisplay(ContentDisplay.LEFT); 
        homebtn.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon5 = new ImageView(new Image("icons/logout2.png")); // Replace with your icon file
        cancelIcon5.setFitHeight(25); 
        cancelIcon5.setFitWidth(25);

        reservationbtn.setGraphic(cancelIcon5);
        reservationbtn.setContentDisplay(ContentDisplay.LEFT); 
        reservationbtn.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon6 = new ImageView(new Image("icons/get.png")); // Replace with your icon file
        cancelIcon6.setFitHeight(25); 
        cancelIcon6.setFitWidth(25);

        borrowbtn.setGraphic(cancelIcon6);
        borrowbtn.setContentDisplay(ContentDisplay.LEFT); 
        borrowbtn.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView c1 = new ImageView(new Image("icons/equipment.png")); // Replace with your icon file
        c1.setFitHeight(25); 
        c1.setFitWidth(25);

        equipmentbtn.setGraphic(c1);
        equipmentbtn.setContentDisplay(ContentDisplay.LEFT); 
        equipmentbtn.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView c2 = new ImageView(new Image("icons/history.png")); // Replace with your icon file
        c2.setFitHeight(25); 
        c2.setFitWidth(25);

        historybtn.setGraphic(c2);
        historybtn.setContentDisplay(ContentDisplay.LEFT); 
        historybtn.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
    }
    public void backToBorrowingSection(){
        availableSerialPane.setVisible(false);
        borrow.setVisible(true);
        selectedSerials.clear();
    }
    public void backToSerialSection(){
        availableSerialPane.setVisible(true);
        userPane.setVisible(false);
    }
}
