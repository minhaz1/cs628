<?php
// taken from: http://www.geodatasource.com/developers/php
function distance($lat1, $lon1, $lat2, $lon2) {

  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;

  return ($miles * 1.609344) * 1000;
}

//	session_start();

//	if(isset($_SESSION['USERNAME']) && $_SESSION['USERNAME'] != ""){
//	    echo "You're already signed in man!";
//	    exit();
//	}

	include_once('dbconnect.php');

	$username = $_REQUEST['username'];
	$password = $_REQUEST['password'];
	
	$qry = "SELECT * FROM gcm_users WHERE username='$username' AND password='$password'";

	$result = mysql_query($qry);
	
	if($result){
	    echo mysql_num_rows($result);	
	
	    if(mysql_num_rows($result) == 1){
	    	$row = mysql_fetch_assoc($result);
	        session_regenerate_id();
			$_SESSION['USERNAME'] = $username;
			$_SESSION['GCM_REGID'] = $row['gcm_regid'];
			echo "OKAY";
	    }
	    else{
	        echo "ERROR";
	    }
	}
	
?>
