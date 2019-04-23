<?php

	/**
	 * 
	 */
	class DbDoctorOperation 
	{
		private $con; 
 
        function __construct(){
 
            require_once dirname(__FILE__).'/Dbconnect.php';
 
            $db = new Dbconnect();
 
            $this->con = $db->connect();
 			
        }
		public function createDoctor($username, $pass, $email,$phone,$age,$gender,$area,$workplace,$catagory,$degree){
			if($this->isDoctorExist($email)){
                return 0; 
            }else{
            	$password = md5($pass);
            	$stmt = $this->con->prepare("INSERT INTO `doctors` (`doctor_name`,`doctor_email`,`doctor_age`,`doctor_gender`,`doctor_phone_no`,`doctor_area`,`doctor_workplace`,`doctor_catagory`,`doctor_degree`,`password`) VALUES (?,?,?,?,?,?,?,?,?,?);");
            	 $stmt->bind_param("ssssssssss",$username, $email, $age,$gender,$phone,$area,$workplace,$catagory,$degree,$password);
 				
                if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
            }
		}
		public function userLogin($email,$pass){
        	$password = md5($pass);
        	$stmt = $this->con->prepare("SELECT doctor_email FROM doctors WHERE doctor_email=? AND password=?");
        	$stmt->bind_param("ss",$email,$password);
        	$stmt->execute();
        	$stmt->store_result();
        	return $stmt->num_rows > 0;
        }
        public function getCon(){
        	return $this->con;
        }
		public function getDoctorByEmail($email){
            $stmt = $this->con->prepare("SELECT * FROM doctors WHERE doctor_email = ?");
            $stmt->bind_param("s",$email);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();
        }
		private function isDoctorExist( $email){
            $stmt = $this->con->prepare("SELECT doctor_email FROM doctors WHERE doctor_email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
        }
        public function getAllDoctor(){
        	$stmt = $this->con->prepare("SELECT * FROM doctors");
        	$stmt->execute();
        	return $stmt->get_result();
        }
        public function getDoctorByArea($area){
        	$stmt = $this->con->prepare("SELECT * FROM doctors WHERE doctor_area= ? ");
        	$stmt->bind_param("s",$area);
        	$stmt->execute();
        	return $stmt->get_result();
        }
        public function getDoctorByName($name){
        	$stmt = $this->con->prepare("SELECT * FROM doctors WHERE doctor_name= ? ");
        	$stmt->bind_param("s",$name);
        	$stmt->execute();
        	return $stmt->get_result();
        }
        public function getDoctorByCatagory($catagory){
        	$stmt = $this->con->prepare("SELECT * FROM doctors WHERE doctor_catagory LIKE ? ");
        	$stmt->bind_param("s",$catagory);
        	$stmt->execute();
        	return $stmt->get_result();
        }
        public function getDoctorByAreaCatagory($area,$catagory){
        	$stmt = $this->con->prepare("SELECT * FROM doctors WHERE doctor_area LIKE ? AND doctor_catagory LIKE ? ");
        	$stmt->bind_param("ss",$area,$catagory);
        	$stmt->execute();
        	return $stmt->get_result();
        }
        public function setAppointmentField($date,$day_name,$doctor_email,$start_time,$end_time,$patient_no){
        	$stmt=$this->con->prepare("INSERT INTO `appointment` (`doctor_email`,`date`,`day_name`,`start_time`,`end_time`,`patient_no`)
        					VALUES (?,?,?,?,?,?)");
        	$stmt->bind_param("ssssss",$doctor_email,$date,$day_name,$start_time,$end_time,$patient_no);
        	if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
        }
        public function getDoctorbyHospital($doctor_workplace){
        	$stmt = $this->con->prepare("SELECT * FROM `doctors` WHERE doctor_workplace=? ");
        	$stmt->bind_param("s",$doctor_workplace);
        	$stmt->execute();

        	return $stmt->get_result();
        }
        public function deleteAppointment($date,$day_name,$doctor_email,$patient_no){
        	$stmt = $this->con->prepare("UPDATE `appointment` SET `patient_email`=NULL WHERE patient_no=? AND day_name=? AND date=? doctor_email=?");
        	$stmt->bind_param("ssss",$patient_no,$day_name,$date,$doctor_email);
        	if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
        }
        public function addPatientToAppointment($doctor_email,$patient_no,$day_name,$date,$patient_email)
        {
        	$stmt = $this->con->prepare("UPDATE `appointment` SET `patient_email`=? WHERE patient_no=? AND day_name=? AND date=? doctor_email=?");
        	$stmt->bind_param("sssss",$patient_email,$patient_no,$day_name,$date,$doctor_email);
        	if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
        }
        public function getAllAppointmentDay($doctor_email,$day_name){
        	$stmt = $this->con->prepare("SELECT * FROM appointment WHERE doctor_email=? and day_name=?");
        	$stmt->bind_param("ss",$doctor_email,$day_name);
        	$stmt->execute();
        	return $stmt->get_result();
        }
        public function deleteWholeAppointment($date,$day_name,$doctor_email,$patient_no){
        	$stmt = $this->con->prepare("DELETE * FROM appointment WHERE patient_no=? AND day_name=? AND date=? doctor_email=?");
        	$stmt->bind_param("ssss",$patient_no,$day_name,$date,$doctor_email);
        	if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
        }
        public function getAmbulenceNumberofHospital($hospital_name){
        	$stmt = $this->con->prepare("SELECT * FROM `ambulence` WHERE hospital_name=?");
        	$stmt->bind_param("s",$hospital_name);
        	$stmt->execute();
        	return $stmt->get_result()->fetch_assoc();
        }

	}