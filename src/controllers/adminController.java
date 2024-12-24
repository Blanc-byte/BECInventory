/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import models.BorrowedEquipments;
import models.User;
import models.equipmentModel;
import models.reservation;

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
    
    @FXML private TextField search,historySearch, searchReservation, searchEquipment, searchUser, quantity, usersss, nameOfEquipment, stocksAvailable;
    @FXML private Pane userPane;
    @FXML private Button add, save, cons;
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
        
        
    }
    String BECpassword = "dorsu-bec2024";
    public void checkPassword(){
        if(password.getText().equals(BECpassword)){
            logInPane.setVisible(false);
            navigation.setVisible(true);
            home.setVisible(true);
        }
    }
    
    
    public void checkFirstTheStocks()throws Exception{
        if(nameOfEquipment.getText().equals("") || stocksAvailable.getText().equals("")){
            JOptionPane.showMessageDialog(null, "COMPLETE THE DETAILS");
        }else{
            java.sql.Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO `equipments`(`name`, `stock`, `available`) "
                    + "VALUES ('"+nameOfEquipment.getText()+"','"+stocksAvailable.getText()+"','"+stocksAvailable.getText()+"')");
            
            
            String ii = "";
            ResultSet resultSet = statement.executeQuery("SELECT * FROM equipments WHERE name ='"+nameOfEquipment.getText()+"'");
            while(resultSet.next()){
                ii = resultSet.getString("id");

            }
            
            for(int a=0; a<Integer.parseInt(stocksAvailable.getText()); a++ ){
                statement.executeUpdate("INSERT INTO `serial`(`serial_num`, `equipment_id`) "
                    + "VALUES ('"+nameOfEquipment.getText()+"-0"+a+"','"+ii+"')");
            
            }
            
            nameOfEquipment.setText("");stocksAvailable.setText("");
            loadEquipmentsTable2();
            JOptionPane.showMessageDialog(null, "SUCCESSFULLY ADDED");
        }
    }
    public void handleQuantity() {
        String input = stocksAvailable.getText();

        if (input.matches("\\d+")) { 
            System.out.println("Quantity: " + input);
        } else {
            System.out.println("Invalid input. Only numbers are allowed.");
            stocksAvailable.clear();
        }
    }
    
    String availableStock = "", equipmentID="", usrID="";
    
    public void deleteEquipment()throws Exception{
        java.sql.Statement statement = con.createStatement();
        statement.executeUpdate("DELETE FROM serial WHERE equipment_id = '"+equipmentID+"'");
        statement.executeUpdate("DELETE FROM equipments WHERE id = '"+equipmentID+"'");
        loadEquipmentsTable2();
    }
    
    public void editEquipment()throws Exception{
        java.sql.Statement statement = con.createStatement();
        statement.executeUpdate("UPDATE equipments SET name = '"+nameOfEquipment.getText()+"', "
                            + "stock='"+stocksAvailable.getText()+"',"
                            + "available='"+stocksAvailable.getText()+"' WHERE id = '"+equipmentID+"'"
        );
        
        
        
        save.setDisable(true);
        add.setDisable(false);
        statement.executeUpdate("DELETE FROM serial WHERE equipment_id = '"+equipmentID+"'");
        
        for(int a=0; a<Integer.parseInt(stocksAvailable.getText()); a++ ){
            statement.executeUpdate("INSERT INTO `serial`(`serial_num`, `equipment_id`) "
                + "VALUES ('"+nameOfEquipment.getText()+"-0"+a+"','"+equipmentID+"')");

        }
        nameOfEquipment.setText("");stocksAvailable.setText("");
        loadEquipmentsTable2();
    }
    
    public void confirmClick()throws Exception{
        if(checkFirst()){
            JOptionPane.showMessageDialog(null, "COMPLETE THE DETAILS");
        }else{
            if(Integer.parseInt(availableStock) <  Integer.parseInt(quantity.getText())){
                JOptionPane.showMessageDialog(null, "No Available Stocks Left");
            }else{
                updateEquipmentAndBorrow();
                loadEquipmentsTable();
                borrow.setVisible(true);
                userPane.setVisible(false);
            }
        }
    }
    
    public void updateEquipmentAndBorrow()throws Exception{
        int left = Integer.parseInt(availableStock) - Integer.parseInt(quantity.getText());
        java.sql.Statement statement = con.createStatement();
        statement.executeUpdate("UPDATE `equipments` SET `available`='"+left+"' WHERE id ='"+equipmentID+"'");
        getAvailSer(equipmentID);
        for(int a=0; a<Integer.parseInt(quantity.getText()); a++){
            statement.executeUpdate("INSERT INTO `borrow`(`user_id`, `equipment_id`, `serial_id`) \n" +
                                    "VALUES ('"+usrID+"',"
                                            + "'"+equipmentID+"',"
                                            + "'"+availSer.get(0)+"')");
            
            statement.executeUpdate("UPDATE `serial` SET `status`='unavailable' WHERE id = '"+availSer.get(0)+"'");
            availSer.remove(0);
        }
    }
    public boolean checkFirst(){
        if(usersss.getText().equals("") || quantity.getText().equals("")){
            return true;
        }
        return false;
    }
    public void handleQuantityInput() {
        String input = quantity.getText();

        if (input.matches("\\d+")) { 
            System.out.println("Quantity: " + input);
        } else {
            System.out.println("Invalid input. Only numbers are allowed.");
            quantity.clear();
        }
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
                        usersss.setText(request.getFname()+" "+request.getMname()+" "+request.getLname()+" "+request.getSuffix());
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
    @FXML private TableColumn<equipmentModel, String> s1,s2, s3, s4;
    @FXML private TableColumn<equipmentModel, Void> s5;
    public void loadEquipmentsTable2()throws Exception{
        Equipments.clear();
        s1.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        s2.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        s3.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
        s4.setCellValueFactory(cellData -> cellData.getValue().dateCreatedProperty());
        
        getEquipments();
        
        
        equipmentsTable.setItems(Equipments);
        
        s5.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox buttonContainer = new HBox(5);

            {
                ImageView cancelIcon = new ImageView(new Image("icons/edit.png")); // Replace with your icon file
                cancelIcon.setFitHeight(23); 
                cancelIcon.setFitWidth(23);
                
                editButton.setGraphic(cancelIcon);
                editButton.setContentDisplay(ContentDisplay.LEFT); 
                editButton.setStyle("-fx-background-color:transparent;");
                
                ImageView cancelIcon2 = new ImageView(new Image("icons/delete.png")); // Replace with your icon file
                cancelIcon2.setFitHeight(23); 
                cancelIcon2.setFitWidth(23);
                
                deleteButton.setGraphic(cancelIcon2);
                deleteButton.setContentDisplay(ContentDisplay.LEFT); 
                deleteButton.setStyle("-fx-background-color:transparent;");
                
                buttonContainer.getChildren().addAll(editButton, deleteButton);
                buttonContainer.setAlignment(Pos.CENTER);
                
                editButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId() +"----"+request.getAvailable());
                     
                    try {
                        equipmentID=request.getId();
                        availableStock=request.getAvailable();
                        
                        if(request.getStock().equals(request.getAvailable())){
                            add.setDisable(true);
                            save.setDisable(false);
                            nameOfEquipment.setText(request.getName());
                            stocksAvailable.setText(request.getStock());

                        }else{
                            JOptionPane.showMessageDialog(null, "Cant proceed, Complete the number of stocks first");
                        }
                        //loadReservationTable();
//                        getRequest(); 
//                        pendingTable.setItems(requestsPending); 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                deleteButton.setOnAction(event -> {
                    equipmentModel request = getTableView().getItems().get(getIndex());
                    System.out.println(request.getId() +"----"+request.getAvailable());
                     
                    try {
                        equipmentID=request.getId();
                        availableStock=request.getAvailable();
                        int response = JOptionPane.showConfirmDialog(
                                null, 
                                "Are you sure you want to delete "+request.getName()+" ?", 
                                "Confirmation", 
                                JOptionPane.YES_NO_OPTION, 
                                JOptionPane.QUESTION_MESSAGE
                        );

                        // Check the user's response
                        if (response == JOptionPane.YES_OPTION) {
                            deleteEquipment();
                        } else if (response == JOptionPane.NO_OPTION) {
                        } else {
                        }
                        
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
                    setGraphic(buttonContainer); // Set the container with both buttons
                }
            }
        });
    }
    
    
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
                        userPane.setVisible(true);
                        loadUsersTable();
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
            
            Equipments.add(new equipmentModel(i,ii,iii,iiii,iiiii));
            
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
               equipment.getSerialNum().toLowerCase().contains(searchTerm);
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
    @FXML private Pane home, reservation, borrow, historyPane, equipmentsPane, logInPane, navigation;
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

    public void homeClick() throws Exception{
        resetButtonStyles();
        setActiveButtonStyle(homebtn);
        home.setVisible(true);
        reservation.setVisible(false);
        borrow.setVisible(false);
        historyPane.setVisible(false);
        userPane.setVisible(false);
        equipmentsPane.setVisible(false);
        loadBorrowersTable();
    }

    public void reservationClick() throws Exception{
        resetButtonStyles();
        setActiveButtonStyle(reservationbtn);
        home.setVisible(false);
        reservation.setVisible(true);
        borrow.setVisible(false);
        historyPane.setVisible(false);
        userPane.setVisible(false);
        equipmentsPane.setVisible(false);
        loadReservationTable();
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
        loadEquipmentsTable();
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
        loadHistoryTable();
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
        loadEquipmentsTable2();
    }
    public void setDesign()throws Exception{
        homeClick();
        homebtn.setStyle("-fx-background-color: #00a693; -fx-text-fill: white;");
        String paneDesign = "-fx-background-color:linear-gradient(to bottom right, #272b3f, #256b52);";
        home.setStyle(paneDesign);
        borrow.setStyle(paneDesign);
        reservation.setStyle(paneDesign);
        userPane.setStyle(paneDesign);
        historyPane.setStyle(paneDesign);
        equipmentsPane.setStyle(paneDesign);
        logInPane.setStyle(paneDesign);
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

        add.setGraphic(cancelIcon);
        add.setContentDisplay(ContentDisplay.LEFT); 
        add.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon2 = new ImageView(new Image("icons/save.png")); // Replace with your icon file
        cancelIcon2.setFitHeight(30); 
        cancelIcon2.setFitWidth(30);

        save.setGraphic(cancelIcon2);
        save.setContentDisplay(ContentDisplay.LEFT); 
        save.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon3 = new ImageView(new Image("icons/confirm.png")); // Replace with your icon file
        cancelIcon3.setFitHeight(30); 
        cancelIcon3.setFitWidth(30);

        cons.setGraphic(cancelIcon3);
        cons.setContentDisplay(ContentDisplay.LEFT); 
        cons.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        add.setDisable(false);
        save.setDisable(true);
        
        
        
        ImageView cancelIcon4 = new ImageView(new Image("icons/home.png")); // Replace with your icon file
        cancelIcon4.setFitHeight(25); 
        cancelIcon4.setFitWidth(25);

        homebtn.setGraphic(cancelIcon4);
        homebtn.setContentDisplay(ContentDisplay.LEFT); 
        homebtn.setStyle("-fx-background-color:transparent; -fx-border-radius:10;");
        
        ImageView cancelIcon5 = new ImageView(new Image("icons/reserve.png")); // Replace with your icon file
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
}
