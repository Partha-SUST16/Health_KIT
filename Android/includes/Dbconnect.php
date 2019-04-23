<?php
	/**
	 * 
	 */
	class Dbconnect 
	{
		private $con; 
 
        function __construct(){
 
        }
 
        function connect(){
            include_once dirname(__FILE__).'/constants.php';
            $this->con = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);
 			$this->con->set_charset("utf8");
            if(mysqli_connect_errno()){
                echo "Failed to connect with database".mysqli_connect_err(); 
            }
 
            return $this->con; 
        }
	}
