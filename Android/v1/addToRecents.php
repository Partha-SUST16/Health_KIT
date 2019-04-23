<?php 
 
require_once '../includes/DbOperation.php';
 
$response = array(); 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
	if(isset($_POST['patient_email']) and isset($_POST['doctor_email']))
	{
		$db = new DbOperation();
		$result=$db->addToRecent($_POST['patient_email'],$_POST['doctor_email']);
		if($result == 1){
            $response['error'] = false; 
            $response['message'] = "Doctor Added successfully";
        }elseif($result == 2){
            $response['error'] = true; 
            $response['message'] = "Some error occurred please try again";          
        }elseif($result == 0){
            $response['error'] = true; 
            $response['message'] = "It seems doctor in already in database";                     
        }
	}else{
        $response['error'] = true; 
        $response['message'] = "Required fields are missing";
    }
}
echo json_encode($response);