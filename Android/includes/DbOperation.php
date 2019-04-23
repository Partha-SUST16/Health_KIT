<?php
	/**
	 * 
	 */
	class DbOperation 
	{
		private $con; 
 
        function __construct(){
 
            require_once dirname(__FILE__).'/Dbconnect.php';
 
            $db = new Dbconnect();
 
            $this->con = $db->connect();
 
        }
 
        /*CRUD -> C -> CREATE */
 
        public function createPatients($username, $pass, $email,$phone,$age,$gender,$area,$blood){
            if($this->isPatientExist($email)){
                return 0; 
            }else{
                $password = md5($pass);
                $stmt = $this->con->prepare("INSERT INTO `patients` ( `patient_name`, `patient_blood`, `patient_email`
            								,`patient_age`,`patient_gender`,`patient_phone_no`,`patient_area`,`password`) VALUES ( ?, ?, ?,?,?,?,?,?);");
                $stmt->bind_param("ssssssss",$username, $blood, $email,$age,$gender,$phone,$area,$password);
 
                if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
            }
        }
 
       
        public function userLogin($email,$pass){
        	$password = md5($pass);
        	$stmt = $this->con->prepare("SELECT patient_email FROM patients WHERE patient_email=? AND password=?");
        	$stmt->bind_param("ss",$email,$password);
        	$stmt->execute();
        	$stmt->store_result();
        	return $stmt->num_rows > 0;
        }
 
        public function getPatientByEmail($email){
            $stmt = $this->con->prepare("SELECT * FROM patients WHERE patient_email = ?");
            $stmt->bind_param("s",$email);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();
        }
         
         public function addToRecent($patient_email,$doctor_email){
         	if($this->ifDoctorExist($patient_email,$doctor_email)){
                return 0; 
            }
            else {
            	$stmt = $this->con->prepare("INSERT INTO `recents` (`patient_email`,`doctor_email`) VALUES (?,?)");
            	$stmt->bind_param("ss",$patient_email,$doctor_email);

                if($stmt->execute()){
                    return 1; 
                }else{
                    return 2; 
                }
            }
         }
         public function getRecentDoctor($patient_email){
         	$stmt = $this->con->prepare("SELECT * FROM doctors,recents WHERE doctors.doctor_email=recents.doctor_email AND recents.patient_email=? ");
         	$stmt->bind_param("s",$patient_email);
         	$stmt->execute();
         	return $stmt->get_result();
         }
         private function ifDoctorExist($patient_email,$doctor_email){
         	$stmt = $this->con->prepare("SELECT doctor_email FROM recents WHERE  patient_email=? AND doctor_email = ? ");
            $stmt->bind_param("ss", $patient_email,$doctor_email);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
         }
 
        private function isPatientExist( $email){
            $stmt = $this->con->prepare("SELECT patient_email FROM patients WHERE patient_email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0; 
        }
 
	}
