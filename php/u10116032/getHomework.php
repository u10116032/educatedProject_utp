<?php
	require 'connection.php';
  	global $connect;


  	$sql2="SELECT * FROM homework ";
    $result2=mysqli_query($connect,$sql2);
  	$row = mysqli_fetch_array($result2);
  	
    echo $row['animal'].":".$row['plant'].":".$row['indicate'];
?>