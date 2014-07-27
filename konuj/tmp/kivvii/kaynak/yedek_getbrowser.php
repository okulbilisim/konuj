<?
function GetBrowser(){
	$browsers = "mozilla msie gecko firefox ";
	$browsers.= "konqueror safari netscape navigator ";
	$browsers.= "opera mosaic lynx amaya omniweb chrome";

	$browsers = split(" ", $browsers);

	$nua = strToLower( $_SERVER['HTTP_USER_AGENT']);

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
?>