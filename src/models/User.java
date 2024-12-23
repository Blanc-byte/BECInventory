package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

    private final StringProperty id;
    private final StringProperty schoolId;
    private final StringProperty fname;
    private final StringProperty mname;
    private final StringProperty lname;
    private final StringProperty suffix;
    private final StringProperty department;
    private final StringProperty yearSection;
    private final StringProperty role;

    // Constructor
    public User(String id, String schoolId, String fname, String mname, String lname, String suffix, String department, String yearSection, String role) {
        this.id = new SimpleStringProperty(id);
        this.schoolId = new SimpleStringProperty(schoolId);
        this.fname = new SimpleStringProperty(fname);
        this.mname = new SimpleStringProperty(mname);
        this.lname = new SimpleStringProperty(lname);
        this.suffix = new SimpleStringProperty(suffix);
        this.department = new SimpleStringProperty(department);
        this.yearSection = new SimpleStringProperty(yearSection);
        this.role = new SimpleStringProperty(role);
    }

    // Getters and Setters with StringProperty for binding
    public StringProperty idProperty() {
        return id;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty schoolIdProperty() {
        return schoolId;
    }

    public String getSchoolId() {
        return schoolId.get();
    }

    public void setSchoolId(String schoolId) {
        this.schoolId.set(schoolId);
    }

    public StringProperty fnameProperty() {
        return fname;
    }

    public String getFname() {
        return fname.get();
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public StringProperty mnameProperty() {
        return mname;
    }

    public String getMname() {
        return mname.get();
    }

    public void setMname(String mname) {
        this.mname.set(mname);
    }

    public StringProperty lnameProperty() {
        return lname;
    }

    public String getLname() {
        return lname.get();
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public StringProperty suffixProperty() {
        return suffix;
    }

    public String getSuffix() {
        return suffix.get();
    }

    public void setSuffix(String suffix) {
        this.suffix.set(suffix);
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getDepartment() {
        return department.get();
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public StringProperty yearSectionProperty() {
        return yearSection;
    }

    public String getYearSection() {
        return yearSection.get();
    }

    public void setYearSection(String yearSection) {
        this.yearSection.set(yearSection);
    }

    public StringProperty roleProperty() {
        return role;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }
}