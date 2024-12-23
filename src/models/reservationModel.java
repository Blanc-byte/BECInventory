package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the details of a reservation.
 * This model is designed to work with JavaFX's binding properties.
 */
public class reservationModel {

    private final StringProperty reservationId;
    private final StringProperty userName;
    private final StringProperty equipmentName;
    private final StringProperty quantity;
    private final StringProperty dateReserve;

    // Constructor to initialize the properties
    public reservationModel(String reservationId, String userName, String equipmentName, String quantity, String dateReserve) {
        this.reservationId = new SimpleStringProperty(reservationId);
        this.userName = new SimpleStringProperty(userName);
        this.equipmentName = new SimpleStringProperty(equipmentName);
        this.quantity = new SimpleStringProperty(quantity);
        this.dateReserve = new SimpleStringProperty(dateReserve);
    }

    // Getter and Setter methods for the properties
    public StringProperty reservationIdProperty() { return reservationId; }
    public StringProperty userNameProperty() { return userName; }
    public StringProperty equipmentNameProperty() { return equipmentName; }
    public StringProperty quantityProperty() { return quantity; }
    public StringProperty dateReserveProperty() { return dateReserve; }

    public String getReservationId() { return reservationId.get(); }
    public void setReservationId(String reservationId) { this.reservationId.set(reservationId); }

    public String getUserName() { return userName.get(); }
    public void setUserName(String userName) { this.userName.set(userName); }

    public String getEquipmentName() { return equipmentName.get(); }
    public void setEquipmentName(String equipmentName) { this.equipmentName.set(equipmentName); }

    public String getQuantity() { return quantity.get(); }
    public void setQuantity(String quantity) { this.quantity.set(quantity); }

    public String getDateReserve() { return dateReserve.get(); }
    public void setDateReserve(String dateReserve) { this.dateReserve.set(dateReserve); }
}
