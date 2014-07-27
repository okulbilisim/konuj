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
$mesaj = @$_POST['mesaj'];
if (!$id) { die('Hata: No:1 (mesajgonder)'); }
if (!$aktifkonusma) { die('Hata: No:2 (mesajgonder)'); }
if (!$mesaj) { die("Hata: Bir ileti yazmadınız. $mesaj"); }

$mesaj = VeEkle($mesaj);

$aktifkonusma = $db->safesql($aktifkonusma);
$query  = "SELECT aktif, baslatan, yabanci FROM konusmalar WHERE id = $aktifkonusma";
$db->query($query, $debug);
$row = $db->get_row();
$aktif = $row['aktif'];
$baslatan = $row['baslatan'];
$yabanci = $row['yabanci'];
$tarih = time();

if ($aktif == 0) { die('Hata: Konuşma sona ermiş. Büyük ihtimalle internet bağlantınız kopmuştu.'); }
if ($aktif == 1) { die('Hata: Bekleme durumunda mesaj gonderemezsiniz.'); }

if ($baslatan != $id) {
	if ($yabanci != $id) {
		die("Hata: No:5 (mesajgonder)");
	}
}

$mesaj = $db->safesql($mesaj);

if ($baslatan == $id) {
	$query = "INSERT INTO konusmatext (konusmaid, gonderen, alici, iletim, tarih, mesaj) VALUES ('$aktifkonusma', '$id', '$yabanci', '0', '$tarih', '$mesaj')";
	$db->query($query, $debug);
}

if ($yabanci == $id) {
	$query = "INSERT INTO konusmatext (konusmaid, gonderen, alici, iletim, tarih, mesaj) VALUES ('$aktifkonusma', '$id', '$baslatan', '0', '$tarih', '$mesaj')";
	$db->query($query, $debug);
}

$mesaj = htmlspecialchars(stripslashes($mesaj));

echo "$mesaj";

$db->close();
?>