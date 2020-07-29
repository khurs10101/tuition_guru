<?php
    
    echo($_SERVER["REQUEST_METHOD"]." request");
    echo("\r\n");
    
    foreach($_REQUEST as $k => $v){
        echo "$k = $v\r\n";
    }


?>
    