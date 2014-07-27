var logid = 0;
var aktifkonusma = 0;
var baglantikes = 0;
var yaziyor = 0;
var knockcdown = 0;
var SListVis = 0;

function SmileyListGoster() {
	if (SListVis == 0) {
		$('#SmileyList').slideDown("fast");
		SListVis = 1;
	}else{
		$('#SmileyList').slideUp("fast");
		SListVis = 0;
	}
}

function smileyekle(dosya) {
	if (!dosya) { return; }
	dosya = ":"+dosya.replace('.gif', ':');
	document.getElementById('chatmsg').value = document.getElementById('chatmsg').value + dosya;
	document.getElementById('chatmsg').focus;
	
	if (SListVis == 1) {
		$('#SmileyList').slideUp("fast");
		SListVis = 0;
	}
}

function FaviconDegis(fav) {
	if (!fav) { return; }
	var link = document.createElement('link');
    link.type = 'image/con';
    link.rel = 'shortcut icon';
    link.href = "images/"+fav+".png";
    document.getElementsByTagName('head')[0].appendChild(link);
}

// knock sesini calmak icin flash fonksyonlari.
function getFlashMovieObject(movieName){
  if (window.document[movieName]) {
    return window.document[movieName];
  }
  if (navigator.appName.indexOf("Microsoft Internet")==-1){
    if (document.embeds && document.embeds[movieName])
      return document.embeds[movieName]; 
  }
  else{
    return document.getElementById(movieName);
  }
}

function PlayFlashMovie(){
	var flashMovie=getFlashMovieObject("knock");
	flashMovie.Play();
}
// knock sesini calmak icin flash fonksyonlari.

function KnockGonder() {
	if (baglantikes != 0) { return; }
	if (aktifkonusma == 0) { return; }
	if (knockcdown != 0) { return; }
	knockcdown = 10;
	
	$('input#chatmsg').val('_sescal.knock');
	KonusmaGonder();
}

function BaslikReset() {
	if (document.title != "Kivvii") {
		document.title = "Kivvii";
		FaviconDegis("kivi_fav");
	}
}

function divKaydir(){
	var objDiv = document.getElementById("mesajlar");
	objDiv.scrollTop = objDiv.scrollHeight;
}

function StatusLogEkle(mesaj) {
	if (mesaj == '') { return; }
	logid = logid + 1;
	var suanki = document.getElementById('mesajlar').innerHTML;
	document.getElementById('mesajlar').innerHTML = suanki + "<div id='logid"+logid+"' class='logitem'><div class='statuslog'>" + mesaj + "</div></div>";
	divKaydir();
}

function YabKonusmaEkle(mesaj) {
	if (mesaj == '') { return; }
	logid = logid + 1;
	var suanki = document.getElementById('mesajlar').innerHTML;
	document.getElementById('mesajlar').innerHTML = suanki + "<div style='display:none' id='logid"+logid+"' class='logitem'><div class='strangermsg'><span class='msgsource'>Yabanci:</span> " + parsesmiley(mesaj) + "</div></div>";
	$('#logid'+logid).fadeIn("fast");
	divKaydir();
	document.title = 'Y: '+mesaj;
	FaviconDegis("kivi_favalert");
}

function VeDenKurtul(str) {
	// & ve % isaretlerinden kurtul
	str = str.replace(/&/g, '-.-amp-.-');
	str = str.replace(/%/g, '-.-yzd-.-');
	return str;
}

function htmlspecialchars(p_string) {
	p_string = p_string.replace(/&/g, '&amp;');
	p_string = p_string.replace(/</g, '&lt;');
	p_string = p_string.replace(/>/g, '&gt;');
	p_string = p_string.replace(/"/g, '&quot;');
//	p_string = p_string.replace(/'/g, '&#039;');
	return p_string;
};


function KonusmaGonder() {
	if (aktifkonusma == 0) { return; }
	if (baglantikes != 0) { return; }
	var mesaj = $('input#chatmsg').val();
	if (!mesaj) { return; }
	$('input#chatmsg').val('');
	
	var mesaj2 = VeDenKurtul(mesaj);
	
	if (mesaj != '_sescal.knock') {
		logid = logid + 1;
		var suanki = document.getElementById('mesajlar').innerHTML;
		mesaj = htmlspecialchars(mesaj);
		document.getElementById('mesajlar').innerHTML = suanki + "<div id='logid"+logid+"' class='logitem'><div class='youmsg'><span class='msgsource'>Sen:</span> " + parsesmiley(mesaj) + "</div></div>";
		divKaydir();
	}else{
		PlayFlashMovie();
		StatusLogEkle('[*] Karsidakinin ekranini tiklattiniz!');
	}
	
	$.ajax({
		type: "POST",
		url: "ajax/mesajgonder",
		data: "&mesaj="+mesaj2,
		success: function(cevap){
			if (aktifkonusma == 0) { return; }
			
			if (cevap.substr(0, 5) == 'Hata:') {
				StatusLogEkle(cevap);
			} /*else{
				if (cevap != '_sescal.knock') {
					logid = logid + 1;
					var suanki = document.getElementById('mesajlar').innerHTML;
					document.getElementById('mesajlar').innerHTML = suanki + "<div id='logid"+logid+"' class='logitem'><div class='youmsg'><span class='msgsource'>Sen:</span> " + cevap + "</div></div>";
					divKaydir();
				}else{
					PlayFlashMovie();
					StatusLogEkle('[*] Karsidakinin ekranini tiklattiniz!');
				}
			}*/
		}
	});
}

function EnteraBas(e) {
	if (e.keyCode == 13) {
		KonusmaGonder();
	}
}

function iletikontrol()	{
	if (aktifkonusma == 0) { return; }
	if (baglantikes != 0) { return; }
	
	if ($('input#chatmsg').val() != "") {
		if (yaziyor == 0) {
			yaziyor = 1;
			
			$.ajax({
			type: "GET",
			url: "ajax/yaziyor",
			data: "val=1",
			success: function(cevap){
			}
		});
		}
	}else{
		if (yaziyor == 1) {
			yaziyor = 0;
			
			$.ajax({
			type: "GET",
			url: "ajax/yaziyor",
			data: "val=0",
			success: function(cevap){
			}
		});
		}
	}
	setTimeout("iletikontrol()", 250);
}

function KonusmadanCik() {
	if (aktifkonusma == 0) { return; }
	var doIt = confirm('Bu konusmadan ayrilmak istedigine emin misin?');
	if (doIt) {
		baglantikes = 1;
		$('#iletiyaziyor').fadeOut("fast");
		$.ajax({
			type: "GET",
			url: "ajax/baglantikes",
			success: function(cevap){
				if (aktifkonusma == 0) { return; }
				
				if (cevap.substr(0, 5) == 'Hata:') {
					StatusLogEkle(cevap);
				}
				if (cevap.substr(0, 2) == 'ok') {
					StatusLogEkle("Konusmadan ayrildiniz. Yeni bir konusma baslatmak icin <a onclick='ChateBasla();'>buraya tiklayin</a>.");
					StatusLogEkle("-- <a href='kaydet?id="+aktifkonusma+"'>Konusma Gecmisini Kaydet</a> --");
					aktifkonusma = 0;
					//..
				}
				
				baglantikes = 0;
			}
		});
	}
}

function MesajlariAl() {
	if (aktifkonusma == 0) { return; }
	if (baglantikes != 0) { return; }
	$.ajax({
		type: "GET",
		url: "ajax/mesajsorgu",
		success: function(cevap){
			if (aktifkonusma == 0) { return; }
			
			if (cevap.substr(0, 5) == 'Hata:') {
				StatusLogEkle(cevap);
				setTimeout("MesajlariAl()", 1000);
			}else{
				if (cevap != "") {
					if (cevap == "_sescal.knock") {
						PlayFlashMovie();
						StatusLogEkle('[*] Karsidaki sizin ekraninizi tiklatti!');
					}else{
						YabKonusmaEkle(cevap);
					}
				}
				setTimeout("MesajlariAl()", 1000); //bu 500 olacakti
			}
		}
	});
}

function DurumGuncelle() {
	if (aktifkonusma == 0) { return; }
	if (baglantikes != 0) { return; }
	
	if (knockcdown > 0) knockcdown--;
	
	$.ajax({
		type: "GET",
		url: "ajax/durum",
		success: function(cevap){
			if (aktifkonusma == 0) { return; }
			
			if (cevap.substr(0, 5) == 'Hata:') {
				StatusLogEkle(cevap);
				setTimeout("DurumGuncelle()", 1000);
			}
			if (cevap.substr(0, 5) == 'aktif') {
				var cvparry = cevap.split(',');
				if (cvparry[1] == '1') {
					$('#iletiyaziyor').fadeIn("fast");
				}else{
					$('#iletiyaziyor').fadeOut("fast");
				}
				
				setTimeout("DurumGuncelle()", 1000); //bu 500 olacakti
			}
			if (cevap.substr(0, 7) == 'inaktif') {
				StatusLogEkle("Karsi taraf baglantiyi kesti. Yeni bir konusma baslatmak icin <a onclick='ChateBasla();'>buraya tiklayin</a>.");
				StatusLogEkle("-- <a href='kaydet?id="+aktifkonusma+"'>Konusma Gecmisini Kaydet</a> --");
				aktifkonusma = 0;
				$('#iletiyaziyor').fadeOut("fast");
			}
			if (cevap.substr(0, 7) == 'timeout') {
				StatusLogEkle("Karsi tarafin baglantisi kopmus gorunuyor. Yeni bir konusma baslatmak icin <a onclick='ChateBasla();'>buraya tiklayin</a>.");
				aktifkonusma = 0;
				$('#iletiyaziyor').fadeOut("fast");
			}
		}
	});
}

var BeklemeSure = 0;

function Bekleme() {
	if (aktifkonusma == 0) { return; }
	if (baglantikes != 0) { return; }
	$.ajax({
		type: "GET",
		url: "ajax/bekleme",
		success: function(cevap){
			if (aktifkonusma == 0) { return; }
			
			if (cevap.substr(0, 5) == 'Hata:') {
				StatusLogEkle(cevap);
				setTimeout("Bekleme()", 1000);
			}
			if (cevap.substr(0, 5) == 'bekle') {
				setTimeout("Bekleme()", 1000); //bu 500 olacakti
				BeklemeSure++;
				if (BeklemeSure == 20) {
					StatusLogEkle("Hala birinin baglanmasi bekleniyor ...");
					BeklemeSure = 0;
				}
			}
			
			if (cevap.substr(0, 5) == 'aktif') {
				StatusLogEkle('Suan rasgele biriyle konusuyorsun. Selam yaz!');
				FaviconDegis("kivi_favalert");
				document.title = ("Kivvi: Selam Yaz!");
				DurumGuncelle();
				iletikontrol();
				MesajlariAl();
			}
			
			if (cevap.substr(0, 5) == 'kapat') {
				StatusLogEkle("Birisi baglandi fakat hemen baglantiyi kesti. Yeni bir konusma baslatmak icin <a onclick='ChateBasla();'>buraya tiklayin</a>.");
				aktifkonusma = 0;
			}
		}
	});
}

function ChateBasla() {
	if (aktifkonusma != 0) { return; }
	document.getElementById('mesajlar').innerHTML = "";
	StatusLogEkle('Baglaniliyor...');

	$.ajax({
		type: "GET",
		url: "ajax/bul",
		success: function(cevap){
			if (aktifkonusma != 0) { return; }
			
			if (cevap.substr(0, 5) == 'Hata:') {
				StatusLogEkle(cevap);
			}
			
			if (cevap.substr(0, 5) == 'banli') {
				StatusLogEkle("Uzgunum, banli oldugunuzdan dolayi konusmaya katilamazsiniz.");
			}
			
			if (cevap.substr(0, 5) == 'aktif') {
				// zaten bir konusma aktif.
				var cvparry = cevap.split(',');
				aktifkonusma = cvparry[1];
				if (cvparry[2] == 'bekleme') {
					StatusLogEkle("Aktif olan konusma tekrar acildi, devam ediliyor...");
					StatusLogEkle("Lutfen sayfayi kapatmayin. Konusmaya birinin katilmasi bekleniyor...");
					Bekleme();
				}
				
				if (cvparry[2] == 'aktif') {
					StatusLogEkle("Aktif olan konusma tekrar acildi, devam edebilirsiniz.");
					StatusLogEkle("Bu konusmadan cikmak icin <a onclick='KonusmadanCik();'>Baglantiyi Kes</a>'e tiklayin.");
					DurumGuncelle();
					iletikontrol();
					MesajlariAl();
				}
			}
			
			if (cevap.substr(0, 7) == 'bulundu') {
				// konusma bulundu
				StatusLogEkle('Suan rasgele biriyle konusuyorsun. Selam yaz!');
				var cvparry = cevap.split(',');
				aktifkonusma = cvparry[1];
				DurumGuncelle();
				iletikontrol();
				MesajlariAl();
			}
			
			if (cevap.substr(0, 10) == 'bekleniyor') {
				// beklemede
				StatusLogEkle('Lutfen sayfayi kapatmayin. Konusmaya birinin katilmasi bekleniyor...');
				var cvparry = cevap.split(',');
				aktifkonusma = cvparry[1];
				Bekleme();
			}
		}
	});
}


$(window).unload( function () {
	$.ajax({
		type: "GET",
		url: "ajax/offline",
		success: function(cevap){
		}
	});
} );

window.onbeforeunload = function () {
	if (aktifkonusma != 0) {
	return "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\nCikarsaniz konusma sonlanacak. Emin misiniz ?\n_______________________________________";
	}
}
