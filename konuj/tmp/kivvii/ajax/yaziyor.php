<?
ob_start(); 
session_start();

require_once("../includes/config.php");
require_once("../includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("../includes/sanitize.php");
require_once("../includes/functions.php");

$id = sanitize_int(@$_COOKIE['kisi_id']); //cevap bekleyen kisi idsi.
$aktifkonusma = sanitize_int(@$_SESSION['aktkon']);

$val = sanitize_int(@$_GET['val']);
if (!$id) { die('Hata: No:1 (yaziyor)'); }
if (!$aktifkonusma) { die('Hata: No:2 (yaziyor)'); }

$aktifkonusma = $db->safesql($aktifkonusma);

$query  = "SELECT baslatan, yabanci FROM konusmalar WHERE id = $aktifkonusma";
$db->query($query, $debug);
$row = $db->get_row();
$baslatan = $row['baslatan'];
$yabanci = $row['yabanci'];
$tarih = time();

if ($baslatan == $id) {
	$query2 = "UPDATE konusmalar SET typing1 = '$val', sonping1 = '$tarih' WHERE id = '$aktifkonusma' ";
	$db->query($query2, $debug);
}

if ($yabanci == $id) {
	$query2 = "UPDATE konusmalar SET typing2 = '$val', sonping2 = '$tarih' WHERE id = '$aktifkonusma' ";
	$db->query($query2, $debug);
}

$db->close();

echo "ok";
?>