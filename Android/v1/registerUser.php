<?php 
 
require_once '../includes/DbOperation.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
    if(isset($_POST['patient_name']) and
        isset($_POST['patient_blood']) and
        isset($_POST['patient_email']) and
        isset($_POST['patient_age']) and
        isset($_POST['patient_gender']) and
   	 	isset($_POST['patient_area']) and
        isset($_POST['patient_phone_no'])and 
		isset($_POST['password'])){    
 
        $db = new DbOperation(); 
 
        $result = $db->createPatients(  $_POST['patient_name'],
                                    $_POST['password'],
                                    $_POST['patient_email'],
                                    $_POST['patient_phone_no'],
                                    $_POST['patient_age'],
                                    $_POST['patient_gender'],
                                    $_POST['patient_area'],
                                    $_POST['patient_blood']
                                );
        if($result == 1){
            $response['error'] = false; 
            $response['message'] = "User registered successfully";
        }elseif($result == 2){
            $response['error'] = true; 
            $response['message'] = "Some error occurred please try again";          
        }elseif($result == 0){
            $response['error'] = true; 
            $response['message'] = "It seems you are already registered, please choose a different email and username";                     
        }
 
    }else{
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
}else{
    $response['error'] = true; 
    $response['message'] = "Invalid Request";
}
 
echo json_encode($response);