/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Blanc
 */
public class serialModel {
    private final StringProperty id;
    private final StringProperty serialNumber;

    // Constructor to initialize the properties
    public serialModel(String id, String serialNumber) {
        this.id = new SimpleStringProperty(id);
        this.serialNumber = new SimpleStringProperty(serialNumber);
    }

    
    public StringProperty idProperty() {return id;}
    public StringProperty serialNumberProperty() {return serialNumber;}

    // Getter methods
    public String getId() { return id.get(); }
    public String getserialNumber() { return serialNumber.get(); }

    // Setter methods
    public void setId(String a) { this.id.set(a); }
    public void setserialNumber(String a) { this.serialNumber.set(a); }

    
}
