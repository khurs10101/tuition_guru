<?php

    require("db_connection.php");
    $ad_fetch_query="SELECT * FROM ad_table";

    if($result=mysqli_query($con,$ad_fetch_query)){

        $teacher= array();

        while($row=mysqli_fetch_assoc($result)){
            $teacher[]= $row;
        }

        //spit out list as json
        echo json_encode($teacher);

    }else{
       
    }

    mysqli_close($con);

?>