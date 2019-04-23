<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST'){
	$db = new DbDoctorOperation();
	if(isset($_POST['doctor_email']) and isset($_POST['day_name']))
	if(mysqli_num_rows($doctor)>0){
		//$response['error'] = false; 
        // $response['message'] = "No Doctor Information available";
		while($row = mysqli_fetch_assoc($doctor))
		{
			$response[]=$row;
		}
	}
}
echo json_encode($response);