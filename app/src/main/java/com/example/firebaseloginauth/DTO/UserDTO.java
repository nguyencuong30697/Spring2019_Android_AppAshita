package com.example.firebaseloginauth.DTO;

import android.net.Uri;

public class UserDTO {
    private String id;
    private String email;
    private String displayname;
    private String phone;
    private String age;
    private String adress;
    private Boolean gender;
    private String information;
    private String photoUri;

    public UserDTO(){

    }

    public UserDTO(String id,String email, String displayname,String phone,String age,String adress,Boolean gender,String information,String photoUri){
        this.id = id;
        this.email = email;
        this.displayname = displayname;
        this.phone = phone;
        this.age = age;
        this.adress = adress;
        this.gender = gender;
        this.information = information;
        this.photoUri = photoUri;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
