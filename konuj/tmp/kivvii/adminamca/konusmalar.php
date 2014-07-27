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

echo "<p><a href='fixle.php'>Aktif olmayıpta aktif görünen konuşmaları fixle</a></p>";

$sid = sanitize_int(@$_GET['id']);
if (!$sid) {
	$query  = "SELECT * FROM `konusmalar` WHERE `baslatan` != 0 AND yabanci != 0 ORDER BY `konusmalar`.`tarih` DESC";
}
else{
	$sid = $db->safesql($sid);
	echo "<a href='konusmalar.php'>Tümünü Göster</a> | <a href='banla.php?id=$sid'>Bu Kişiyi Banla</a><br/>";
	$query  = "SELECT * FROM `konusmalar` WHERE `baslatan` != 0 AND yabanci != 0 AND (`baslatan` = $sid OR `yabanci` = $sid) ORDER BY `konusmalar`.`tarih` DESC";
}

$db->query($query, $debug);

$count = $db->num_rows();
$suan = date('d.m.Y H:i',time());
echo "<p>Toplam <b>$count</b> sonuç bulundu. (Şuan tarih: $suan)</p>";

?>
<table id='konusmalist'>
<tr class='header'>
<td class='td1'>id</td>
<td class='td2'>aktif ?</td>
<td class='td3'>basl.</td>
<td class='td4'>yab.</td>
<td class='td5'>tarih</td>
<td class='td6'>sonping1</td>
<td class='td7'>sonping2</td>
<td class='td8'>konuşma</td>
</tr>
<?

$color = 2;
while($row = $db->get_row()){
	if ($color == 1) { $color = 2; }else{ $color = 1; }
	$id = $row['id'];
	$aktif = $row['aktif'];
	if ($aktif == 0) { $aktif = "hayır"; }else{ $aktif = "evet"; }
	$baslatan = $row['baslatan'];
	$yabanci = $row['yabanci'];
	$tarih =    date('d.m.Y H:i',$row['tarih']);
	$sonping1 = date('H:i:s',$row['sonping1']);
	$sonping2 = date('H:i:s',$row['sonping2']);
	
	echo "<tr class='stil$color'>
	<td>$id</td>
	<td>$aktif</td>
	<td><a href='konusmalar.php?id=$baslatan'>$baslatan</a></td>
	<td><a href='konusmalar.php?id=$yabanci'>$yabanci</a></td>
	<td>$tarih</td>
	<td>$sonping1</td>
	<td>$sonping2</td>
	<td><a href='konusma.php?id=$id'>göster</a></td>
	</tr>
	";
}

$db->close();

?>
</table>
</body></html>