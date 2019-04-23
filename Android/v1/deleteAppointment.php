<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array();
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['doctor_email']) and isset($_POST['date']) and isset($_POST['patient_no'] )and isset($_POST['day_name'] )){
		$db = new DbDoctorOperation();
		if($db->deleteAppointment($_POST['date'],$_POST['day_name'],$_POST['doctor_email'],$_POST['patient_no'])==1){
			$response['error']=false;
			$response['message']='Free REservation';
		}
		else {
			$response['error']=true;
			$response['message']='Error Occured';
		}
	}
	else {
		$response['error']=true;
			$response['message']='Field missing';
	}
} elseif($_SERVER['REQUEST_METHOD']=='GET'){
	if(isset($_GET['doctor_email']) and isset($_GET['day_name']) and isset($_GET['date']) and isset($_GET['patient_no'])){
		$db =new DbDoctorOperation();

		if($db->deleteWholeAppointment($_GET['date'],$_GET['day_name'],$_GET['doctor_email'],$_GET['patient_no'])==1){
			$response['error']=false;
			$response['message']='Delete REservation';
		}
		else {
			$response['error']=true;
			$response['message']='Error Occured';
		}
	}
}
echo json_encode($response);