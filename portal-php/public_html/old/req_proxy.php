<?php
############################
require_once("conf.php");
############################

$url = activityUrl.'/form/form-data';
$postFields = file_get_contents('php://input');

/*
echo $postFields;
exit;
*/

$headers = array("POST / HTTP/1.1",
				 "Content-type: application/json;charset=\"utf-8\"",
				 "Accept: *",
				 "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0",
				 "Content-Length: ".strlen($postFields));

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
curl_setopt($ch, CURLOPT_TIMEOUT, 90);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $postFields);
$rez = curl_exec($ch);
$http_status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

echo $rez;
?>
