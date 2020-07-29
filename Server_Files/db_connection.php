<?php

	$hostname="localhost";
    $db_name="oxomtech_all_user_db";
    $username="oxomtech_admin";
    $password="OxomTech07012018";
    //ends
	//$username="root";
    //$password="";

    //connection to database
    $con= mysqli_connect($hostname,$username,$password,$db_name);
	
	/*if($con)
		echo "Connection sucessfull";*/
    
    if(mysqli_connect_errno())
    {
        echo "Error occured ".mysqli_connect_errno();
    }
    
    
?>