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
<title>Konuşma Listesi</title>
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

if (!$sid) die("Kişi IDsi yazmadınız.");

$sid = $db->safesql($sid);
$query  = "SELECT *  FROM `konusmatext` WHERE `konusmaid` = $sid ORDER BY `konusmatext`.`tarih` ASC";
$db->query($query, $debug);

$count = $db->num_rows();
echo "<p>Toplam <b>$count</b> metin bulundu.</p>";

?>
<table id='textlist'>
<tr class='header'>
<td class='td1'>Tarih</td>
<td class='td2'>İletim</td>
<td class='td3'>Mesaj</td>
</tr>
<?

$ilkgond = 0;
while($row = $db->get_row()){
	$iletim = $row['iletim'];
	if ($iletim == 0) { $iletim = "hayır"; }else{ $iletim = "evet"; }
	$gonderen = $row['gonderen'];
	if ($ilkgond == 0) { $ilkgond = $gonderen; }
	if ($gonderen == $ilkgond) { $color = 1; }else{ $color = 2; }
	$tarih = date('d.m.Y H:i:s',$row['tarih']);
	$mesaj = "$gonderen: ".stripslashes(htmlspecialchars($row['mesaj']));
	
	echo "<tr class='stil$color'>
	<td>$tarih</td>
	<td>$iletim</td>
	<td>$mesaj</td>
	</tr>
	";
}

$db->close();

?>
</table>
</body></html>