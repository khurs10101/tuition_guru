<?php

    require("db_connection.php");

    $query="SELECT vversion FROM version_table";
    $result= mysqli_query($con,$query);
    $row=mysqli_fetch_assoc($result);
    echo $row['vversion'];


?>