<?php
	
		$account=@$_POST["account"];
		$password=@$_POST["password"];
		if ($account != "admin" || $password != "123456"){ 
			echo "帳號或密碼錯誤，三秒後自動回上一頁";
			header("refresh:3;url=login.html");    
			exit;
		}else{
			if(isset($_POST['login']))
				header("refresh:0;url=main.php");  // 進入查看作業或是出作業
			else if(isset($_POST['homework']))
				header("refresh:0;url=homework.php"); 
		}
	
?>