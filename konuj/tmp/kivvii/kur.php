<?
header("Location: ./");
die();

require_once("includes/config.php");
require_once("includes/mysql.class.php");
$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);
require_once("includes/sanitize.php");
require_once("includes/functions.php");

$query = "CREATE TABLE konusmalar( ".  // aktif konusmalarin bulundugu tablo
		 'id INT NOT NULL AUTO_INCREMENT, '.
         'aktif INT(1) NOT NULL, '.    // 0=kapali , 1=bekleniyor , 2=aktif
		 'baslatan INT NOT NULL, '.    // konusmayi baslatan kisi (kisiler tablosu)
		 'yabanci INT NOT NULL, '.     // konusmaya katilan kisi (kisiler tablosu)
		 'tarih INT(10) NOT NULL, '.   // konusmanin baslatilma tarihi
		 'sonping1 INT(10) NOT NULL, '.// son gelen ping tarihi (baslatan)
		 'sonping2 INT(10) NOT NULL, '.// son gelen ping tarihi (katilan)
		 'typing1 INT(1) NOT NULL, '.  // ileti yaziyor (baslatan)
		 'typing2 INT(1) NOT NULL, '.  // ileti yaziyor (katilan)
         'PRIMARY KEY(id))';
		 
$db->query($query, $debug);

$query = "CREATE TABLE konusmatext( ".// konusma gecmisinin bulundugu tablo
		 'id INT NOT NULL AUTO_INCREMENT, '.
         'konusmaid INT NOT NULL, '.  // konusmalar tablosundaki karsiligi
		 'gonderen INT NOT NULL, '.   // yaziyi gonderen kisi idsi (kisiler tablosu)
		 'alici INT NOT NULL, '.      // yaziyi alan kisi idsi (kisiler tablosu)
		 'iletim INT(1) NOT NULL, '.  // mesaj iletildi mi?
		 'tarih INT(10) NOT NULL, '.  // gonderilme tarihi
		 'mesaj TEXT NOT NULL, '.     // mesaj
         'PRIMARY KEY(id))';
		 
$db->query($query, $debug);

$query = "CREATE TABLE kisiler( ".     // sitedeki kisilerin listesi
		 'id INT NOT NULL AUTO_INCREMENT, '.
		 'IP VARCHAR(15) NOT NULL, '.  // kisinin IP numarasi
		 'online INT(1) NOT NULL, '.   // online olup olmadigi
		 'aktifkonusma INT NOT NULL, '.// aktif olan konusma idsi (konusmalar tablosu)
		 'sonping INT(10) NOT NULL, '. // son gelen ping tarihi
		 'banli INT(1) NOT NULL, '.    // ban durumu (1:banli, 0:degil)
         'PRIMARY KEY(id))';
		 
$db->query($query, $debug);

echo "Kurulum tamamlandi !";

$db->close();
?>