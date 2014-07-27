<?
require_once("../includes/config.php");
require_once("../includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("../includes/sanitize.php");
require_once("../includes/functions.php");

$id = sanitize_int(@$_COOKIE['kisi_id']);
if ($id) {

	$tarih = time();
	$id = $db->safesql($id);
	$query = "UPDATE kisiler SET online = '1', sonping = '$tarih' WHERE id = '$id' ";
	$db->query($query, $debug);


}
echo "$tarih";
$db->close();
?>