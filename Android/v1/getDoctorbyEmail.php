<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['doctor_email']) ){
		$db = new DbDoctorOperation();

		$user = $db->getDoctorByEmail($_POST['doctor_email']);
		$response['error'] = false;
		$response['doctor_email']=$user['doctor_email'];
		$response['doctor_name']=$user['doctor_name'];
		$response['doctor_catagory']=$user['doctor_catagory'];
		$response['doctor_age']=$user['doctor_age'];
		$response['doctor_gender']=$user['doctor_gender'];
		$response['doctor_phone_no']=$user['doctor_phone_no'];
		$response['doctor_area']=$user['doctor_area'];
		$response['doctor_workplace']=$user['doctor_workplace'];
		$response['doctor_degree']=$user['doctor_degree'];
	} else {
		$response['error'] = true; 
        $response['message'] = "Required fields are missing";
	}
}
echo json_encode($response);