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

if (!$id) { die('Hata: No:1 (durum)'); }
if (!$aktifkonusma) { die('Hata: No:2 (durum)'); }

$aktifkonusma = $db->safesql($aktifkonusma);
$query  = "SELECT aktif, baslatan, yabanci, sonping1, sonping2, typing1, typing2 FROM konusmalar WHERE id = $aktifkonusma";
$db->query($query, $debug);
$row = $db->get_row();


$aktif = $row['aktif'];
$baslatan = $row['baslatan'];
$yabanci = $row['yabanci'];
$sonping1 = $row['sonping1']; //baslatan ping
$sonping2 = $row['sonping2']; //yabanci ping
$typing1 = $row['typing1']; //baslatan yaziyor mu
$typing2 = $row['typing2']; //yabanci yaziyor mu
$tarih = time();

if ($baslatan != $id) {
	if ($yabanci != $id) {
		die("Hata: No:4 (durum)");
	}
}

if ($id == $baslatan) {
	$query2 = "UPDATE konusmalar SET sonping1 = '$tarih' WHERE id = '$aktifkonusma' ";
	$db->query($query2, $debug);
}
if ($id == $yabanci) {
	$query2 = "UPDATE konusmalar SET sonping2 = '$tarih' WHERE id = '$aktifkonusma' ";
	$db->query($query2, $debug);
}

if ($aktif == 2) {
	if ($id == $baslatan) {
		if (($tarih - $sonping2) > 60) {
			$query2 = "UPDATE konusmalar SET aktif = '0' WHERE id = '$aktifkonusma' ";
			$db->query($query2, $debug);
			
			$query2 = "UPDATE kisiler SET aktifkonusma = '0' WHERE id = '$baslatan' ";
			$db->query($query2, $debug);
			
			@$_SESSION['aktkon'] = "0";
			die("timeout");			
		}
	}
	
	if ($id == $yabanci) {
		if (($tarih - $sonping1) > 60) {
			$query2 = "UPDATE konusmalar SET aktif = '0' WHERE id = '$aktifkonusma' ";
			$db->query($query2, $debug);
			
			$query2 = "UPDATE kisiler SET aktifkonusma = '0' WHERE id = '$yabanci' ";
			$db->query($query2, $debug);
			
			@$_SESSION['aktkon'] = "0";
			die("timeout");			
		}
	}
	
	if ($id == $baslatan) echo "aktif,$typing2";
	if ($id == $yabanci) echo "aktif,$typing1";
}

if ($aktif == 0) {
	echo "inaktif";
	@$_SESSION['aktkon'] = "0";
}

$db->close();
?>