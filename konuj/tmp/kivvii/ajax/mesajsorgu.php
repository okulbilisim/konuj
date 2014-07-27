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

if (!$id) { die('Hata: No:1 (mesajsorgu)'); }
if (!$aktifkonusma) { die(); } // Hata no 2

$aktifkonusma = $db->safesql($aktifkonusma);
$id = $db->safesql($id);
$query  = "SELECT id, mesaj FROM konusmatext WHERE konusmaid = $aktifkonusma AND alici = $id AND iletim = 0";
$db->query($query, $debug);
$row = $db->get_row();

$mesajid = $row['id'];
$mesaj = stripslashes(htmlspecialchars($row['mesaj']));

echo "$mesaj";

$query2 = "UPDATE konusmatext SET iletim = '1' WHERE id = '$mesajid' ";
$db->query($query2, $debug);

$db->close();
?>