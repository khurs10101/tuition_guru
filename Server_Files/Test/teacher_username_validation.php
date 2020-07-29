<?php

	
	//making database connection
	require("db_connection.php");

	
	//obtaining values
	$username= $_POST['username'];

	$query="SELECT * FROM tution_teacher WHERE tusername='$username'";
	
	if($result=mysqli_query($con,$query)){
		//returns the number of rows
		$row_num= mysqli_num_rows($result);
		if($row_num==0){
			
			echo "0";
			
		}else{
			
			echo "1";
		}
	}


	mysqli_close($con);

?>