CREATE TABLE doctors(
	doctor_name varchar(60),
	doctor_email varchar(20),
	doctor_age varchar(3),
	doctor_gender varchar(8),
	doctor_phone_no varchar(11),
	doctor_area varchar(60),
	doctor_workplace varchar(30),
	doctor_catagory varchar(15),
	doctor_degree varchar(50),
	password varchar(60),
	PRIMARY KEY (doctor_email)
)
CREATE TABLE patients(
	patient_name varchar(60),
	patient_blood varchar(5),
	patient_email varchar(20),
	patient_age varchar(3),
	patient_gender varchar(8),
	patient_phone_no varchar(11),
	patient_area varchar(60),
	password varchar(60),
	PRIMARY KEY (patient_email)
)ENGINE='MyISAM';

CREATE TABLE recents( patient_email varchar(20), doctor_email varchar(20), FOREIGN KEY (patient_email) REFERENCES patients(patient_email), FOREIGN KEY (doctor_email) REFERENCES doctors(doctor_email) )ENGINE='MyISAM';

CREATE TABLE ambulence(
	hospital_name varchar(50),
	ambulence_number varchar(13)
)ENGINE='MyISAM';