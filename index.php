<?php
############################
require_once("conf.php");
############################
ini_set('session.cookie_lifetime', 10*60);
ini_set('session.gc_maxlifetime', 10*60);
session_start();
?>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Портал -= Электронное правительство =-</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="js/jquery-1.11.2.min.js"></script>
</head>

<body>
<div class="container">

<center>
	<img src="img/logo.png" border="0" style="margin-top:20px;">
</center>

<div class="page-header">
  <h2>Електронні послуги:</h2>
</div>

<?php
$pr = explode('/',$_SERVER['SERVER_PROTOCOL']);
$callbackUrl = selfUrl.'/auth_proxy.php';

$url = activityUrl.'/repository/process-definitions';

$headers = array("GET / HTTP/1.1",
				 "Content-type: text/html;charset=\"utf-8\"",
				 "Accept: *",
				 "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($ch, CURLOPT_TIMEOUT, 90);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
$rez = curl_exec($ch);
$http_status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if(trim($http_status) == "200" && trim($rez) !='') {
	$obj = json_decode($rez);

	if($obj) {
		echo '<ul>';
		foreach($obj->data as $entry) {
			if(isset($_SESSION["access_token"])) {
				echo '<li><h4><a href="form.php?id='.$entry->id.'">'.$entry->name.'</a></h4></li>';
			} else {
				echo '<li><h4><a href="https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri='.urlencode($callbackUrl.'?id='.$entry->id).'">'.$entry->name.'</a></h4></li>';
			}
		}
		echo '</ul>';
	}
} else {
	echo 'Error getting info from '.$url.'; Http-state: '.$http_status;
}
?>

</div> 
</body>
</html>
