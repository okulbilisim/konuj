/*
 * contactable 1.2.1 - jQuery Ajax contact form
 *
 * Copyright (c) 2009 Philip Beel (http://www.theodin.co.uk/)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * Revision: $Id: jquery.contactable.js 2010-01-18 $
 *
 */
 
//extend the plugin
(function($){

	//define the new for the plugin ans how to call it	
	$.fn.contactable = function(options) {
		//set default options  
		var defaults = {
			name: 'İsim',
			email: 'E-posta',
			message : 'Mesajınız',
			subject : 'KONUj: Ggeri bildirim',
			recievedMsg : 'Mesajınız için tejekkürler',
			notRecievedMsg : 'Şu anda mesajınızı gönderemedik, lütfen daha sonra tekrar deneyiniz',
			disclaimer: 'Lütfen düşüncelerinizi bizimle paylaşın, konujun',
			hideOnSubmit: true
		};

		//call in the default otions
		var options = $.extend(defaults, options);
		//act upon the element that is passed into the design    
		return this.each(function(options) {
			//construct the form
			$(this).html('<div id="contactable"></div><form id="contactForm" method="" action=""><div id="loading"></div><div id="callback"></div><div class="holder"><p><label for="name">Adınız <span class="red"> * </span></label><br /><input id="name" class="contact" name="name" /></p><p><label for="email">E-Mail <span class="red"> * </span></label><br /><input id="email" class="contact" name="email" /></p><p><label for="comment">Mesajınız <span class="red"> * </span></label><br /><textarea id="comment" name="comment" class="comment" rows="4" cols="30" ></textarea></p><p><input class="submit" type="submit" value="Gönder"/></p><p class="disclaimer">'+defaults.disclaimer+'</p></div></form>');
			//show / hide function
			$('div#contactable').toggle(function() {
				$('#overlay').css({display: 'block'});
				$(this).animate({"marginRight": "-=5px"}, "fast"); 
				$('#contactForm').animate({"marginRight": "-=0px"}, "fast");
				$(this).animate({"marginRight": "+=387px"}, "slow"); 
				$('#contactForm').animate({"marginRight": "+=390px"}, "slow"); 
			}, 
			function() {
				$('#contactForm').animate({"marginRight": "-=390px"}, "slow");
				$(this).animate({"marginRight": "-=387px"}, "slow").animate({"marginRight": "+=5px"}, "fast"); 
				$('#overlay').css({display: 'none'});
			});
			
			//validate the form 
			$("#contactForm").validate({
				//set the rules for the fild names
				rules: {
					name: {
						required: true,
						minlength: 2
					},
					email: {
						required: true,
						email: true
					},
					comment: {
						required: true
					}
				},
				//set messages to appear inline
					messages: {
						name: "",
						email: "",
						comment: ""
					},			

				submitHandler: function() {
					$('.holder').hide();
					$('#loading').show();
					// POST'tan GET yapacak
					$.getJSON('/mail.php',{subject:defaults.subject, name:$('#name').val(), email:$('#email').val(), comment:$('#comment').val()},islemTamam);
					// return edecek şey [{"sonuc":"success"}]
					function islemTamam(data){
						$('#loading').css({display:'none'}); 
						if(data[1].sonuc == 'success') {
							$('#callback').show().append(defaults.recievedMsg);
							if(defaults.hideOnSubmit == true) {
								//hide the tab after successful submition if requested
								$('#contactForm').animate({dummy:1}, 2000).animate({"marginRight": "-=450px"}, "slow");
								$('div#contactable').animate({dummy:1}, 2000).animate({"marginRight": "-=447px"}, "slow").animate({"marginRight": "+=5px"}, "fast"); 
								$('#overlay').css({display: 'none'});	
							}
						} else {
							$('#callback').show().append(defaults.notRecievedMsg);
						}
					});		
				}
			});
		});
	};
})(jQuery);