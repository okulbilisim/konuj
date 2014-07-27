<?php
include("EmailAddressValidator.php");

function is_valid_email($email) {

	$validator = new EmailAddressValidator;
	if ($validator->check_email_address($email)) {
		return TRUE;
	} else {
		return FALSE;
	}
} 

function check_injection($str){
	return (preg_match("/(\r|\n|%0a|%0d)(to:|from:|cc:|bcc:|Content-Type:|Mime-Version:|Content-Transfer-Encoding:)/",$str));
	
}

?>