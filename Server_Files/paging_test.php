<?php

    require("db_connection.php");

    $current_page= $_POST['current_page'];

    $count="SELECT COUNT(*) FROM tution_teacher_dummy";
    $count= mysqli_query($con,$count);

    //get the count of the rows
    echo mysqli_num_rows($count);
    

?>