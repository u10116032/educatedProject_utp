<?php session_start();?>
<div class="container">
<p align="center">更改作業項目</p>
<form  method="POST" action="homework.php">
<p align="center"><input type="submit" name="homework"  value="出作業"></p>
</form>
</div>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<div class="container">
<?php
	 echo ("<p align=".center.">請選擇學生帳號</p>");
	 echo("<form  method=".POST." action=".">");
	 	echo("<p align=".center.">");
	 		echo ("<select class=".selectpicker." name=studentId>");
	 		echo ("<Option Value=".u10116032.">u10116032</Option>");
	 		echo ("<Option Value=".u10116033.">u10116033</Option>");
	 		echo ("<Option Value=".u10116034.">u10116034</Option>");
	 		echo ("<Option Value=".u10116035.">u10116035</Option>");
	 		echo ("<Option Value=".u10116036.">u10116036</Option>");

	 		echo ("</elect>");
	 		echo ("<input type=".submit." name=".STUDENTID."  value=".確定.">");
	 	echo("</p>");
	 

   
	$_SESSION['studentId'] = $_POST['studentId'];
  $student = $_SESSION['studentId'];
	 

?>
</div>

<?php
  require 'connection.php';
  global $connect;
?>



<meta http-equiv="content-type" content="text/html; charset=utf-8">
<html lang="en">
<head>
  <title>檢閱作業</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
  <h2><p><?php echo "$student"."作業繳交查詢" ?></p></h2>
  <p><?php
      $sql="SELECT * FROM $student ORDER BY category";
      $result=mysqli_query($connect,$sql);
      if(mysqli_num_rows($result)==0)
      {echo ("<h2 style=".color.":".red.";".">學生尚未繳交作業</h2>");}
  ?></p>
  <table class="table table-striped">
    <thead>
      <tr>
        <th>種類</th>
        <th>標題</th>
        <th>日期</th>
        <th>內容</th>
        <th>照片</th>
        <th>位置</th>
        <th>錄音</th>
        <th>分數</th>
        <th>評語</th>

      </tr>
    </thead>

    <tbody>
     <p><?php
        
        while($row = mysqli_fetch_array($result)){
          echo ("<tr>");
          echo("<td>".$row['category']."</td>");
          echo("<td>".$row['title']."</td>");
          echo("<td>".$row['date']."</td>");
          echo("<td>".$row['content']."</td>");
          echo("<td>"); // photo!!!
          if($row['photo_dir'] != null){echo("<img src=".$row['photo_dir']." style="."width:150px;height:100px ; "."/>");}
          echo("</td>");    
          echo("<td>".$row['address']."</td>");
          echo("<td>"); // rec!!!
          if($row['rec_dir'] != null){
            echo("<audio controls preload=auto>");
            echo("<source src=".$row['rec_dir']." type=audio/mpeg />");
            echo("</audio>");
          }
            
          echo("</td>");
           echo("<td>".$row['score']."</td>");
          echo("<td>".$row['comment']."</td>");
     
          echo("</tr>");
        }
    ?></p>
      
    </tbody>
  </table>
</div>



 
  

    <p align="center">分數: <input type="text" name="score"><br/></p>
    <p align="center">老師評語: <input type="text" name="comment"><br/></p>
    <p align="center">評分日期: <input type="text" name="comment_date"><br/></p>

    <p align="center"><input type="submit" name="COMMAND"  value="送出"></p>

  </form>

  <p><?php 
    if(isset($_POST['COMMAND']) && $_POST['COMMAND'] == '送出'){
      ?></p>
      
      <script>
        alert("成績已送出!");
      </script>
      <p><?php

      //插入資料庫!!!

      $score = $_POST["score"];
      $comment = $_POST["comment"];
      $comment_date = $_POST["comment_date"];
      $sql="SELECT * FROM $student";
    $result=mysqli_query($connect,$sql);
    if( mysqli_num_rows($result)==0 ) {

      $tablequery = "CREATE TABLE $student (_id integer PRIMARY KEY AUTO_INCREMENT, date text  , title text  , content text, 
                longitude real, latitude real, photo_dir text, rec_dir text, category text, address text, score int, comment text); ";
      mysqli_query($connect,$tablequery) ;
    }
    
       
          $query = "Insert into $student(title,date,score,comment) values ('老師評語','$comment_date','$score','$comment');";   
          mysqli_query($connect,$query) ;
      
    }
      
  ?></p>


</body>
</html>




