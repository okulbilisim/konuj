<?
$dbhost = 'localhost'; // Mysql Host
$dbuser = 'root';      // Mysql User
$dbpass = '';  // Mysql Password
$dbname = 'kivvii';    // Database
$collate = 'utf8'; // Database Collate

$debug = false;
if($debug){
	$db_debug = 1;
	error_reporting(E_ALL);
}
else{
	$db_debug = 0;
	error_reporting(0);
}

$adminpass = "123456";

# Google Analytics ID
$google_id = "UA-735148-6";

?>