package com.company.petadoptionapp;

public class NGO_Model {
    public String ngoName, ngoEmail, ngoRegNo, ngoPhone;

    NGO_Model(){

    }

    public NGO_Model(String ngoName, String ngoEmail, String ngoRegNo, String ngoPhone) {
        this.ngoName = ngoName;
        this.ngoEmail = ngoEmail;
        this.ngoRegNo = ngoRegNo;
        this.ngoPhone = ngoPhone;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getNgoEmail() {
        return ngoEmail;
    }

    public void setNgoEmail(String ngoEmail) {
        this.ngoEmail = ngoEmail;
    }

    public String getNgoRegNo() {
        return ngoRegNo;
    }

    public void setNgoRegNo(String ngoRegNo) {
        this.ngoRegNo = ngoRegNo;
    }

    public String getNgoPhone() {
        return ngoPhone;
    }

    public void setNgoPhone(String ngoPhone) {
        this.ngoPhone = ngoPhone;
    }
}
