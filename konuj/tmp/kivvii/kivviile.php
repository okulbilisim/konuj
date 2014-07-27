<? 
ob_start(); 
session_start();

require_once("includes/config.php");
require_once("includes/mysql.class.php");

$db = new db($dbhost, $dbuser, $dbpass, $dbname, $collate, $db_debug);

require_once("includes/sanitize.php");
require_once("includes/functions.php");

$browser = GetBrowser();
if ($browser == 'msie 6.0' || $browser == 'msie 5.5' || $browser == 'msie 7.0') {
	header('Location: ./');
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<title>Kivvii</title>
<link type="text/css" rel="stylesheet kivvii" href="temalar/kivvii.css" title='kivvii' />
<link type="text/css" rel="stylesheet mavi" href="temalar/mavi.css" title='mavi' />
<link type="text/css" rel="stylesheet kirmizi" href="temalar/kirmizi.css" title='kirmizi' />
<link type="text/css" rel="stylesheet karamel" href="temalar/karamel.css" title='karamel' />
<link type="text/css" rel="stylesheet darkred" href="temalar/darkred.css" title='darkred' />
<link type="text/css" rel="stylesheet darkblue" href="temalar/darkblue.css" title='darkblue' />
<link type="text/css" rel="stylesheet" href="temalar/cizgi.css" title='cizgi' />

<link rel="shortcut icon" href="images/kivi_fav.png" type="image/ico"/>
<script type="text/javascript" src="js/styleswitcher.js"></script>
<script type="text/javascript" src="js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="js/ping.js"></script>
<?
$kisi_id = sanitize_int(@$_COOKIE['kisi_id']);
if ($kisi_id) {
	$kisi_id = $db->safesql($kisi_id);
	$query  = "SELECT online FROM kisiler WHERE id = $kisi_id";
	$db->query($query, $debug);
	$num_rows = $db->num_rows();
	if ($num_rows == 0) {
		$kisi_id = "";
		setcookie("kisi_id", $kisi_id, strtotime('+1 month'), "/");
		$db->close();
		header('Location: ./');
		die();
	}
}
if (!$kisi_id) {
	header('Location: ./');
	die();
}
$istisna = onlineGuncelle();
$online_count = getOnlinePeople();
?>
<script type="text/javascript">ping();</script>
<script type="text/javascript">
function parsesmiley(text) {
	if (!text) { return; }
	<?
	$directory = "images/smileys";
	$results = array();
	$handler = opendir($directory);
	while ($file = readdir($handler)) {
		if ($file != '.' && $file != '..')
			$results[] = $file;
	}
	closedir($handler);
	
	for ($i=0; $i<count($results); $i++){
		if (substr($results[$i], -3, 3) == "gif") {
			$temp = str_replace('.gif', '', $results[$i]);
			echo "text = text.replace(/:$temp:/g, \"<img src='images/smileys/$temp.gif'/>\"); \n";
		}
	}
	?>
	
	text = text.replace(/:\)/g, "<img title=':)' src='images/smileys/smile.gif'/>");
	text = text.replace(/:\(/g, "<img title=':)' src='images/smileys/sad.gif'/>");
	text = text.replace(/;\)/g, "<img title=':)' src='images/smileys/wink.gif'/>");
	text = text.replace(/:D/gi, "<img title=':D' src='images/smileys/laugh.gif'/>");
	text = text.replace(/:O/gi, "<img title=':O' src='images/smileys/ohmy.gif'/>");
	text = text.replace(/:S/gi, "<img title=':S' src='images/smileys/wtf.gif'/>");
	text = text.replace(/:P/gi, "<img title=':P' src='images/smileys/tongue.gif'/>");
	return text;
}
</script>
<script type="text/javascript" src="js/chat.js"></script>

</head>

<body onmousemove="BaslikReset();" class="inconversation">

<div class="chatbox">

<div align='right' id='temalar'>
<div id='temasec'>Tema Seç:</div>
<a onclick="setActiveStyleSheet('kivvii'); return false;"><div id='kivvii'></div></a>
<a onclick="setActiveStyleSheet('mavi'); return false;"><div id='mavi'></div></a>
<a onclick="setActiveStyleSheet('kirmizi'); return false;"><div id='kirmizi'></div></a>
<a onclick="setActiveStyleSheet('karamel'); return false;"><div id='karamel'></div></a>
<a onclick="setActiveStyleSheet('darkred'); return false;"><div id='darkred'></div></a>
<a onclick="setActiveStyleSheet('darkblue'); return false;"><div id='darkblue'></div></a>
<a onclick="setActiveStyleSheet('cizgi'); return false;"><div id='cizgi'></div></a>
</div>

<div id='title'>Kivvii | Beta</div>

<div id='onlinesay'>Şuan <? echo $online_count; ?> kişi online</div>

<div style="top: 40px;" class="logwrapper">
	<div id='mesajlar' class="logbox">
		<div class="logitem">
		<div class="statuslog">Yükleniyor...</div>
		</div>
	</div>
	
	<div id='iletiyaziyor'>
		Yabancı, ileti yazıyor...
	</div>
</div>

<div class="controlwrapper">
	<table class="controltable" border="0" cellpadding="0" cellspacing="0">
	<tbody><tr>
		<td class="smileybtncell">
		<div class="smileybtnwrapper"><input onclick="SmileyListGoster();" value="" class="smileybtn" type="button"/></div>
		</td>
		
		<div id='SmileyList'>
			<?
			for ($i=0; $i<count($results); $i++){
				if (substr($results[$i], -3, 3) == "gif")
					echo "<div onclick=\"smileyekle('".$results[$i]."');\" class='smile'><img src='images/smileys/".$results[$i]."'/></div>";
			}
			?>
		</div>
	
		<td class="chatmsgcell">
		<div class="chatmsgwrapper"><input type="text" id="chatmsg" class="chatmsg" onkeydown='EnteraBas(event)'/></div>
		</td>

		<td class="sendbthcell">
		<div class="sendbtnwrapper"><input onclick='KonusmaGonder();' value="Gönder" class="sendbtn" type="button"/></div>
		</td>
		
		<td class="knockbthcell">
		<div class="knockbtnwrapper"><input onclick='KnockGonder();' value="Tık Tık Tık!" class="knockbtn" type="button"/></div>
		</td>
		
		<td class="disconnectbtncell">
		<div class="disconnectbtnwrapper"><input onclick="KonusmadanCik();" value="Bağlantıyı Kes" class="disconnectbtn" type="button"/></div>
		</td>
	</tr></tbody>
	</table>
</div>
	
</div>

<div id='divknock'>
<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="1" height="1" id="knock" align="middle">
<param name="allowScriptAccess" value="sameDomain" />
<param name="allowFullScreen" value="false" />
<param name="movie" value="knock.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#ffffff" />
<embed src="images/knock.swf" quality="high" bgcolor="#ffffff" width="1" height="1" name="knock" align="middle" allowScriptAccess="sameDomain" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.adobe.com/go/getflashplayer" />
</object>
</div>

<script type="text/javascript">
ChateBasla();
</script>

<?php
printGoogleAnalytics();
?>
</body>
</html>