
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author estal
 */
public class borrowModel {
    private final StringProperty id;
    private final StringProperty userName;
    private final StringProperty equipmentName;
    private final StringProperty serial_id;
    private final StringProperty status;
    private final StringProperty borrowed_at;

    // Constructor to initialize the properties
    public borrowModel(String id, String userName, String equipmentName, String serial_id, String borrowed_at, String status) {
        this.id = new SimpleStringProperty(id);
        this.userName = new SimpleStringProperty(userName);
        this.equipmentName = new SimpleStringProperty(equipmentName);
        this.serial_id = new SimpleStringProperty(serial_id);
        this.status = new SimpleStringProperty(status);
        this.borrowed_at = new SimpleStringProperty(borrowed_at);
    }

    
    public StringProperty idProperty() {return id;}
    public StringProperty user_idProperty() {return userName;}
    public StringProperty equipmentNameProperty() {return equipmentName;}
    public StringProperty serial_idProperty() {return serial_id;}
    public StringProperty statusProperty() {return status;}
    public StringProperty borrowed_atProperty() {return borrowed_at;}

    // Getter methods
    public String getId() { return id.get(); }
    public String getUserName() { return userName.get(); }
    public String getEquipmentName() { return equipmentName.get(); }
    public String getSerial_id() { return serial_id.get(); }
    public String getStatus() { return status.get(); }
    public String getBorrowed_at() { return borrowed_at.get(); }

    // Setter methods
    public void setId(String id) { this.id.set(id); }
    public void setUserName(String user_id) { this.userName.set(user_id); }
    public void setEquipmentName(String equipment_id) { this.equipmentName.set(equipment_id); }
    public void setSerial_id(String quantity) { this.serial_id.set(quantity); }
    public void setStatus(String status) { this.status.set(status); }
    public void setBorrowed_at(String borrowed_at) { this.borrowed_at.set(borrowed_at); }

    
}