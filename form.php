<?php
############################
require_once("conf.php");
############################
session_start();

if(!isset($_SESSION["access_token"])) {
	header("Location: ".selfUrl."/index.php");
}
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

<script type="text/javascript">

$(document).ready(function() {
	$('#btnSubm').click( function() {
		var data = {};
		$("#procForm").serializeArray().map(function(x){data[x.name] = x.value;}); 

		var arr = new Array(data);		
		var i = 0;
		$.each( data, function( key, value ) {
			arr[i] = {"id":key,"value":value};
			i++;
		});

		var postData = {
			"processDefinitionId" : "<?php echo $_REQUEST["id"]; ?>",
			"businessKey" : "<?php echo $_SESSION["access_token"]; ?>",
			"properties" : arr
		}

		console.log(postData);

		$.ajax({
			url: "req_proxy.php",
			dataType: "json",
			cache: false,
			type: "POST",
			timeout: 10000,
			data: JSON.stringify(postData),
			success : function(data) {
				alert('Ваша заявка успешно сохранена');
				location.href = 'index.php';
				//console.log(data);
			},
			statusCode: {
				400: function (jqXHR, textStatus, errorThrown) { 
					alert('Ошибка: '+errorThrown);
					//console.log(errorThrown);
				},
				500: function (jqXHR, textStatus, errorThrown) { 
					alert('Ошибка: '+errorThrown);
					//console.log(errorThrown);
				}
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert('Error: '+errorThrown);				
			}
		});
	});       	      
});

</script> 

</head>
<body>

<div class="container">


<nav class="navbar navbar-default">
  <div class="container-fluid">
	   <a class="navbar-brand" href="index.php">Портал звернень громадян</a>
  </div>
</nav>

<center>
	<img src="img/logo.png" border="0" style="margin-top:10px;">
</center>

<div class="page-header">
  <h2>Будь ласка заповніть форму <?php echo $_REQUEST["id"]; ?></h2>
</div>


<?php
$url = activityUrl.'/form/form-data?processDefinitionId='.$_REQUEST["id"];

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
	//echo $rez;
	$obj = json_decode($rez);

	if($obj) {		
		echo '<form role="form" id="procForm">';
		foreach($obj->formProperties as $entry) {
			echo '<div class="form-group">';
			echo '<label for="email">'.$entry->name.':</label>';

			if($entry->type == 'string' || $entry->type == 'date' || $entry->type == 'long') {
				echo '<input type="text" class="form-control" name="'.$entry->id.'" id="'.$entry->id.'" value="'.$entry->value.'">';
			} else if($entry->type == 'enum') {
				echo '<select name="'.$entry->id.'" class="form-control">';

				foreach($entry->enumValues as $entrySub) {
					$sel='';

					if((string)$entrySub->id == (string)$entry->value) {
						$sel='selected';
					}

					echo '<option value="'.$entrySub->id.'" '.$sel.'>'.$entrySub->name.'</option>';
				}
				
				echo '</select>';
			}
			
			echo '</div>';			
		}
		//echo '<button class="btn btn-lg btn-primary" type="button">Ідентифікация BankID</button>&nbsp;';
		echo '<button class="btn btn-lg btn-primary" id="btnSubm" type="button">Подати заявку</button>';
		echo '</form>';
		
	}
} else {
	echo 'Error getting info from '.$url.'; Http-state: '.$http_status;
}
?>

</div> 
<br>

</body>
</html>
