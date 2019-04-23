<?php 
 
require_once '../includes/DbDoctorOperation.php';
 
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST')
{
	if(isset($_POST['doctor_catagory']))
	{
		$db = new DbDoctorOperation();
		$doctor = $db->getDoctorByCatagory($_POST['doctor_catagory']);
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