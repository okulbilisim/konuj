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

if (!$id) { 
	die('Hata: No:1 (bekleme)'); 
}
if (!$aktifkonusma) { 
	die('Hata: No:2 (bekleme)'); 
}

$aktifkonusma = $db->safesql($aktifkonusma);

$query  = "SELECT aktif, baslatan FROM konusmalar WHERE id = $aktifkonusma";
$query_id = $db->query($query, $debug);
$row = $db->get_row();

$aktif = $row['aktif'];
$baslatan = $row['baslatan'];

if ($baslatan != $id) {
	die("Hata: No:4 (bekleme)");
}

$tarih = time();

$query2 = "UPDATE konusmalar SET sonping1 = '$tarih' WHERE id = '$aktifkonusma' ";
$db->query($query2, $debug);

if ($aktif == 2) { 
	echo "aktif"; 
}
if ($aktif == 1) { echo "bekle"; }
if ($aktif == 0) { echo "kapat"; @$_SESSION['aktkon'] = "0"; }

$db->close();
?>