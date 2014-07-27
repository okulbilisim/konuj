<?
ob_start(); 
session_start();

require_once("../includes/config.php");
require_once("../includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("../includes/sanitize.php");
require_once("../includes/functions.php");

$id = sanitize_int(@$_COOKIE['kisi_id']); //offline yapilacak kisi
if (!$id) { die('Hata: No:1 (offline)'); }

$aktifkonusma = sanitize_int(@$_SESSION['aktkon']);

if ($aktifkonusma != 0) {
	$query2 = "UPDATE konusmalar SET aktif = '0' WHERE id = '$aktifkonusma' ";
	$db->query($query2, $debug);
}

$query3 = "UPDATE kisiler SET aktifkonusma = '0', online = '0' WHERE id = '$id' ";
$db->query($query3, $debug);

echo "ok";

$db->close();
?>