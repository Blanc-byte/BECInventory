/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

//import controllers.studentController;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.User;

/**
 *
 * @author Administrator
 */
public class authController {
    final String DB_URL = "jdbc:mysql://localhost/becinventory";
    final String USER = "root";
    final String PASS = "";
    public String url;
    public Connection con = null;
    
    ObservableList<String> years = FXCollections.observableArrayList("1", "2", "3", "4");
    ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
    ObservableList<String> sections = FXCollections.observableArrayList("A", "B", "C", "D", "E", "F");
    ObservableList<String> courses = FXCollections.observableArrayList("BSIT", "BSBA", "BSA", "BTLEd");
    
    String typeOfUser = "", setUsername="ouch";
    
    public void initialize()throws Exception{
        connect();
        year.setItems(years);
        section.setItems(sections);
        department.setItems(courses);
        gender.setItems(genders);
        loadStudents();
    }
    
    
    ObservableList<User> users = FXCollections.observableArrayList();
    ObservableList<String> usernames = FXCollections.observableArrayList();
    ObservableList<String> passwords = FXCollections.observableArrayList();
    public void loadStudents()throws Exception{
        users.clear();
        java.sql.Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
        while(resultSet.next()){
            String i1 = resultSet.getString("id");
            String i2 = resultSet.getString("school_id");
            String i3 = resultSet.getString("fname");
            String i4 = resultSet.getString("mname");
            String i5 = resultSet.getString("lname");
            String i6 = resultSet.getString("suffix");
            String i7 = resultSet.getString("gender");
            String i8 = resultSet.getString("username");
            String i9 = resultSet.getString("password");
            String i10 = resultSet.getString("department");
            String i11 = resultSet.getString("year_section");
            String i12 = resultSet.getString("role");
            users.add(new User(i1,i2,i3,i4,i5,i6,i7,i8,i9,i10, i11, i12));
            usernames.add(i8);
            passwords.add(i9);
        }
    }
    public boolean checkregistrarauth()throws Exception{
        String adminAd="ADMIN", passAd="ADMIN";
        if(adminAd.equals(user.getText()) && passAd.equals(pass.getText())){
            return true;
        }
        return false;
    }
    
    @FXML 
    public void adminLogin(MouseEvent  event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/stage.fxml"));
                Parent root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();

                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                currentStage.close();
        }
    
    public void logIn(ActionEvent event)throws Exception{
        if(!user.getText().equals("") && !pass.getText().equals("")){
            boolean logIned = true;
            if(checkregistrarauth()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/stage.fxml"));
                Parent root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();

                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                currentStage.close();
                logIned=false;
            }else{
                for(User usered: users){
                    if(usered.getUsername().contains(user.getText()) && usered.getPassword().contains(pass.getText())){
                        writeToFile(usered.getId() + "", usered.getUsername());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/users/UserScene.fxml"));
                        Parent root = loader.load();

                        // Apply the stylesheet programmatically
                        root.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                        Stage newStage = new Stage();
                        newStage.setScene(new Scene(root));
                        newStage.show();

                        // Close the current stage
                        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                        currentStage.close();

                        

                        logIned=false;
                    }
                }
            }
            
            if(logIned){
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!!!");
            }
        
        }else{
            JOptionPane.showMessageDialog(null, "FILL IN ALL THE INFORMATION BEFORE PROCEEDING");
            
        }

    }
    public void writeToFile(String userId, String username) throws Exception{
        // Define the path to the text file
        String filePath = "C:/Users/estal/Documents/NetBeansProjects/mavenproject1/BECInventory/src/id.txt";  // Path to save the file (can be adjusted)

        // Prepare the data you want to write to the file
        String dataToWrite = userId;

        // Write to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
        writer.write(dataToWrite);
        System.out.println("Login data written to file successfully.");
        writer.close();
        
    }
    @FXML private TextField user, fname, mname, lname, suffix, schoolID, role;
    @FXML private PasswordField pass;
    @FXML private ChoiceBox year,section, department, gender;
    public boolean checkThe1stPhase()throws Exception{
        if(fname.getText().equals("") || mname.getText().equals("") || lname.getText().equals("") ||
           suffix.getText().equals("") || year.getValue()==null || section.getValue()==null || schoolID.getText().equals("")
                 || department.getValue()==null || gender.getValue()==null){
            if(typeOfUser.equals("student")) {
                    JOptionPane.showMessageDialog(null, "FILL IN ALL THE INFORMATION BEFORE PROCEEDING");
                    return false;
            }
        }
        
        if (!fname.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "First name must contain only letters, no numbers or special characters.");
            return false;
        }
        if (!lname.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Lastzzz name must contain only letters, no numbers or special characters.");
            return false;
        }
        if (!mname.getText().isEmpty() && !mname.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Middle name must contain only letters, no numbers or special characters.");
            return false;
        }
        if (!suffix.getText().isEmpty() && !suffix.getText().matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(null, "Suffix must contain only letters, no numbers or special characters.");
            return false;
        }
        return true;
    }
    @FXML private TextField userRegister,passRegister,passConfirmRegister;
    public boolean checkThe2ndPhase()throws Exception{
        if(userRegister.getText().equals("") || passRegister.getText().equals("") || passConfirmRegister.getText().equals("")){
            JOptionPane.showMessageDialog(null, "FILL IN ALL THE INFORMATION BEFORE PROCEEDING");
            return false;
        }
        return true;
    }
    public void createAccount(ActionEvent event)throws Exception{
        if(checkThe2ndPhase()){
            if(usernames.contains(userRegister.getText())){
                JOptionPane.showMessageDialog(null, "USERNAME already taken, try again!!!");
            }else if(!passRegister.getText().equals(passConfirmRegister.getText())){
                JOptionPane.showMessageDialog(null, "PASSWORD dont match, try again!!!");
            }else{
                loadRequest();
                setUsername=userRegister.getText();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/users/UserScene.fxml"));
                Parent root = loader.load();

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
                
                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
        }
    }
    public void loadRequest()throws Exception{
        String insertQuery = "INSERT INTO `users` (`fname`, `mname`, `lname`, `year_section`, `department`, `suffix`, `gender`, `school_id`, `username`, `password`, `role`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
       // java.sql.Statement statementq = con.createStatement();
       // statement.executeUpdate(insertQuery);
       String yearSection = "";
       if(typeOfUser.equals("student")){
           yearSection = year.getValue()+""+section.getValue();
       }else if(typeOfUser.equals("staff")){
           yearSection ="N/A";
       }

       
         try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
            stmt.setString(1, fname.getText());
            stmt.setString(2, mname.getText());
            stmt.setString(3, lname.getText());
            stmt.setString(4, yearSection);
            stmt.setString(5, department.getValue()+"");
            stmt.setString(6, suffix.getText() != null ? suffix.getText() : ""); // Handle null suffix
            stmt.setString(7, gender.getValue()+""); // Get selected value from ChoiceBox
            stmt.setString(8, schoolID.getText());
            stmt.setString(9, userRegister.getText());
            stmt.setString(10, passConfirmRegister.getText());
            stmt.setString(11, typeOfUser);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Successfully Create User");
            } else {
                JOptionPane.showMessageDialog(null, "No Found found ");
            }
         } catch (Exception e) {
             
         }
        loadStudents();
        for(User usered: users){
            if(usered.getUsername().contains(userRegister.getText()) && usered.getPassword().contains(passRegister.getText())){
                writeToFile(usered.getId() + "", usered.getUsername());
            }
        }
    }
    @FXML private Pane logIn, signUp, typeOf, signUp2ndPhase;
    public void logInShow(){
        logIn.setVisible(true);
        signUp.setVisible(false);

    }
    public void signUpShowInternal()throws Exception{
        signUp.setVisible(true);
        typeOf.setVisible(false);
        typeOfUser = "student";
    }
    public void signUpShowExternal()throws Exception{
        signUp.setVisible(true);
        typeOf.setVisible(false);
        department.setDisable(false);
        section.setDisable(true);
        year.setDisable(true);
//        schoolID.setDisable(true);
        typeOfUser = "staff";
    }
    public void signUp2ndPhaseShow()throws Exception{
        if(checkThe1stPhase()){
            signUp.setVisible(false);
            signUp2ndPhase.setVisible(true);
        }
    }
    public void typeOfShow(){
        logIn.setVisible(false);
        typeOf.setVisible(true);
        
    }
    public Connection connect() {
        url = "jdbc:mysql://localhost:3306/becsystem";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error");
        }
        return con;
    
    }
    
    // Method to handle the back button click
    @FXML
    public void handleBackButtonClick(ActionEvent event) throws Exception {
        // Check which pane you want to go back to
        if (signUp.isVisible()) {
            // Go back to typeOf from signUp
            signUp.setVisible(false);
            typeOf.setVisible(true);
        } else if (typeOf.isVisible()) {
            // Go back to login from typeOf
            logIn.setVisible(true);
            typeOf.setVisible(false);
        }
    }

    
    
}
