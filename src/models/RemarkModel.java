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
public class RemarkModel {
    private final SimpleStringProperty remark;
    private final SimpleStringProperty date;

    public RemarkModel(String remark, String date) {
        this.remark = new SimpleStringProperty(remark);
        this.date = new SimpleStringProperty(date);
    }

    public StringProperty remarkProperty() { return remark; }
    public StringProperty dateProperty() { return date; }
}
