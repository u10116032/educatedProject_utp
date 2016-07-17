<meta http-equiv="content-type" content="text/html; charset=utf-8">

<html>
<head>
	<title>homework</title>
</head>

<body>
	<div class="container">
	<p align="center">本周作業項目</p>
	<form  method="POST" action="">
		<p align="center">
			動物數量 : <input type="text" name="animalNumber"><br/>
			植物數量 : <input type="text" name="plantNumber"><br/>
			作業敘述 : <input type="text" name="indicate"><br/>
			<input type="submit" name="homework"  value="上傳作業項目">

		</p>
	</form>

	<P><?php
	require 'connection.php';
  	global $connect;

	if(isset($_POST['homework']) && $_POST['homework']!=null &&  $_POST['animalNumber']!=null && $_POST['plantNumber']!=null){
		$animalNumber = $_POST['animalNumber'];
		$plantNumber = $_POST['plantNumber'];
		$indicate = $_POST['indicate'];

		$sql2="SELECT * FROM homework ";
        $result2=mysqli_query($connect,$sql2);
        if(mysqli_num_rows($result2)==0){
        	$query = "Insert into homework(animal,plant,indicate) values ('$animalNumber','$plantNumber','$indicate');";		
			mysqli_query($connect,$query) or die(mysqli_error($connect));
        }
		else{
			$sql3="UPDATE homework SET animal='$animalNumber',plant='$plantNumber',indicate='$indicate' WHERE id='1'";
         	mysqli_query($connect,$sql3) ;
		}

		echo("<script>");
        echo('alert("作業已更改並送出!");');
        echo("</script>");

		mysqli_close(connect);
	}
		 
	?></p>
</div>
</body>
</html>