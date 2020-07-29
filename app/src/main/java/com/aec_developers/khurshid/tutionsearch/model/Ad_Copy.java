package com.aec_developers.khurshid.tutionsearch.model;

import java.io.Serializable;

/**
 * Created by Khurshid on 2/19/2018.
 */

public class Ad_Copy implements Serializable {
    private String name, description, phone1, phone2, phone3, phone4, email, address, city, state, adImageUrl;

    public Ad_Copy() {

    }


    public Ad_Copy(Ad_Copy copy) {
        name = copy.name;
        description = copy.description;
        phone1 = copy.phone1;
        phone2 = copy.phone2;
        phone3 = copy.phone3;
        phone4 = copy.phone4;
        email = copy.email;
        address = copy.address;
        city = copy.city;
        state = copy.state;
        adImageUrl = copy.adImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getPhone4() {
        return phone4;
    }

    public void setPhone4(String phone4) {
        this.phone4 = phone4;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdImageUrl() {
        return adImageUrl;
    }

    public void setAdImageUrl(String adImageUrl) {
        this.adImageUrl = adImageUrl;
    }
}
