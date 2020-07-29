<?php

    require("db_connection.php");

    //setting records limit per page is 21
    $rec_limit = 21;
    
    
    /* Get total number of records */
    $sql = "SELECT count(*) FROM authors";
    $retval = $con->query($sql);
    if(! $retval )
    {
    die($mysqli->error.__LINE__);
    }
    
    $rec_count = $retval->fetch_row();
    $rec_count = $rec_count[0];
    
    // Checking for page parameter to set.
    if( isset($_GET{'page'} ) )
    {
    $page = $_GET{'page'};
    $offset = $rec_limit * $page ;
    }
    else
    {
    $page = 0;
    $offset = 0;
    }
    
    //getting all data from table
    $sql = "SELECT * FROM authors ORDER BY id ".
        "LIMIT $offset, $rec_limit";
        
    $retval = $con->query($sql);
    if(! $retval )
    {
       
    }
    
    //creating an array for response
    $response = array();
    
    if ($retval->num_rows > 0) {
    $response["records"] = array();
    $response["success"] = 1;
    $response["count"]= $rec_count;
    while ($row = $retval->fetch_array()) {
            // temp wallpaper array
            $wallpaper = array();
            $wallpaper["id"]= $row["id"];
            $wallpaper["first"]= $row["first_name"];
            $wallpaper["last"]=$row["last_name"];
    
            
            // push all data into final response array
            array_push($response["records"], $wallpaper);
        }
    
        // echoing JSON response
    echo str_replace('\/','/',json_encode($response,JSON_PRETTY_PRINT));
    
    } else {
        // no wallpapers found
        $response["success"] = 0;
        $response["message"] = "No Wallpapers found";
    }
    
    mysqli_close($con);
?>