package com.restdemo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity

public class CloudVendor
{
    @Id
    private String vendorId;

    @NotNull(message = "Vendor Name should not be null")
    private String vendorName;

    @NotNull(message = "Vendor Address should not be null")
    private String vendorAddress;
    @Pattern(regexp = "^\\d{10}$",message = "Phone Number should be 10 digits")
    private String vendorPhoneNumber;

    public CloudVendor() {
    }

    public CloudVendor(String vendorId, String vendorName, String vendorAddress, String vendorPhoneNumber) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorAddress = vendorAddress;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }

    public void setVendorPhoneNumber(String vendorPhoneNumber) {
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    @Override
    public String toString() {
        return "CloudVendor{" +
                "vendorId='" + vendorId + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", vendorAddress='" + vendorAddress + '\'' +
                ", vendorPhoneNumber='" + vendorPhoneNumber + '\'' +
                '}';
    }
}
