<?php
############################
require_once("conf.php");

ini_set('session.cookie_lifetime', 10*60);
ini_set('session.gc_maxlifetime', 10*60);
session_start();
############################

if(isset($_REQUEST["code"])) {
	$code = $_REQUEST["code"];
	$id = $_REQUEST["id"];
	$callbackUrl = urlencode(selfUrl.'/auth_proxy.php?id='.$id);

	$url = 'https://bankid.privatbank.ua/DataAccessService/oauth/token?grant_type=authorization_code&client_id=dniprorada&client_secret=NzVmYTI5NGJjMDg3OThlYjljNDY5YjYxYjJiMjJhNA==&code='.$code.'&redirect_uri='.$callbackUrl;

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
			if(isset($obj->error)) {
				echo 'Error in auth: '.$rez.'; Http-state: '.$http_status;
			} else {
				$_SESSION["access_token"] = (string)$obj->access_token;
				header("Location: ".selfUrl."/form.php?id=".$id);
			}
		}
	} else {
		echo 'Error in auth: '.$rez.'; Http-state: '.$http_status;
	}
}

?>
