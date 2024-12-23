/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Administrator
 */
public class equipmentModel {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty stock;
    private final StringProperty available;
    private final StringProperty dateCreated;

    public equipmentModel(String id, String name, String stock, String available, String dateCreated) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.stock = new SimpleStringProperty(stock);
        this.available = new SimpleStringProperty(available);
        this.dateCreated = new SimpleStringProperty(dateCreated);
    }

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty stockProperty() {
        return stock;
    }

    public StringProperty availableProperty() {
        return available;
    }

    public StringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStock() {
        return stock.get();
    }

    public void setStock(String stock) {
        this.stock.set(stock);
    }

    public String getAvailable() {
        return available.get();
    }

    public void setAvailable(String available) {
        this.available.set(available);
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }
}