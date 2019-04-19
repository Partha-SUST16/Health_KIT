package com.example.healthkit;

public class Constants {
    private static final String ROOT_URL = "http://10.101.1.21/Android/v1/";
    public static final String URL_PATIENTS_REGISTER = ROOT_URL+"registerUser.php";
    public static final String URL_DOCTORS_REGISTER = ROOT_URL+"registerDoctor.php";
    //public static final String URL_PATIENTS_REGISTER = ROOT_URL+"re"
    public static final String URL_LOGIN = ROOT_URL+"userLogin.php";
    public static final String URL_DOCTOR_LOGIN = ROOT_URL+"doctorLogin.php";
    public static final String URL_GET_ALL_DOCTOR = ROOT_URL+"getAllDoctor.php";

    public static final String URL_GET_DOCTOR_by_AREA = ROOT_URL+"getDoctorbyArea.php";
    public static final String URL_GET_DOCTOR_by_CATAGORY = ROOT_URL+"getDoctorbyCatagory.php";
    public static final String URL_GET_DOCTOR_FILTER = ROOT_URL+"getDoctorbyAreaCatagory.php";
    public static final String URL_GET_RECENT_DOCTOR = ROOT_URL+"getRecentDoctors.php";
    public static final String URL_SET_TO_APPOINTMENT = ROOT_URL+"addToRecents.php";
    public static final String URL_GET_DOCTOR_by_EMAIL = ROOT_URL+"getDoctorbyEmail.php";
    public static final String URL_SET_APPOINTMENT_DOCTOR=ROOT_URL+"addAppointmentField.php";
    public static final String URL_DELETE_APPOINTMENT = ROOT_URL+"deleteAppointment.php";
    public static final String URL_GET_ALL_APPOINTMENT = ROOT_URL+"getAllAppointment.php";
    public static final String URL_GET_by_NAME = ROOT_URL+"getDoctorbyName.php";
    public static final String URL_GET_by_HOSPITAL = ROOT_URL+"getDoctorbyHospital.php";
    public static final String URL_GET_AMBULENCE = ROOT_URL+"getAmbulenceNumber.php";
}
