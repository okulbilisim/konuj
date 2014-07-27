<? 
ob_start(); 
session_start(); 
if(@isset($_SESSION['hatali_giris_sayisi']) && (@$_SESSION['hatali_giris_sayisi'] == 3)){
	die();
}
@require_once("../includes/config.php");
?>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="admin.css" media="all"/>
<title>Aktif Konuşmaları Fixle</title>
</head>
<body>
<?

if (@$_COOKIE['adminpass'] != $adminpass) {
    header('Location: index.php');
    die("Hatalı şifre.");
}

@require_once("../includes/mysql.class.php");
$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);
@require_once("../includes/sanitize.php");
@require_once("../includes/functions.php");

$query  = "SELECT id, sonping1, sonping2 ".
          "FROM konusmalar ".
		  "WHERE aktif != 0";
$db->query($query, $debug);

$count = 0;

while($row = $db->get_row()){
	$sonping1 = $row['sonping1'];
	$sonping2 = $row['sonping2'];
	$id = $row['id'];
	$tarih = time();
	
	if (($tarih - $sonping1) > 300) {
		if (($tarih - $sonping2) > 300) {
			// 5 dkdir ping alinmiyor
			$query2 = "UPDATE konusmalar SET aktif = '0' WHERE id = '$id' ";
			$db->query($query, $debug);
			$count++;
		}
	}
}

echo "$count konuşma düzeltildi.";

$db->close();

?>