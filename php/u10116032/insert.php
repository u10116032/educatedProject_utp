<?php
	
	if($_SERVER["REQUEST_METHOD"]=="POST"){
		require 'connection.php';
		global $connect;

		$date = $_POST["date"];
		$title = $_POST["title"];
		$content = $_POST["content"];
		$longitude = $_POST["longitude"];
		$latitude = $_POST["latitude"];
		$photo_dir = $_POST["photo_dir"];
		$rec_dir = $_POST["rec_dir"];
		$user = $_POST["user"];
		$category = $_POST["category"];
		$address = $_POST["address"];

		$sql="SELECT * FROM $user";
		$result=mysqli_query($connect,$sql);
		if( ! $result ) {

			$tablequery = "CREATE TABLE $user (_id integer PRIMARY KEY AUTO_INCREMENT, date text  , title text  , content text, 
                longitude real, latitude real, photo_dir text, rec_dir text, category text, address text, score int, comment text); ";
			mysqli_query($connect,$tablequery) ;
		}
		$sql2="SELECT * FROM $user WHERE title = '$title'";
        $result2=mysqli_query($connect,$sql2);
        if(mysqli_num_rows($result2)==0){
        	$query = "Insert into $user(date,title,content,longitude,latitude,photo_dir,rec_dir,category,address) values ('$date','$title','$content','$longitude'
				,'$latitude','$photo_dir','$rec_dir', '$category', '$address');";		
			mysqli_query($connect,$query) or die(mysqli_error($connect));
        }
        else{
        	$sql3="UPDATE $user SET date='$date',content='$content',longitude='$longitude',latitude='$latitude',photo_dir='$photo_dir',rec_dir='$rec_dir',category='$category',address='$address' WHERE title='$title'";
          	mysqli_query($connect,$sql3) or die(mysqli_error($connect));
        }
		
		mysqli_close(connect);
	}
?>