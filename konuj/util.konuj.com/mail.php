<?php
error_reporting(E_ALL & ~E_NOTICE & ~E_DEPRECATED);  
include("functions.php");

$name = addslashes($_GET['name']);
$email = addslashes($_GET['email']);
$valid = is_valid_email($email);
$inject = check_injection(addslashes($_GET['email']));
$comment = addslashes($_GET['comment']);
$subject = "[KONUj'dan Mesaj Var]";
$result='';
if(!$name){
	$result=array ('sonuc'=>'hatalı isim girdiniz'); 
}

if(!$valid){
	$result= array ('sonuc'=>'hatalı eposta adresi girdiniz'); 
}

if($inject){
	$result= array ('sonuc'=>'ne yapmaya çalıjıyorsunuz? komik olmayın, bilgileriniz şimdi loglanmadı mı sanıyorsunuz?'); 
}

if(!$comment){
	$result= array ('sonuc'=>'hatali mesaj girdiniz'); 
}

if ($name && $valid && $comment && !$inject){
	
	$headers = "From:". $name ." <". $email .">\r\n" .
	"Reply-To: ". $email ." \r\n" .
	"X-Mailer: KONUj-Mailer/1.0";
	
	mail("konuj@konuj.com", $subject, $comment, $headers);
	$result= array ('sonuc'=>'success'); 
	
} else {
	$result= array ('sonuc'=>'hatalı bilgi girdiniz'); //email was not valid
}		
echo json_encode($result);
?>