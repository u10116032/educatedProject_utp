<?php session_start();?>


<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<?php
    echo("<div class =table-responsive>");
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
    echo("</div>");
   

   
  $_SESSION['studentId'] = $_POST['studentId'];
  $student = $_SESSION['studentId'];
   

?>


<?php
  require 'connection.php';
  global $connect;
?>



<meta http-equiv="content-type" content="text/html; charset=utf-8">
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>

<div class="table-responsive">
  <h2><p><?php echo "$student"."作業繳交查詢" ?></p></h2>
  <p><?php
      $sql="SELECT * FROM $student ORDER BY category";
      $result=mysqli_query($connect,$sql);
      if(mysqli_num_rows($result)==0)
      {echo ("<h2 style=".color.":".red.";".">學生尚未繳交作業</h2>");}
  ?></p>
  <table class="table">
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
            echo("Your browser does not support the audio element.");
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
</body>
</html>




