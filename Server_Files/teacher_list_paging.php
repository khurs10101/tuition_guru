<?php

    require("db_connection.php");

    //this variable will be use to make filter out sql result
    $search_location= trim($_POST['search_location']);
    $search_subject= trim($_POST['search_subject']);
    $search_field= trim($_POST['search_field']);
    $q_loc= '%'.$search_location.'%';
    $q_sub= '%'.$search_subject.'%';
    $q_field='%'.$search_field.'%';

    //setting records limit per page is 21
    $rec_limit = 20;
    // $rec_limit = 2;
    

    /*get total number of records securely*/
    $statement= mysqli_prepare($con,"SELECT count(*) FROM tution_teacher WHERE tfield LIKE ? AND tspecific_subject LIKE ? AND t_city LIKE ?");
    mysqli_stmt_bind_param($statement,"sss",$q_field,$q_sub,$q_loc);
    mysqli_stmt_execute($statement);
    $retval= mysqli_stmt_get_result($statement);
    $rec_count = mysqli_fetch_row($retval);
    $rec_count= $rec_count[0];

    
    /* Get total number of records */
    // $sql = "SELECT count(*) FROM tution_teacher WHERE tfield LIKE '$q_sub' OR tspecific_subject LIKE '$q_sub' AND t_city LIKE '$q_loc'";
    // $retval = $con->query($sql);
    // if(! $retval )
    // {
    // die($mysqli->error.__LINE__);
    // }
    
    // $rec_count = $retval->fetch_row();
    // $rec_count = $rec_count[0];
    
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
    // $sql = "SELECT * FROM tution_teacher ORDER BY tid ".
    //     "LIMIT $offset, $rec_limit";
        
    // $retval = $con->query($sql);
    // if(! $retval )
    // {
       
    // }


    $statement= mysqli_prepare($con,"SELECT * FROM tution_teacher WHERE tfield LIKE ? AND tspecific_subject LIKE ? AND t_city LIKE ? ORDER BY tcoins DESC LIMIT $offset, $rec_limit");
    mysqli_stmt_bind_param($statement,"sss",$q_field,$q_sub,$q_loc);
    mysqli_stmt_execute($statement);
    $retval= mysqli_stmt_get_result($statement);
    
    $total_page_count= $rec_count/$rec_limit;
    //creating an array for response
    $response = array();
    if(mysqli_num_rows($retval)>0){ 
        $response["records"] = array();
        $response["success"] = 1;
        $response["count"]= (int)$total_page_count;

        while($row= mysqli_fetch_array($retval)){
            // temp wallpaper array
            $wallpaper = array();
            $wallpaper["id"]= $row["tid"];
            $wallpaper["name"]= $row["tname"];
            $wallpaper["email"]=$row["temail"];
            $wallpaper['age']=$row['tage'];
            $wallpaper["mobile"]=$row["tphone"];
            $wallpaper["gender"]=$row["tgender"];
            $wallpaper["qualification"]=$row["tqualification"];
            $wallpaper["field"]=$row["tfield"];
            $wallpaper["subject"]=$row["tspecific_subject"];
            $wallpaper["email"]=$row["temail"];
            $wallpaper["fee"]=$row["tfee"];
            $wallpaper["city"]=$row["t_city"];
            $wallpaper["state"]=$row["t_state"];
    
            
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


    
    // if ($retval->num_rows > 0) {
    // $response["records"] = array();
    // $response["success"] = 1;
    // $response["count"]= $rec_count;
    // while ($row = $retval->fetch_array()) {
    //         // temp wallpaper array
    //         $wallpaper = array();
    //         $wallpaper["id"]= $row["tid"];
    //         $wallpaper["name"]= $row["tname"];
    //         $wallpaper["email"]=$row["temail"];
    //         $wallpaper["mobile"]=$row["tphone"];
    //         $wallpaper["gender"]=$row["tgender"];
    //         $wallpaper["qualification"]=$row["tqualification"];
    //         $wallpaper["subject"]=$row["tspecific_subject"];
    //         $wallpaper["email"]=$row["temail"];
    //         $wallpaper["fee"]=$row["tfee"];
    //         $wallpaper["city"]=$row["t_city"];
    //         $wallpaper["state"]=$row["t_state"];
    
            
    //         // push all data into final response array
    //         array_push($response["records"], $wallpaper);
    //     }
    
    //     // echoing JSON response
    // echo str_replace('\/','/',json_encode($response,JSON_PRETTY_PRINT));
    
    // } else {
    //     // no wallpapers found
    //     $response["success"] = 0;
    //     $response["message"] = "No Wallpapers found";
    // }
    
    mysqli_close($con);
?>












































































<!-- <?php

    // require("db_connection.php");

    // //this variable will be use to make filter out sql result
    // $search_location= $_POST['search_location'];
    // $search_subject= $_POST['search_subject'];
    // $q_loc= '%'.$search_location.'%';
    // $q_sub= '%'.$search_subject.'%';

    // //setting records limit per page is 21
    // $rec_limit = 20;
    // // $rec_limit = 2;
    

    // /*get total number of records securely*/
    // $statement= mysqli_prepare($con,"SELECT count(*) FROM tution_teacher WHERE tfield LIKE ? OR tspecific_subject LIKE ? AND t_city LIKE ?");
    // mysqli_stmt_bind_param($statement,"sss",$q_sub,$q_sub,$q_loc);
    // mysqli_stmt_execute($statement);
    // $retval= mysqli_stmt_get_result($statement);
    // $rec_count = mysqli_fetch_row($retval);
    // $rec_count= $rec_count[0];

    
    // /* Get total number of records */
    // // $sql = "SELECT count(*) FROM tution_teacher WHERE tfield LIKE '$q_sub' OR tspecific_subject LIKE '$q_sub' AND t_city LIKE '$q_loc'";
    // // $retval = $con->query($sql);
    // // if(! $retval )
    // // {
    // // die($mysqli->error.__LINE__);
    // // }
    
    // // $rec_count = $retval->fetch_row();
    // // $rec_count = $rec_count[0];
    
    // // Checking for page parameter to set.
    // if( isset($_GET{'page'} ) )
    // {
    // $page = $_GET{'page'};
    // $offset = $rec_limit * $page ;
    // }
    // else
    // {
    // $page = 0;
    // $offset = 0;
    // }
    
    // //getting all data from table
    // // $sql = "SELECT * FROM tution_teacher ORDER BY tid ".
    // //     "LIMIT $offset, $rec_limit";
        
    // // $retval = $con->query($sql);
    // // if(! $retval )
    // // {
       
    // // }


    // $statement= mysqli_prepare($con,"SELECT * FROM tution_teacher WHERE tfield LIKE ? OR tspecific_subject LIKE ? AND t_city LIKE ? ORDER BY tcoins DESC LIMIT $offset, $rec_limit");
    // mysqli_stmt_bind_param($statement,"sss",$q_sub,$q_sub,$q_loc);
    // mysqli_stmt_execute($statement);
    // $retval= mysqli_stmt_get_result($statement);
    
    // $total_page_count= $rec_count/$rec_limit;
    // //creating an array for response
    // $response = array();
    // if(mysqli_num_rows($retval)>0){ 
    //     $response["records"] = array();
    //     $response["success"] = 1;
    //     $response["count"]= (int)$total_page_count;

    //     while($row= mysqli_fetch_array($retval)){
    //         // temp wallpaper array
    //         $wallpaper = array();
    //         $wallpaper["id"]= $row["tid"];
    //         $wallpaper["name"]= $row["tname"];
    //         $wallpaper["email"]=$row["temail"];
    //         $wallpaper['age']=$row['tage'];
    //         $wallpaper["mobile"]=$row["tphone"];
    //         $wallpaper["gender"]=$row["tgender"];
    //         $wallpaper["qualification"]=$row["tqualification"];
    //         $wallpaper["field"]=$row["tfield"];
    //         $wallpaper["subject"]=$row["tspecific_subject"];
    //         $wallpaper["email"]=$row["temail"];
    //         $wallpaper["fee"]=$row["tfee"];
    //         $wallpaper["city"]=$row["t_city"];
    //         $wallpaper["state"]=$row["t_state"];
    
            
    //         // push all data into final response array
    //         array_push($response["records"], $wallpaper);
    //     }

    //      // echoing JSON response
    //     echo str_replace('\/','/',json_encode($response,JSON_PRETTY_PRINT));

    // } else {
    //     // no wallpapers found
    //     $response["success"] = 0;
    //     $response["message"] = "No Wallpapers found";
    // }


    
    // // if ($retval->num_rows > 0) {
    // // $response["records"] = array();
    // // $response["success"] = 1;
    // // $response["count"]= $rec_count;
    // // while ($row = $retval->fetch_array()) {
    // //         // temp wallpaper array
    // //         $wallpaper = array();
    // //         $wallpaper["id"]= $row["tid"];
    // //         $wallpaper["name"]= $row["tname"];
    // //         $wallpaper["email"]=$row["temail"];
    // //         $wallpaper["mobile"]=$row["tphone"];
    // //         $wallpaper["gender"]=$row["tgender"];
    // //         $wallpaper["qualification"]=$row["tqualification"];
    // //         $wallpaper["subject"]=$row["tspecific_subject"];
    // //         $wallpaper["email"]=$row["temail"];
    // //         $wallpaper["fee"]=$row["tfee"];
    // //         $wallpaper["city"]=$row["t_city"];
    // //         $wallpaper["state"]=$row["t_state"];
    
            
    // //         // push all data into final response array
    // //         array_push($response["records"], $wallpaper);
    // //     }
    
    // //     // echoing JSON response
    // // echo str_replace('\/','/',json_encode($response,JSON_PRETTY_PRINT));
    
    // // } else {
    // //     // no wallpapers found
    // //     $response["success"] = 0;
    // //     $response["message"] = "No Wallpapers found";
    // // }
    
    // mysqli_close($con);
?> -->