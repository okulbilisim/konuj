function ping() {
  $.ajax({
	type: "GET",
	url: "ajax/ping.php",
	success: function(cevap){
    }
  });
  
  setTimeout("ping()", 5000); // 5 saniyede bir pingle
}
