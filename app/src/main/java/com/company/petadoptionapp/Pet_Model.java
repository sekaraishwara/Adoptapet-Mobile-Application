package com.company.petadoptionapp;

public class Pet_Model {
    private String PetAge,PetBreed,PetName,ImageUrl,PetType,PetGender,PetAbout,PetUser,PetAddress,City,Country,State;
    private double Latitude,Longitude;
    Pet_Model(){

    }

    public Pet_Model(String petAge, String petBreed, String petName, String imageUrl, String petType, String petGender, String petAbout, String petUser ,String petCountry, String petCity, String petState, double petLongitude, double petLatitude) {
        PetAge = petAge;
        PetBreed = petBreed;
        PetName = petName;
        ImageUrl = imageUrl;
        PetType = petType;
        PetGender = petGender;
        PetAbout = petAbout;
        PetUser = petUser;
        Country = petCountry;
        City = petCity;
        State = petState;
        Longitude = petLongitude;
        Latitude = petLatitude;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getPetAge() {
        return PetAge;
    }

    public void setPetAge(String petAge) {
        PetAge = petAge;
    }

    public String getPetBreed() {
        return PetBreed;
    }

    public void setPetBreed(String petBreed) {
        PetBreed = petBreed;
    }

    public String getPetAddress() {
        return PetAddress;
    }

    public void setPetAddress(String petAddress) {
        PetAddress = petAddress;
    }

    public String getPetName() {
        return PetName;
    }

    public void setPetName(String petName) {
        PetName = petName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPetType() {
        return PetType;
    }

    public void setPetType(String petType) {
        PetType = petType;
    }

    public String getPetGender() {
        return PetGender;
    }

    public void setPetGender(String petGender) {
        PetGender = petGender;
    }

    public String getPetAbout() {
        return PetAbout;
    }

    public void setPetAbout(String petAbout) {
        PetAbout = petAbout;
    }

    public String getPetUser() {
        return PetUser;
    }

    public void setPetUser(String petUser) {
        PetUser = petUser;
    }
}
