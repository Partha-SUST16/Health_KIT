<?php 
 
require_once '../includes/DbOperation.php';
 
$response = array(); 
if($_SERVER['REQUEST_METHOD']=='POST')
{
	if(isset($_POST['patient_email']))
	{
		$db = new DbOperation();
		$doctor = $db->getRecentDoctor($_POST['patient_email']);
		if(mysqli_num_rows($doctor)>0)
		{
			while($row = mysqli_fetch_assoc($doctor))
			{
				$response[]=$row;
			}
		}
	}else {
			$response['error'] = true; 
        	$response['message'] = "Require field missing";

	}
}
echo json_encode($response);