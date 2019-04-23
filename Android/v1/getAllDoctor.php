<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
 
//if($_SERVER['REQUEST_METHOD']=='POST'){
	$db = new DbDoctorOperation();
	$doctor = $db->getAllDoctor();
	if(mysqli_num_rows($doctor)>0){
		//$response['error'] = false; 
        // $response['message'] = "No Doctor Information available";
		while($row = mysqli_fetch_assoc($doctor))
		{
			$response[]=$row;
		}
	}
// 	else {
// 		$response['error'] = true; 
//         $response['message'] = "No Doctor Information available";
// }
// }else {
// 		$response['error'] = true; 
//         $response['message'] = "Something went Wrong";
// }
echo json_encode($response);