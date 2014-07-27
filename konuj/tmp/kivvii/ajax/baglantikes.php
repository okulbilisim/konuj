<?
ob_start(); 
session_start();

require_once("../includes/config.php");
require_once("../includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("../includes/sanitize.php");
require_once("../includes/functions.php");

$id = sanitize_int(@$_COOKIE['kisi_id']);
if (!$id) { 
	die('Hata: No:1 (baglantikes)'); 
}

$aktifkonusma = sanitize_int(@$_SESSION['aktkon']);

if ($aktifkonusma != 0) {
	$id = $db->safesql($id);
	$query3 = "UPDATE kisiler SET aktifkonusma = '0' WHERE id = '$id' ";
	$db->query($query3, $debug);

	$aktifkonusma = $db->safesql($aktifkonusma);
	$query2 = "UPDATE konusmalar SET aktif = '0' WHERE id = '$aktifkonusma' ";
	$db->query($query2, $debug);
}

echo "ok";

$db->close();
?>