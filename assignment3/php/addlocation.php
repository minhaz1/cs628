<?php

error_reporting(E_ALL);

//session_start();

//if(!isset($_SESSION['USERNAME']) || $_SESSION['USERNAME'] == ""){
//    echo "ERROR: You need to be signed in!";
//    exit();
//}

// taken from: http://www.geodatasource.com/developers/php
function distance($lat1, $lon1, $lat2, $lon2) {

  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;

  return ($miles * 1.609344) * 1000;
}
 

// Allowing user to update location
if (isset($_REQUEST["username"]) && isset($_REQUEST["lat"]) && isset($_REQUEST["long"])) {
    $username = $_REQUEST["username"];
    $lat = $_REQUEST["lat"];
    $long = $_REQUEST['long'];

    // Store location info
    include_once('dbconnect.php');
    include_once('GCM.php');        
    $gcm = new GCM(); 
    $registration_ids = array();

	// gett info before adding loc update
	$qry = "SELECT * FROM gcm_users WHERE username='$username'";
	$result = mysql_query($qry);

	$old = mysql_fetch_array($result, MYSQL_ASSOC);

	// update users info in db
    $qry = "UPDATE `gcm_users` SET `lat`=$lat, `long`=$long WHERE `username` ='$username'";

    $result = mysql_query($qry);

	// if succesful
    if($result){
	
		// get list of all users and check relative locations
		$qry = "SELECT * FROM gcm_users";
		$result = mysql_query($qry);
	
		$message = array();

		while ($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
	    	if($row['username'] == $username)
	        	continue;
			
			// check if user is in range of new lat long
	    	if(distance($lat, $long, $row['lat'], $row['long']) <= 200){
				// only send to people that were out of range before
				if(distance($old['lat'], $old['long'], $row['lat'], $row['long']) > 200){
					$registration_ids[] = $row['gcm_regid'];
				}
				
				// don't need an else, if they were already in range then we don't care
			}

			// if they're not in range now then we don't care
		}

		$message = $username . " " . $lat . " " . $long;
		$messages = array("data" => $message);
		$result = $gcm->send_notification($registration_ids, $messages);

	}
} 
else{
	echo "FAIL";
}

?>
