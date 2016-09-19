<?php
require_once 'DB_Functions.php';
$db = new DB_Functions();
try{
    // json response array
    $response = array("error" => FALSE);

    $uid = $_POST['uid'];
    $title = $_POST['title'];
    $description = $_POST['description'];
    $testfile = fopen("testfile.txt", 'w');
      fwrite($testfile, $uid);
      fwrite($testfile, $title);
      fwrite($testfile, $description);

     
   if (isset($_POST['uid']) && isset($_POST['title']) && isset($_POST['description']) ) {
     
        // receiving the post params
        $uid = $_POST['uid'];
        $title = $_POST['title'];
        $description = $_POST['description'];
     
        // edit notice
         $user = $db->editnotice($uid, $title, $description);
     
        if ($user != false) {
            // use is found
            $response["error"] = FALSE;
                $response["uid"] = $user["uniqueid"];
                $response["user"]["title"] = $user["title"];
                $response["user"]["description"] = $user["description"];
            echo json_encode($response);
        } else {
            // user is not found with the credentials
            $response["error"] = TRUE;
            $response["error_msg"] = "unknown error occurred";
            echo json_encode($response);
        }
    } else {
        // required post params is missing
        $response["error"] = TRUE;
        $response["error_msg"] = "title or description is absent";
        echo json_encode($response);
    }
}catch(Exception $e){
    $response["error"] = TRUE;
    $response["error_msg"] = var_dump($e->getMessage());
    echo json_encode($response);
}
?>