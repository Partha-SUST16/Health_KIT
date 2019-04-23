<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST')
{
	if(isset($_POST['doctor_workplace']))
	{
		$db = new DbDoctorOperation();
		$doctor = $db->getDoctorbyHospital($_POST['doctor_workplace']);
		if(mysqli_num_rows($doctor)>0)
		{
			while($row = mysqli_fetch_assoc($doctor))
			{
				$response[]=$row;
			}
		}
		else {
			$response['error'] = true; 
        	$response['message'] = "No data available";}
	}
	else {
			$response['error'] = true; 
        	$response['message'] = "Require field missing";

}}
echo json_encode($response);