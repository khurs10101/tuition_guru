package com.aec_developers.khurshid.tutionsearch.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Khurshid on 1/26/2018.
 */

public class Teacher implements Serializable {

    private int id;
    private String name, age, gender, email, phone, fee, qualification, field, subject, specific_subject, tclass, city, state, verified, totalCount;
    Bitmap teacherImage;


    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getVerified() {
        return verified;
    }


    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Bitmap getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(Bitmap teacherImage) {
        this.teacherImage = teacherImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSpecific_subject() {
        return specific_subject;
    }

    public void setSpecific_subject(String specific_subject) {
        this.specific_subject = specific_subject;
    }

    public String getTclass() {
        return tclass;
    }

    public void setTclass(String tclass) {
        this.tclass = tclass;
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
}
