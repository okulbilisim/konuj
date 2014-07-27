<?php
if(isset($_POST['mail']) && isset($_POST['isim'])){
	echo '<span class="basari">OLDUUU</span>';
	die;
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> 
<head> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
	<title>KONUj</title>
	<meta name="content-language" content="tr" />
	<meta name="rating" content="All" />
	<meta name="robots" content="index, all" />
	<meta name="classification" content="Social Networking" />
	<style type="text/css"> 
		body{background:#ccc;}
		#wrap{margin:250px auto 0 auto; background:#fff; color:#333;height:370px; width:400px; border:10px solid #eee; -moz-border-radius:15px; -webkit-border-radius:15px;}
		#wrap #container{position:relative; display:block; height:300px;}
		#wrap #description{font-family:Georgia,"Times New Roman",Times,serif;font-size:24px; line-height:32px;font-style:italic;clear:both; display:block; margin:20px; }
		#wrap #formWrap{display:block; margin:10px;}
		#wrap .in{border:1px solid #007E9A; -moz-border-radius:3px; -webkit-border-radius:3px; float:left; display:block; width:325px; padding:4px; margin:4px 0 4px 10px; font-size:14px; font-weight:bold;}
		#wrap .but{width:190px; background:#007E9A; color:#fff; text-decoration:none;}
		#wrap label span{float:left; display:block; width:150px;}
		#wrap #sonuc{display:block; float:left;width:325px;}
		strong{color:#007E9A;}
		.uyari{color:red;}
		.basari{color:green;}
	</style> 
</head> 
<body> 
	<div id="wrap"> 
		<div id="container">
			<div id="description" class="chracters"><span class="basari">KONUj</span>'dan ilk haberdar olacak janslı listeye kayıt...</div>
			<div id="formWrap">
				<form action="index.php" id="subs" method="post">
					<label><span>mail (lütfen)</span><input type="text" class="in" name="mail" id="mail"  /></label>
					<label><span>isim (yani)</span><input type="text" class="in" name="isim" id="isim" /></label>
					<label for="mesaj"><span>mesaj (çok seviniriz)</span></label>
					<textarea class="in" name="mesaj" id="mesaj"></textarea> 
					<div id="sonuc"></div>
					<a href="#" id="sub1" class="in but">beni de haberdar et</a>
				</form>
			</div>
		</div> 
	</div> 
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		$('#sub1').click(function(){
			$('#sonuc').html('oluyor...');
			$.ajax({
				url:'beta.php',
				data:$('#subs').serialize(),
				success:function(data){
					$('#sonuc').html(data);
				}
			});
			return false;
		});
	});
	</script>
	<script type="text/javascript">
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-9202645-1']);
	  _gaq.push(['_trackPageview']);

	  (function() {
		var ga = document.createElement('script');
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		ga.setAttribute('async', 'true');
		document.documentElement.firstChild.appendChild(ga);
	  })();

	</script>
</body> 
</html>