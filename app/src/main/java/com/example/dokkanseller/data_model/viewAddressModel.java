package com.example.dokkanseller.data_model;

import java.io.Serializable;

public class viewAddressModel implements Serializable {
    public String customerID;
    public String customerName;
    public String customerAddress;
    public String customerCountry;
    public String customerNumber;

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String customerImage;

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public viewAddressModel() {
    }

    public viewAddressModel(String customerName, String customerAddress, String customerCountry, String customerNumber) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerCountry = customerCountry;
        this.customerNumber = customerNumber;
    }
}
