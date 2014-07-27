<?php
function GetBrowser(){
	$browsers = "mozilla msie gecko firefox ";
	$browsers.= "konqueror safari netscape navigator ";
	$browsers.= "opera mosaic lynx amaya omniweb chrome";

	$browsers = split(" ", $browsers);

	$nua = strToLower( @$_SERVER['HTTP_USER_AGENT']);

	$l = strlen($nua);
	for ($i=0; $i<count($browsers); $i++){
	  $browser = $browsers[$i];
	  $n = stristr($nua, $browser);
	  if(strlen($n)>0){
	    $GLOBALS["ver"] = "";
	    $GLOBALS["nav"] = $browser;
	    $j=strpos($nua, $GLOBALS["nav"])+$n+strlen($GLOBALS["nav"])+1;
	    for (; $j<=$l; $j++){
	      $s = substr ($nua, $j, 1);
	      if(is_numeric($GLOBALS["ver"].$s) )
	      $GLOBALS["ver"] .= $s;
	      else
	      break;
	    }
	  }
	}

	return $GLOBALS["nav"] . " " . $GLOBALS["ver"];
}

function getOnlinePeople()
{
	global $db, $debug;

	$query  = "SELECT id, sonping, online ";
	$query .= "FROM kisiler ";
	$query .= "WHERE online = 1";

	$db->query($query, $debug);

	$online_count = 0;

	while($row = $db->get_row()){
		$sonping = $row['sonping'];
		$id = $row['id'];
		$tarih = time();
		if (($tarih - $sonping) > 60) {
			$query2 = "UPDATE kisiler SET online = '0' WHERE id = '$id' ";
			$db->query($query2, $debug);
		}else{
			$online_count++;
		}
	}

	return $online_count;
}

function validip($ip) {
    if (!empty($ip) && ip2long($ip)!=-1) {
        $reserved_ips = array (
        array('0.0.0.0','2.255.255.255'),
        array('10.0.0.0','10.255.255.255'),
        array('127.0.0.0','127.255.255.255'),
        array('169.254.0.0','169.254.255.255'),
        array('172.16.0.0','172.31.255.255'),
        array('192.0.2.0','192.0.2.255'),
        array('192.168.0.0','192.168.255.255'),
        array('255.255.255.0','255.255.255.255')
        );
 
        foreach ($reserved_ips as $r) {
            $min = ip2long($r[0]);
            $max = ip2long($r[1]);
            if ((ip2long($ip) >= $min) && (ip2long($ip) <= $max)) return false;
        }
        return true;
    } else {
        return false;
    }
 }
 
 function getip() {
    if (validip(@$_SERVER["HTTP_CLIENT_IP"])) {
        return @$_SERVER["HTTP_CLIENT_IP"];
    }
    foreach (explode(",",@$_SERVER["HTTP_X_FORWARDED_FOR"]) as $ip) {
        if (validip(trim($ip))) {
            return $ip;
        }
    }
    if (validip(@$_SERVER["HTTP_X_FORWARDED"])) {
        return @$_SERVER["HTTP_X_FORWARDED"];
    } elseif (validip(@$_SERVER["HTTP_FORWARDED_FOR"])) {
        return @$_SERVER["HTTP_FORWARDED_FOR"];
    } elseif (validip(@$_SERVER["HTTP_FORWARDED"])) {
        return @$_SERVER["HTTP_FORWARDED"];
    } elseif (validip(@$_SERVER["HTTP_X_FORWARDED"])) {
        return @$_SERVER["HTTP_X_FORWARDED"];
    } else {
        return @$_SERVER["REMOTE_ADDR"];
    }
 }

function onlineGuncelle()
{
	global $db, $debug;

	$kisi_id = sanitize_int(@$_COOKIE['kisi_id']);
	$tarih = time();
	$kisi_ip = getip();

	$istisna = 0;


	if ($kisi_id) {
		$kisi_id = $db->safesql($kisi_id);
		$query  = "SELECT online FROM kisiler WHERE id = ".$kisi_id;
		$query_id = $db->query($query, $debug);
		if($db->num_rows($query_id) == 0){
			$kisi_id = "";
		}
		else{
			$kisi_ip = $db->safesql($kisi_ip);
			$query = "UPDATE kisiler SET sonping = '".$tarih."', online = '1', IP = '".$kisi_ip."' WHERE id = '".$kisi_id."' ";
			$db->query($query, $debug);
		}
	}
	else{
		$kisi_ip = $db->safesql($kisi_ip);
		$query = "INSERT INTO kisiler (IP, online, aktifkonusma, sonping) VALUES ('".$kisi_ip."', '1', '0', '".$tarih."')";
		$db->query($query, $debug);
		$kisi_id = $db->insert_id();
		
		setcookie("kisi_id", $kisi_id, strtotime('+1 month'), "/");
		$istisna = 1;
	}

	return $istisna;
}

function printGoogleAnalytics()
{
	global $google_id;
?>
	<script type="text/javascript">
	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
	document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script type="text/javascript">
	try {
	var pageTracker = _gat._getTracker("<?=$google_id?>");
	pageTracker._trackPageview();
	} catch(err) {}</script>
<?
}

function VeEkle($str) {
	// cikartilan & ve % karakterlerini tekrar ekle
    $str = str_replace('-.-amp-.-', '&', $str);
	$str = str_replace('-.-yzd-.-', '%', $str);
    return $str;
}
?>