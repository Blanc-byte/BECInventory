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
public class StockRecord {
    private final SimpleStringProperty id;
    private final SimpleStringProperty equipmentId;
    private final SimpleStringProperty equipmentName;
    private final SimpleStringProperty stocks;
    private final SimpleStringProperty dateAdded;

    public StockRecord(String id, String equipmentId, String equipmentName, String stocks, String dateAdded) {
        this.id = new SimpleStringProperty(id);
        this.equipmentId = new SimpleStringProperty(equipmentId);
        this.equipmentName = new SimpleStringProperty(equipmentName);
        this.stocks = new SimpleStringProperty(stocks);
        this.dateAdded = new SimpleStringProperty(dateAdded);
    }

    public StringProperty idProperty() { return id; }
    public StringProperty equipmentIdProperty() { return equipmentId; }
    public StringProperty equipmentNameProperty() { return equipmentName; }
    public StringProperty stocksProperty() { return stocks; }
    public StringProperty dateAddedProperty() { return dateAdded; }
}
