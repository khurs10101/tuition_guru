<?php

    require("db_connection.php");

    $query="SELECT tspecific_subject FROM tution_teacher";
    $result= mysqli_query($con,$query);
    $outp= array();

    while($row=mysqli_fetch_assoc($result)){
        $outp[]=$row;
    }

    
    echo json_encode($outp);

    mysqli_close($con);


?>