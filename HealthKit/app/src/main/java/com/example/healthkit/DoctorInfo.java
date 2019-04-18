package com.example.healthkit;

public class DoctorInfo {
    public String name,gender,age,area,phone,email,catagory,workplace,degree,filter;
    public DoctorInfo(){

    }

    public DoctorInfo(String name, String gender, String age, String area, String phone, String email, String catagory, String workplace, String degree, String filter) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.area = area;
        this.phone = phone;
        this.email=email;
        this.catagory = catagory;
        this.workplace = workplace;
        this.degree = degree;
        this.filter = filter;
    }

    public DoctorInfo(String name, String gender, String age, String area, String phone, String email, String catagory, String workplace, String degree) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.area = area;
        this.phone = phone;
        this.email = email;
        this.catagory = catagory;
        this.workplace = workplace;
        this.degree = degree;
    }
}
