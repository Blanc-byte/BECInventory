package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the details of borrowed equipment.
 * This model is designed to work with JavaFX's binding properties.
 */
public class reservation {

    private final StringProperty id;
    private final StringProperty fullname;
    private final StringProperty department;
    private final StringProperty yearSection;
    private final StringProperty role;
    private final StringProperty equipmentName;
    private final StringProperty reserveAt;
    private final StringProperty quantity;
    private final StringProperty userid;
    private final StringProperty equipmentid;

    // Constructor to initialize the properties
    public reservation(String id, String fullname, String department, String yearSection, 
                                  String role, String equipmentName, String borrowedAt, String quantity
                                 , String userid, String equipmentid) {
        this.id = new SimpleStringProperty(id);
        this.fullname = new SimpleStringProperty(fullname);
        this.department = new SimpleStringProperty(department);
        this.yearSection = new SimpleStringProperty(yearSection);
        this.role = new SimpleStringProperty(role);
        this.equipmentName = new SimpleStringProperty(equipmentName);
        this.reserveAt = new SimpleStringProperty(borrowedAt);
        this.quantity = new SimpleStringProperty(quantity);
        this.userid = new SimpleStringProperty(userid);
        this.equipmentid = new SimpleStringProperty(equipmentid);
    }

    // Getter and Setter methods for the properties
    public StringProperty idProperty() { return id; }
    public StringProperty fullnameProperty() { return fullname; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty yearSectionProperty() { return yearSection; }
    public StringProperty roleProperty() { return role; }
    public StringProperty equipmentNameProperty() { return equipmentName; }
    public StringProperty reserveAtProperty() { return reserveAt; }
    public StringProperty quantityProperty() { return quantity; }
    

    // Standard getter and setter methods for each property
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getFullname() { return fullname.get(); }
    public void setFullname(String fullname) { this.fullname.set(fullname); }

    public String getDepartment() { return department.get(); }
    public void setDepartment(String department) { this.department.set(department); }

    public String getYearSection() { return yearSection.get(); }
    public void setYearSection(String yearSection) { this.yearSection.set(yearSection); }

    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }

    public String getEquipmentName() { return equipmentName.get(); }
    public void setEquipmentName(String equipmentName) { this.equipmentName.set(equipmentName); }

    public String getreserveAt() { return reserveAt.get(); }
    public void setreserveAt(String borrowedAt) { this.reserveAt.set(borrowedAt); }
    
    public String getquantity() { return quantity.get(); }
    public void setquantity(String quantity) { this.quantity.set(quantity); }

    public String getuserid() { return userid.get(); }
    public void setuserid(String borrowedAt) { this.userid.set(borrowedAt); }
    
    public String getequipmentid() { return equipmentid.get(); }
    public void setequipmentid(String quantity) { this.equipmentid.set(quantity); }
}
