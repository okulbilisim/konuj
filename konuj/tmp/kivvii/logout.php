<?php
session_start();
setcookie("kisi_id", "", time() - 3600, "/");
session_destroy();
header("Location: ./");
die();
?>