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
<title>Kişiyi Banla</title>
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

$sid = sanitize_int(@$_GET['id']);

if (!$sid) {
	header('Location: konusmalar.php');
}
else{
	$sid = $db->safesql($sid);
	$query  = "SELECT banli FROM `kisiler` WHERE `id` = $sid";
}
$db->query($query, $debug);
$row = $db->get_row();
$banli = $row['banli'];

if ($banli == 1) { 
	$banli = 0; 
}
else{ 
	$banli = 1; 
}

$query = "UPDATE kisiler SET banli = '$banli' WHERE id = '$sid' ";
$db->query($query, $debug);

if ($banli == 1) {
	echo "Kişi banlandı.";
}else{
	echo "Kişinin banı açıldı.";
}

$db->close();

?>