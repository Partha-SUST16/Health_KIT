<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST')
{
	if(isset($_POST['doctor_area']))
	{
		$db = new DbDoctorOperation();
		$doctor = $db->getDoctorByArea($_POST['doctor_area']);
		if(mysqli_num_rows($doctor)>0)
		{
			//$response['error'] = false; 
        // $response['message'] = "No Doctor Information available";
			while($row = mysqli_fetch_assoc($doctor))
			{
				$response[]=$row;
			}
		}
	}
	else {
			$response['error'] = true; 
        	$response['message'] = "Require field missing";

}}
echo json_encode($response);