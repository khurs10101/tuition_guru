<?php

    require("db_connection.php");
    $query= $_POST['search_query'];
    $new_query= '%'.$query.'%';

    // $statement= mysqli_prepare($con,"SELECT * FROM tution_teacher WHERE tname LIKE ? OR tsubject LIKE ? OR tspecific_subject LIKE ? OR tclass LIKE ?");
    // mysqli_stmt_bind_param($statement,"ssss",$new_query,$new_query,$new_query,$new_query);
    // mysqli_stmt_execute($statement);
    // $result= mysqli_stmt_get_result($statement);

    $statement= mysqli_prepare($con,"SELECT * FROM tution_teacher WHERE tname LIKE ? OR tsubject LIKE ? OR tspecific_subject LIKE ? OR tclass LIKE ?");
    mysqli_stmt_bind_param($statement,"ssss",$new_query,$new_query,$new_query,$new_query);
    mysqli_stmt_execute($statement);
    $result= mysqli_stmt_get_result($statement);

    // $result= mysqli_query($con,"SELECT * FROM tution_teacher");

    

    if(mysqli_num_rows($result)>0){

        $teacher=array();
        while($row=mysqli_fetch_assoc($result)){
            $teacher[]= $row;
        }

        echo json_encode($teacher);

    }else{
        //no result found. 
        echo "c";
    }


?>