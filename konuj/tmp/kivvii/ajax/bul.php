<?
ob_start(); 
session_start();

require_once("../includes/config.php");
require_once("../includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("../includes/sanitize.php");
require_once("../includes/functions.php");

$id = sanitize_int(@$_COOKIE['kisi_id']); //cevap bekleyen kisi idsi.
if (!$id) { 
	die('Hata: No:1'); 
}

// once kontroller...
$id = $db->safesql($id);
$query6  = "SELECT aktifkonusma, banli FROM kisiler WHERE id = $id";

$query_id = $db->query($query6, $debug);
$rowx = $db->get_row();
$chk_aktifkonusma = $rowx['aktifkonusma'];
$chk_banli = $rowx['banli'];

// kisi banli mi ?
if ($chk_banli == 1) {
	die("banli");
}

// kisinin aktif bir konusmasi var mi ?
if ($chk_aktifkonusma != 0) {
	// bulunan konusma, aktif mi?
	$query7  = "SELECT aktif FROM konusmalar WHERE id = $chk_aktifkonusma";
	$db->query($query7, $debug);
	$rowy = $db->get_row();
	$chk_aktif = $rowy['aktif'];
	
	if ($chk_aktif == 1) {
		// kisi zaten bir aktif konusmada
		@$_SESSION['aktkon'] = $chk_aktifkonusma;
		die("aktif,$chk_aktifkonusma,bekleme");		
	}
	
	if ($chk_aktif == 2) {
		// kisi zaten bir aktif konusmada
		@$_SESSION['aktkon'] = $chk_aktifkonusma;
		die("aktif,$chk_aktifkonusma,aktif");		
	}
}
// kontroller bitti.

// beklemede olan konusma varmi ?
$query  = "SELECT id, baslatan, sonping1 FROM konusmalar WHERE aktif = 1";
$db->query($query, $debug);
$bulundu = 0;

while($row = $db->get_row()){
	// beklemede olan konusma bulundu
	$konusmaid = $row['id'];
	$baslatan = $row['baslatan'];
	$sonping = $row['sonping1'];
	$tarih = time();
	
	if ($baslatan != $id) {
		if (($tarih - $sonping) > 60) {
			// beklemeyi baslatan, ping timeout olmus
			$query2 = "UPDATE konusmalar SET aktif = '0' WHERE id = '$konusmaid' ";
			$db->query($query2, $debug);
		}else{
			// bulundu
			$bulundu = 1;
			$row = "";
		}
	}
}

if ($bulundu == 1) {
	//beklemede olan biri bulundu. gerekli islemleri yap.
	$query3 = "UPDATE kisiler SET aktifkonusma = '$konusmaid' WHERE id = '$id' ";
	$db->query($query3, $debug);
	
	$tarih = time();
	$query8 = "UPDATE konusmalar SET aktif = '2', yabanci = '$id', sonping2 = '$tarih' WHERE id = '$konusmaid' ";
	$db->query($query8, $debug);
	
	echo "bulundu,$konusmaid,$baslatan";
	@$_SESSION['aktkon'] = $konusmaid;
}
else{
	//beklemede olan yok. yeni konusma baslat.
	$tarih = time();
	$query4 = "INSERT INTO konusmalar (aktif, baslatan, yabanci, tarih, sonping1, sonping2, typing1, typing2) VALUES ('1', '$id', '0', '$tarih', '$tarih', '0', '0', '0')";
	$db->query($query4, $debug);
	$yeni_konusma_id = $db->insert_id();
	
	$query5 = "UPDATE kisiler SET aktifkonusma = '$yeni_konusma_id' WHERE id = '$id' ";
	$db->query($query5, $debug);
	
	echo "bekleniyor,$yeni_konusma_id";
	@$_SESSION['aktkon'] = $yeni_konusma_id;
}

$db->close();
?>