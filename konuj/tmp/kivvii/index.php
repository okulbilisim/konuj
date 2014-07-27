<?
session_start(); 
require_once("includes/config.php");
require_once("includes/mysql.class.php");
$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);
require_once("includes/sanitize.php");
require_once("includes/functions.php");

$istisna = onlineGuncelle();
$online_count = getOnlinePeople();
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Kivvii - Hiç tanımadığınız kişilerle isimsiz sohbet edin! Haydi sen de Kivviile!</title>
<meta name="description" content="Kivvii ile hiç tanımadığınız biriyle isimsiz sohbet edebilirsiniz. Haydi sen de Kivviile!" />
<meta name="keywords" content="sohbet, chat, kivi, kivvii, kivviilemek, anlık mesajlaşma, anlık, mesaj, ileti, muhabbet, yabancı" />
<link rel="shortcut icon" href="images/kivi_fav.png" type="image/ico"/>
<link rel="stylesheet" type="text/css" href="index.css">
<link rel="stylesheet" type="text/css" href="reset.css">
<link rel="stylesheet" type="text/css" href="all.css">
<!--[if IE]>
  <link rel="stylesheet" type="text/css" href="ie.css">
<![endif]-->

<script type="text/javascript" src="js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="js/ping.js"></script>
<script type="text/javascript">
ping();

$(window).unload( function () {
	$.ajax({
		type: "GET",
		url: "ajax/offline",
		success: function(cevap){
		}
	});
} );
</script>
</head>

<body id="home">

<div id="hd">
	<h1><a href="index.php"><span class="hide"></span></a></h1>
	<ul id="nav">
	<li class="active"><a href="./" title="Anasayfa" alt="Anasayfa">Anasayfa</a></li>
	<li><a href="./kivviile" title="Haydi Sen de Kivviile" alt="Haydi Sen de Kivviile">Kivviile!</a></li>
	<li><a href="./gizlilik" title="Gizlilik Sözleşmesi" alt="Gizlilik Sözleşmesi">Gizlilik</a></li>
	<li><a href="./sss" title="Sıkça Sorulan Sorular" alt="Sıkça Sorulan Sorular">SSS</a></li>
	<li><a href="./hakkimizda" title="Hakkımızda" alt="Hakkımızda">Hakkımızda</a></li>
	<li><a href="./iletisim" title="İletişim" alt="İletişim">İletişim</a></li>
	</ul>
</div>

<div id="bd">
	<div id="rssems-wrapper">
		<div id="rssems">
			<h2 class="heading-home"><span style="display: none">Slogan: </span>İsimsiz sohbet edin!</h2>
			<p class="intro">
			Kivvii ile hiç tanımadığınız kişilerle isimsiz sohbet edebilirsiniz.
			Sadece aşağıdaki <b>Kivviile !</b> butonuna basın ve karşınıza
			gelecek rasgele bir kişi ile konuşmaya başlayın :)
			</p>
			
			<?
			$browserfail = 0;
			$browser = GetBrowser();
			if ($browser == 'msie 6.0' || $browser == 'msie 5.5' || $browser == 'msie 7.0') 
				$browserfail = 1;

			if ($browserfail == 1) {
			echo "<div id='baslat'>Bu site Internet Explorer 7 ve önceki sürümlerde çalışmamaktadır.<br/>
			<a href='http://www.microsoft.com/windows/internet-explorer/worldwide-sites.aspx'><b>BURADAN</b></a> Internet Explorer 8'i yükleyebilirsiniz.<br/>
			Yinede bizim önerimiz <a href='http://www.getfirefox.com/'><b>Firefox</b></a> kullanmanız.</div>";
			}else{
				if (@$_COOKIE['kisi_id'] || $istisna == 1) {
				echo "<div id='baslat'>Konuşmaya Başlayabilmek için Javascript Aktif Olmalıdır<br/>Tarayıcınızdan Javascript'i aktif edin.</div>";
				}else{
				echo "<div id='baslat'>Tarayıcınızın Cookie(Çerez) tutma özelliği aktif değildir.<br/>Aktif ettikten sonra konuşmaya başlayabilirsiniz.</div>";
				}
			}
			?>
			
			<p class="sub-text">İstediğin an bağlantıyı kes ve başka bir sohbete geç.</p>
			<div class="hero">&nbsp;</div>
			<span class="clear-all"></span>
		</div>
	</div>
	
	<? if (($_COOKIE['kisi_id'] || $istisna == 1) && ($browserfail == 0)) { ?>
	<script type="text/javascript">
	function CheckJS() {
		document.getElementById('baslat').innerHTML = "<a href='kivviile' class='button-hero'>Konuşma Başlat</a>";
	}
	CheckJS();
	</script>
	<? } ?>

	<div class="wrap">
		<h3>Toplam <? echo $online_count; ?> kişi çevirimiçi!</h3>
	</div>

	<div class="wrap">
	<p>Kivvi, sohbet etmek isteyen kişileri buluşturur. Profil oluşturmanıza gerek kalmadan hemen sohbete başla butonuna tıklayın!</p>
	</div>
</div>

<? 
require("includes/footer.php");
printGoogleAnalytics();
?>
</body>
</html>
<?php
$db->close();
?>