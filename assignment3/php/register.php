<?php
 
// response json
$json = array();
 
/**
 * Registering a user device
 * Store reg id in users table
 */
if (isset($_REQUEST["username"]) && isset($_REQUEST["password"]) && isset($_REQUEST["regid"])) {
    $username = $_REQUEST["username"];
    $password = $_REQUEST["password"];
    $gcm_regid = $_REQUEST["regid"]; // GCM Registration ID
    // Store user details in db
    include_once './db_functions.php';
    include_once './GCM.php';
 
    $db = new DB_Functions();
    $gcm = new GCM();
 
    $res = $db->storeUser($username, $password, $gcm_regid);

	if(!$res){
		echo "ERROR";
		exit();
	}

	$row = mysql_fetch_assoc($res);
	//session_regenerate_id();
	$_SESSION['USERNAME'] = $username;
	$_SESSION['GCM_REGID'] = $row['gcm_regid'];
 
    $registatoin_ids = array($gcm_regid);
    $message = array("data" => "STUFF");
 
    $result = $gcm->send_notification($registatoin_ids, $message);
 
    echo $result;
} else {
	echo "ERROR";
}
?>
