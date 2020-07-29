<?php

    require("db_connection.php");

    $email= $_POST['email'];

    // $query="SELECT * FROM tution_teacher WHERE temail='$email'";

    // $result= mysqli_query($con,$query);
    // $teacher= array();
    // while($row=mysqli_fetch_assoc($result)){
    //     $teacher[]= $row;
    // }

    //query the database for the existence of email
    $query_to_execute= mysqli_prepare($con,"SELECT susername, spassword FROM tution_student WHERE semail=?");
    mysqli_stmt_bind_param($query_to_execute,"s",$email);
    mysqli_stmt_execute($query_to_execute);

    mysqli_stmt_store_result($query_to_execute);
    mysqli_stmt_bind_result($query_to_execute,$rusername,$rpassword);

    if(mysqli_stmt_num_rows($query_to_execute)==1){
        $user= array();

        while(mysqli_stmt_fetch($query_to_execute)){
            $user['username']= $rusername;
            $user['password']= $rpassword;
        }

        echo "a";

        //email goes here

            $to = $email;
            $subject = "TutionFinder user credentials. Don not share";

            $message = "
            <html>
            <head>
            <title>User Credentials</title>
            </head>
            <body>
            <p>This email contains sensitive information. Donot share this mail with anyone</p>
            <table>
            <tr>
            <th>username</th>
            <th>password</th>
            </tr>
            <tr>
            <td>".$user['username']."</td>
            <td>".$user['password']."</td>
            </tr>
            </table>
            </body>
            </html>
            ";

            // Always set content-type when sending HTML email
            $headers = "MIME-Version: 1.0" . "\r\n";
            $headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";

            // More headers
            $headers .= 'From: <donotreply@oxomtech.in>' . "\r\n";
            // $headers .= 'Cc: myboss@example.com' . "\r\n";

            mail($to,$subject,$message,$headers);

            //means mail is sent to the reciept
           

    }else{
        //user doesnt exists. please register
        echo "c";
    }

    
    
    // echo json_encode($user);

    //compose mail and send the password to the email

    
    mysqli_stmt_close($query_to_execute);
    mysqli_close($con);


?>