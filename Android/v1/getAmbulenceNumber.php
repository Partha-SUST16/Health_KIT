<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST')
{
	if(isset($_POST['hospital_name'])){
		$db = new DbDoctorOperation();
		$user = $db->getAmbulenceNumberofHospital($_POST['hospital_name']);
		$response['error']=false;
		$response['ambulence_number'] = $user['ambulence_number'];
		$response['hospital_name']=$user['hospital_name'];
	}
	else{
		$response['error'] = true;
		$response['message']='required field are missing';
	}
}

echo json_encode($response);