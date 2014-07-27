<?
require_once("includes/config.php");
require_once("includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("includes/sanitize.php");
require_once("includes/functions.php");


$id = sanitize_int($_COOKIE['kisi_id']); //kisi idsi.
$sid = sanitize_int($_GET['id']); // konusma id
if (!$sid){
	header("Location: ./");
	die();
}

$sid = $db->safesql($sid);
$query  = "SELECT baslatan, yabanci FROM konusmalar WHERE id = $sid";
$query_id = $db->query($query, $debug);
$row = $db->get_row();
$baslatan = $row['baslatan'];
$yabanci = $row['yabanci'];
if ($baslatan != $id) {
	if ($yabanci != $id) {
		header("Location: ./");
		die();
	}
}

$query  = "SELECT gonderen, tarih, mesaj FROM `konusmatext` WHERE `konusmaid` = $sid ORDER BY `konusmatext`.`tarih` ASC";
$query_id = $db->query($query, $debug);

header('Content-Description: File Transfer');
header('Content-Type: text/plain');
header('Content-Disposition: attachment; filename="konusmalar.txt"');

while($row = $db->get_row()){
	$gonderen = $row['gonderen'];
	if ($id == $gonderen) {
		$gonderen = "Sen"; 
	}
	else{ 
		$gonderen = "Yabancı"; 
	};
	
	$tarih = date('H:i:s',$row['tarih']);
	$mesaj = stripslashes($row['mesaj']);
	
	if ($mesaj == "_sescal.knock"){
		$mesaj = "*Ekranı Tıklattınız*";
	}
	
	echo "($tarih) $gonderen: $mesaj".chr(13).chr(10);
}

$db->close();
?>