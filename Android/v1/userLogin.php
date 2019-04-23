<?php 
 
require_once '../includes/DbOperation.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['patient_email']) and isset($_POST['password'])){
		$db = new DbOperation();

		if($db->userLogin($_POST['patient_email'],$_POST['password'])){
			$user = $db->getPatientByEmail($_POST['patient_email']);
			$response['error'] = false;
			$response['patient_email']=$user['patient_email'];
			$response['patient_name']=$user['patient_name'];
			$response['patient_blood']=$user['patient_blood'];
			$response['patient_age']=$user['patient_age'];
			$response['patient_gender']=$user['patient_gender'];
			$response['patient_phone_no']=$user['patient_phone_no'];
			$response['patient_area']=$user['patient_area'];
		}else {
			$response['error'] = true; 
        	$response['message'] = "Username or password is incorrect";
		}
	} else {
		$response['error'] = true; 
        $response['message'] = "Required fields are missing";
	}
}
echo json_encode($response);