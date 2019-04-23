<?php
	require_once '../includes/DbDoctorOperation.php';
	$response = array(); 
	if($_SERVER['REQUEST_METHOD']=='POST'){
		if(isset($_POST['doctor_name']) and
			isset($_POST['doctor_email']) and
			isset($_POST['doctor_age']) and
			isset($_POST['doctor_gender']) and
			isset($_POST['doctor_phone_no']) and
			isset($_POST['doctor_area']) and
			isset($_POST['doctor_workplace']) and
			isset($_POST['doctor_catagory']) and
			isset($_POST['doctor_degree']) and
			isset($_POST['password']) ){

			 $db = new DbDoctorOperation(); 
			 $result = $db->createDoctor($_POST['doctor_name'],
                                    $_POST['password'],
                                    $_POST['doctor_email'],
                                    $_POST['doctor_phone_no'],
                                    $_POST['doctor_age'],
                                    $_POST['doctor_gender'],
                                    $_POST['doctor_area'],
                                	$_POST['doctor_workplace'],
                                	$_POST['doctor_catagory'],
                                	$_POST['doctor_degree']);

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

		}else {
			 $response['error'] = true; 
        	$response['message'] = "Required fields are missing";
		}
	} else {
		$response['error'] = true; 
    	$response['message'] = "Invalid Request";
	}
echo json_encode($response);