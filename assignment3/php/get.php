<?php
$latitude = $_REQUEST["latitude"];
$longitude = $_REQUEST["longitude"];
$DIST_CONSTANT = 200;

$con = mysql_connect("localhost","team1","team1");
if(!$con)
 {
die('Could not connect: ' . mysql_error());
 }

$db_selected = mysql_select_db("mobile",$con);

if(!$db_selected)
{
die('Can\'t use demo: ' . mysql_error());
}

$i = 0;

$idarray = array();
$latarray = array();
$longarray = array();
$count = 0;

$sql = "SELECT * FROM gcm_users order by created_at desc";
$result = mysql_query($sql, $con);
if(!$result)
{
die('Result not parsed');
}
$num = mysql_numrows($result);
$j = 0;
while($j < $num)
{
$fielduser=mysql_result($result,$j,"username");
$fielddate=mysql_result($result,$j,"created_at");
$fieldlatitude = mysql_result($result, $j, "lat");
$fieldlongitude = mysql_result($result, $j, "long");
$phpdate = strtotime($fielddate);
$dist = distance($fieldlatitude, $fieldlongitude, $latitude, $longitude);


if($dist < $DIST_CONSTANT){
$idarray[] = $fielduser;
$latarray[] = $fieldlatitude;
$longarray[] = $fieldlongitude;
}
$count ++;
$j++;
}


$array2 = array(
    id    => $idarray,
    long  => $longarray,
    lat => $latarray,
);

print json_encode($array2);
return json_encode($array2);

mysql_close($con);

function distance($lat1, $lon1, $lat2, $lon2) {
  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;
  return ($miles * 1.609344) * 1000;
}
?>





