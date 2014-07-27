<?
include "dbconfig.php";
include "dbconnect.php";

$query  = "SELECT id, sonping, online ".
          "FROM kisiler ".
		  "WHERE online = 1";
$result = mysql_query($query) or die('MySQL Hatasi (checkonline-1)<br>');

$online_count = 0;

while($row = mysql_fetch_array($result)){
	$sonping = $row['sonping'];
	$id = $row['id'];
	$tarih = time();
	
	if (($tarih - $sonping) > 60) {
		$query2 = "UPDATE kisiler SET online = '0' WHERE id = '$id' ";
		$result2 = mysql_query($query2) or die("MySQL Hatasi (checkonline-id:$id)<br>");
	}else{
		$online_count++;
	}
}

include "dbclose.php";	
?>