<? 
ob_start(); 
session_start(); 
if(@isset($_SESSION['hatali_giris_sayisi']) && (@$_SESSION['hatali_giris_sayisi'] == 3)){
	die();
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="0">
<title>Admin Panel</title>
</head>

<body>
    <form name="loginform" action="index.php" method="POST">
        Şifre: <input type="password" name="passwordfield"/>
        <input type="submit" value="Giriş" name="submit" />
    </form>
</body>

<?
@require_once("../includes/config.php");
if ($_COOKIE['adminpass']) {
    //cookie var

    if ($_COOKIE['adminpass'] == $adminpass) {
        header('Location: konusmalar.php');
    }else{
        $_COOKIE['adminpass'] = "";
        die("Hatalı şifre. <a href='index.php'>Tekrar dene</a>");
    }

}else{
    //cookie yok
    if (@$_POST) {
        //post var
        $girilen = @$_POST['passwordfield'];
        if ($girilen == $adminpass) {
            //sifre dogru
			@$_SESSION['hatali_giris_sayisi'] = 0;
            setcookie("adminpass", $girilen, strtotime('+1 month'), "/");
            header('Location: konusmalar.php');
        }else{
            //sifre yanlis
			if(@isset($_SESSION['hatali_giris_sayisi'])){
				@$_SESSION['hatali_giris_sayisi'] = @$_SESSION['hatali_giris_sayisi'] + 1;
			}
			else{
				@$_SESSION['hatali_giris_sayisi'] = 1;
			}
            @$_COOKIE['adminpass'] = "";
            die("Hatalı şifre. <a href='index.php'>Tekrar dene</a>");
        }
    }
}

?>