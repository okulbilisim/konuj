var isTyping = false;
var isChatting = false;
var isStrangerConnected = false;
var isBoxyOpen = false;
var state = 'startchat';
var konujBoxy;

var firstMessages = [
    "Yak\u0131n zamanda iyi bir konser var m\u0131?", 
    "Ufff, mola vermeye ihtiyac\u0131m var. Chat yapmaya ne dersin?", 
    "G\u00f6sterime giren iyi bir film var m\u0131?", 
    "E\u011fer bir s\u00fcper g\u00fcc\u00fcn olsayd\u0131 ne olurdu? G\u00f6r\u00fcnmez olmak m\u0131?", 
    "Buralarda bisiklet kiralayan iyi bir yer biliyor musun?", 
    "Hey, d\u00fcn geceki ma\u00e7\u0131 g\u00f6rd\u00fcn m\u00fc?", 
    "Ben avc\u0131 de\u011filim,sadece konu\u015fmak istiyorum(ve eve kadar takip edicem).", 
    "Hey, ne kadar zamand\u0131r burada ya\u015f\u0131yorsun?", 
    "Bu haftasonu i\u00e7in yapacak bi\u015feyler ar\u0131yorum.Fikri olan var m\u0131?", 
    "Yard\u0131m et, s\u0131k\u0131ld\u0131m!!!", 
    "\u0130yi bir gece kul\u00fcb\u00fc biliyor musun?\r\n", 
    "\u0130yi \u0130talyan restoran\u0131 biliyor musun?", 
    "Ben bir devlet ajan\u0131y\u0131m; s\u0131r saklayabilir misin?", 
    "Hey, tatile gitmek i\u00e7in iyi bir yer tavsiye edebilir misin?", 
    "Buralarda canl\u0131 m\u00fczik dinlenebilecek en iyi yeri biliyor musun?", 
    "2010 D\u00fcnya Kupas\u0131 i\u00e7in G\u00fcney Afrika'ya gidece\u011fim. Benimle gelmek ister misin?", 
    "Hey, yak\u0131nlarda m\u00fczik festivali var m\u0131?", 
    "Bak\u0131yorum bir \u00e7ok ortak noktam\u0131z var. Sence de \u00f6yle de\u011fil mi?", 
    "\u00c7ok d\u0131\u015far\u0131ya \u00e7\u0131kar m\u0131s\u0131n? Hadi piste beraber \u00e7\u0131kal\u0131m!", 
    "Foto\u011fraflardan bahsetmek ister misin acaba?", 
    "Kahve i\u00e7icisi misin \u00e7ay m\u0131? Ne i\u00e7ti\u011fine bak\u0131p ki\u015filik hakk\u0131nda bir \u00e7ok \u015fey \u00f6\u011frenilebiliyormu\u015f.", 
    "D\u00fcnyay\u0131 ele ge\u00e7irmeme yard\u0131m etmek ister misin?", 
    "Skydiving? Hadi gidelim?", 
    "Ger\u00e7ekten biraz araya ihtiyac\u0131m var. Muhabbet edelim mi?", 
    "Son alb\u00fcm\u00fcm\u00fc g\u00f6rd\u00fcn m\u00fc?", 
    "Hi\u00e7 Sahne I\u015f\u0131klar\u0131n\u0131 kulland\u0131n m\u0131?", 
    "D\u00fcnyada gidecek tek bir yer se\u00e7ecek olsan, o neresi olur?", 
    "Sence bilgisayar\u0131m\u0131 camdan ne kadar uza\u011fa f\u0131rlatabilirim?", 
    "Mac mi PC mi?", 
    "3 g\u00fcnl\u00fck haftasonuna oy verir miydin?", 
    "B\u00fcy\u00fck \u015fehir mi k\u00fc\u00e7\u00fck kasaba m\u0131?", 
    "Neden insanlar hep benim z\u0131mbam\u0131 \u00e7al\u0131yorlar?", 
    "Gelecek Olimpiyatlar nerede olacak biliyor musun?", 
    "K\u0131z\u0131l Meydan nerede?", 
    "Hi\u00e7 Birinci S\u0131n\u0131fta u\u00e7tun mu?", 
    "Vejeteryanlar hakk\u0131nda ne d\u00fc\u015f\u00fcn\u00fcyorsun?", 
    "Hangisini tercih edersin, y\u00fczme mi ko\u015fu mu?", 
    "Mutlu y\u0131llar dilemek i\u00e7in \u00e7ok mu ge\u00e7??", 
    "Rasgele anket: En sevdi\u011fin film ne?", 
    "Rasgele anket: En sevdi\u011fin grup hangisi?", 
    "Rasgele anket: En sevdi\u011fin futbol tak\u0131m\u0131 hangisi?"
];

function getRandomFirstMessage(){
    return firstMessages[Math.round(Math.random() * (firstMessages.length - 1))];
}

window.onbeforeunload= function (evt) {
    if(isChatting)
        return "Cidden konujmayı bitirejen?";   
}

window.onunload = function (evt) {
    if(isChatting)
        sendDisconnect();
}

function parsesmiley(messageText) {
  if(messageText) {
    messageText = messageText.replace(/&#58;angry&#58;/g, "<img src='/img/smileys/angry.gif'/>");
    messageText = messageText.replace(/&#58;biggrin&#58;/g, "<img src='/img/smileys/biggrin.gif'/>");
    messageText = messageText.replace(/&#58;blink&#58;/g, "<img src='/img/smileys/blink.gif'/>");
    messageText = messageText.replace(/&#58;blushing&#58;/g, "<img src='/img/smileys/blushing.gif'/>");
    messageText = messageText.replace(/&#58;closedeyes&#58;/g, "<img src='/img/smileys/closedeyes.gif'/>");
    messageText = messageText.replace(/&#58;confused1&#58;/g, "<img src='/img/smileys/confused1.gif'/>");
    messageText = messageText.replace(/&#58;cool&#58;/g, "<img src='/img/smileys/cool.gif'/>");
    messageText = messageText.replace(/&#58;cool2&#58;/g, "<img src='/img/smileys/cool2.gif'/>");
    messageText = messageText.replace(/&#58;cowboy&#58;/g, "<img src='/img/smileys/cowboy.gif'/>");
    messageText = messageText.replace(/&#58;crying&#58;/g, "<img src='/img/smileys/crying.gif'/>");
    messageText = messageText.replace(/&#58;cursing&#58;/g, "<img src='/img/smileys/cursing.gif'/>");
    messageText = messageText.replace(/&#58;devil&#58;/g, "<img src='/img/smileys/devil.gif'/>");
    messageText = messageText.replace(/&#58;flowers&#58;/g, "<img src='/img/smileys/flowers.gif'/>");
    messageText = messageText.replace(/&#58;fuck&#58;/g, "<img src='/img/smileys/fuck.gif'/>");
    messageText = messageText.replace(/&#58;glare&#58;/g, "<img src='/img/smileys/glare.gif'/>");
    messageText = messageText.replace(/&#58;huh&#58;/g, "<img src='/img/smileys/huh.gif'/>");
    messageText = messageText.replace(/&#58;innocent&#58;/g, "<img src='/img/smileys/innocent.gif'/>");
    messageText = messageText.replace(/&#58;kiss&#58;/g, "<img src='/img/smileys/kiss.gif'/>");
    messageText = messageText.replace(/&#58;laugh&#58;/g, "<img src='/img/smileys/laugh.gif'/>");
    messageText = messageText.replace(/&#58;lol&#58;/g, "<img src='/img/smileys/lol.gif'/>");
    messageText = messageText.replace(/&#58;love&#58;/g, "<img src='/img/smileys/love.gif'/>");
    messageText = messageText.replace(/&#58;mad&#58;/g, "<img src='/img/smileys/mad.gif'/>");
    messageText = messageText.replace(/&#58;mellow&#58;/g, "<img src='/img/smileys/mellow.gif'/>");
    messageText = messageText.replace(/&#58;ohmy&#58;/g, "<img src='/img/smileys/ohmy.gif'/>");
    messageText = messageText.replace(/&#58;pinch&#58;/g, "<img src='/img/smileys/pinch.gif'/>");
    messageText = messageText.replace(/&#58;rolleyes&#58;/g, "<img src='/img/smileys/rolleyes.gif'/>");
    messageText = messageText.replace(/&#58;sad&#58;/g, "<img src='/img/smileys/sad.gif'/>");
    messageText = messageText.replace(/&#58;scared&#58;/g, "<img src='/img/smileys/scared.gif'/>");
    messageText = messageText.replace(/&#58;shit&#58;/g, "<img src='/img/smileys/shit.gif'/>");
    messageText = messageText.replace(/&#58;sleep&#58;/g, "<img src='/img/smileys/sleep.gif'/>");
    messageText = messageText.replace(/&#58;sleeping&#58;/g, "<img src='/img/smileys/sleeping.gif'/>");
    messageText = messageText.replace(/&#58;smile&#58;/g, "<img src='/img/smileys/smile.gif'/>");
    messageText = messageText.replace(/&#58;sweatdrop&#58;/g, "<img src='/img/smileys/sweatdrop.gif'/>");
    messageText = messageText.replace(/&#58;thumbdown&#58;/g, "<img src='/img/smileys/thumbdown.gif'/>");
    messageText = messageText.replace(/&#58;thumbup1&#58;/g, "<img src='/img/smileys/thumbup1.gif'/>");
    messageText = messageText.replace(/&#58;tongue&#58;/g, "<img src='/img/smileys/tongue.gif'/>");
    messageText = messageText.replace(/&#58;unsure&#58;/g, "<img src='/img/smileys/unsure.gif'/>");
    messageText = messageText.replace(/&#58;w00t&#58;/g, "<img src='/img/smileys/w00t.gif'/>");
    messageText = messageText.replace(/&#58;whistling&#58;/g, "<img src='/img/smileys/whistling.gif'/>");
    messageText = messageText.replace(/&#58;wink&#58;/g, "<img src='/img/smileys/wink.gif'/>");
    messageText = messageText.replace(/&#58;wtf&#58;/g, "<img src='/img/smileys/wtf.gif'/>");
    messageText = messageText.replace(/&#58;wub&#58;/g, "<img src='/img/smileys/wub.gif'/>");
    messageText = messageText.replace(/&#58;&#41;/g, "<img title=':)' src='/img/smileys/smile.gif'/>");
    messageText = messageText.replace(/&#58;&#40;/g, "<img title=':)' src='/img/smileys/sad.gif'/>");
    messageText = messageText.replace(/&#59;&#41;/g, "<img title=':)' src='/img/smileys/wink.gif'/>");
    messageText = messageText.replace(/&#58;D/gi, "<img title=':D' src='/img/smileys/laugh.gif'/>");
    messageText = messageText.replace(/&#58;O/gi, "<img title=':O' src='/img/smileys/ohmy.gif'/>");
    messageText = messageText.replace(/&#58;S/gi, "<img title=':S' src='/img/smileys/wtf.gif'/>");
    return messageText = messageText.replace(/&#58;P/gi, "<img title=':P' src='/img/smileys/tongue.gif'/>")
  }
}

function generateUserID() {
  for(var a = "", b = 0;b < 60;b++) {
    var c = Math.floor(Math.random() * 61);
    a += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz".substring(c, c + 1)
  }
  return a
}

function u2hf (s){
  ret = '';
  for (i=0; i<s.length; i++) 
    ret += '&#x' + s.charCodeAt(i).toString(16).toUpperCase() + ';'; 
  return ret;
}

$username = generateUserID();

function playGong(){
    $('#flashGong').flash(function(){this.Play();});
    $('link[rel=icon]').attr('href','img/favicon_gong.ico');
    document.title ='KONUj: gong çaldı';
}

function playPop(){
    $('#flashPop').flash(function(){this.Play();});
}

function send(action, user, message, handler) {
    $.ajax({
        url: "/chat/",
        data: {
            action: action,
            user: user,
            message: message
        },
        type: 'POST',
        dataType: 'json',
        success: handler
    });
};

function updateOnlineUserCount() {
    $.get('/chat/count', function (count) {
        $('#counter').html('şu anda ' + count + ' konujan kişi var');
    });
}

var room = {
    join: function (name) {
        this._username = name;
        $('#start_chat_button').hide();
        changeState('connecting');
        send('join', room._username, null, function () {
            send('poll', room._username, null, room._poll);
            changeState('waiting');
        });
        isChatting = true;
    },
    chat: function (text) {
        if (text != null && text.length > 0) {
            send('chat', room._username, text);
        }
    },
    _poll: function (m) {
        if (m.chat) {
            if (m.from == "JOINED") {
                changeState('connected');
                $('#gongDugme').attr('src','img/bell.gif');
                $('#randomDugme').attr('src','img/shuffle.png');
                $('#randomDugme').removeAttr('class');
                $('#chatTextInput').removeAttr('disabled').val('');
                $('.sendThis').html('<a href="#" id="sendThisLink">Gönder<\/a>');
                $('#typeSpan').html('Bir yabanji bağlandı. Konujmaya başla hadi');
                $('link[rel=icon]').attr('href','img/favicon_online.ico');
                isStrangerConnected = true;
                document.title ='KONUj: Bir yabanjı bağlandı. Konujmaya başla hadi.';
                updateOnlineUserCount();
                konujBoxy.options.closeConfirm = true; // konujboxy connect olunca kapatmayı confirm ettir
            }
            else if (m.from == "TYPING") {
                addTypingNotification();
            }
            else if (m.from == "DISCONNECTED") {
                removeTypingNotification();
                changeState('disconnect');
                $('#typeSpan').html('Konuj\'mayı kaydetmek istiyorsan <a href="#" id="downKonuj">OK<\/a>\'e bas. Yeni Konujma için hemen <a href="#" id="konujStart">tıklayın<\/a>');
                $('#chatTextInput').val('').attr('disabled', true);
                $('.sendThis').html('<span class="disabled">Gönder<\/span>');
                $('link[rel=icon]').attr('href','img/favicon_dis.ico');
                document.title ='KONUj: Yabanji kapatti...';
                setTimeout(function () {
                    updateOnlineUserCount();
                }, 2000);
                isChatting = false;
                isStrangerConnected = false;
                konujBoxy.options.closeConfirm = false; // konujboxy disconnect edilince kapatilabilir
                return;
            }
            else {
                if (m.from == 'Yabanji') {
                    removeTypingNotification();
                }
                addRow(m.from, m.chat);
                $('#randomDugme').attr('class','hidden');
            }
        }
        if (m.action == 'poll') {
            send('poll', room._username, null, room._poll);
        }
    },
    _end: ''
};

$('#downKonuj').live('click',function(){
    $('#downloadLogs').trigger('click');
    return false;
});

$('#konujStart').live('click',function(){
    startChat();
    $('#typeSpan').empty();
    $('#chatTextInput').val('Konujacak birisi bekleniyor...')
    $('.messages').empty();
    return false;
});

function disconnect() {
    if (isChatting && confirm("Cidden konujmayı bitirejen?")) {
        sendDisconnect();
    }
}

function sendDisconnect() {
    send('end', room._username, null, function (m) {
    });
    konujBoxy.options.closeConfirm = false;
}

function startChat() {
    $('link[rel=icon]').attr('href','img/favicon_wait.ico');
    document.title ='KONUj: Konujacak birisi bekleniyor..';
    $username = generateUserID();
    $('#username').val($username);
    room.join($username);    
}

function removeTypingNotification() {
    $('#typeSpan').empty();
    isTyping = false;
}

function addTypingNotification() {
    $('#typeSpan').html('yabanji yazıyor..');
    isTyping = true;
}

function addRow(from, msg) {
    now = new Date();
    timeOfChat = now.getHours() + ":" + now.getMinutes() + ':' + now.getSeconds();
    who = from.toLowerCase();
    if(who=='yabanji'){
        playPop();
    }
    classOfLog = who == "yabanji" ? " stranger" : " you";
    if(msg != "&#95;sescal&#46;knock"){
        $('#chatwindow .messages').append('<div class="logItem' + classOfLog + '"><div class="time">' + timeOfChat + '<\/div><div class="nic">' + from + '<\/div><div class="msg">' + parsesmiley(msg) + '<\/div><\/div>').animate({
        scrollTop: $('#chatwindow .messages').attr('scrollHeight')
        }, 200);
    }else{
        playGong();
        knockMessage = who == "yabanji" ? "[*] Yabanjı ekranını tıklattı.." : "[*] Yabanjının ekranını tıklattın..";
        $('#chatwindow .messages').append('<div class="logItemKnock"><div class="time">' + timeOfChat + '<\/div><div class="msg">' + knockMessage + '<\/div><\/div>').animate({
        scrollTop: $('#chatwindow .messages').attr('scrollHeight')
        }, 200);
    }    
}

function changeState(stated) {
    state = stated;
}

function encodeHtml(a) {
  encodedHtml = escape(a);
  encodedHtml = encodedHtml.replace(/\//g, "%2F");
  encodedHtml = encodedHtml.replace(/\?/g, "%3F");
  encodedHtml = encodedHtml.replace(/=/g, "%3D");
  encodedHtml = encodedHtml.replace(/&/g, "%26");
  return encodedHtml = encodedHtml.replace(/@/g, "%40")
}

function encodeHTMLForDownload(a) {
    a = a.replace(new RegExp("%+", "gi" ),'%25');
    a = a.replace(new RegExp('"+', "gi" ),'%22');
    a = a.replace(new RegExp("'+", "gi" ),'%5C%5C%27');
    a = a.replace(new RegExp("&+", "gi" ),'%26');
    a = a.replace(new RegExp("=+", "gi" ),'%3D');
    return a;
}

// Encode HTML function
function encode(input){
  return $('<div/>').text(input).html();
}

// Decode HTML function
function decode(input){
  return $('<div/>').html(input).text();
}

$(function () {
    $.fn.fixedPosition = function (options) {
        var defaults = {
            vpos: null,
            hpos: null
        };
        var options = $.extend(defaults, options);
        return this.each(function (index) {
            var $this = $(this);
            $this.css("position", "absolute");
            if (options.vpos === "top") {
                $this.css("top", "0");
            }
            else if (options.vpos === "middle") {
                $this.css("top", ((parseInt($(window).height()) / 2) - ($(this).height() / 2)) + "px");
            }
            else if (options.vpos === "bottom") {
                $this.css("bottom", "0");
            }
            if (options.hpos === "left") {
                $this.css("left", "0");
            }
            else if (options.hpos === "center") {
                $this.css("left", ((parseInt($(window).width()) / 2) - ($(this).width() / 2)) + "px");
            }
            else if (options.hpos === "right") {
                $this.css("right", "0");
            }
            var top = parseInt($this.offset().top);
            var left = parseInt($this.offset().left);
            $(window).scroll(function () {
                $this.css("top", top + $(document).scrollTop()).css("left", left + $(document).scrollLeft());
            });
        });
    };
    $('#contact').contactable({
        subject: 'KONUj İletijim'
    });

    jQuery.download = function(url, data, method){
        if( url && data ){ 
            data = typeof data == 'string' ? data : jQuery.param(data);
            var inputs = '';
            jQuery.each(data.split('&'), function(){ 
                var pair = this.split('=');
                inputs+='<input type="hidden" id="inn'+ pair[0] +'" name="'+ u2hf(pair[0]) +'" value="" />'; 
                //jQuery('#inn'+ pair[0]).val(pair[1]);
            });
            jQuery('<form action="'+ url +'" method="'+ (method||'post') +'" target="_blank" accept-charset="UTF-8">'+inputs+'</form>')
            .appendTo('body').submit().remove();
        };
    };
    
    $('.chatText').live('keyup', function (e) {
        if (!isTyping) {
            isTyping = true;
            send('typing', room._username, null, null);
        }
        if (e.keyCode == 13 && $(this).val() != '') {
            $('#chatwindow .messages').animate({
                scrollTop: $('#chatwindow .messages').attr('scrollHeight')
            }, 200);
            room.chat($('.chatText').val());
            isTyping = false;
            $(this).val('').focus();
        }
    });
    
    $('#sendThisLink').live('click',function(){
        isTyping = false;
        send('chat', room._username, $('.chatText').val());
        $('.chatText').val('').focus();
        return false;
    });

    Boxy.DEFAULTS.title = 'KONUj';
    new Boxy(' \
    <div style="width:300px;"> \
        <p id="introTitle">Yabanjı birisiyle konuşmak için hemen konuj\'a tıklayınız.<\/p><br \/> \
        <p><a href="#chatwindow" class="boxy" id="startChat">KONUj<\/a><\/p> \
    <\/div> \
    ', {
        title: "KONUj: Konujturur..",
        closeable: false
    });
    
    // Converts a string of characters to JavaScript escapes
    // str: sequence of Unicode characters
    function convertCharStr2jEsc (str) { 
        var highsurrogate = 0;
        var suppCP;
        var pad;
        var n = 0;
        var outputString = '';
        for (var i = 0; i < str.length; i++) {
            var cc = str.charCodeAt(i); 
            if (cc < 0 || cc > 0xFFFF) {
                outputString += 'KONUj hata: beklenmedik charCodeAt durumu, cc=' + cc + '!';
            }
            if (highsurrogate != 0) { // this is a supp char, and cc contains the low surrogate
                if (0xDC00 <= cc && cc <= 0xDFFF) {
                    suppCP = 0x10000 + ((highsurrogate - 0xD800) << 10) + (cc - 0xDC00); 
    
                    suppCP -= 0x10000; 
                    outputString += '\\u'+ dec2hex4(0xD800 | (suppCP >> 10)) +'\\u'+ dec2hex4(0xDC00 | (suppCP & 0x3FF));
    
                    highsurrogate = 0;
                    continue;
                }
                else {
                    outputString += 'KONUj hata: low surrogate bekleniyor, cc=' + cc + '!';
                    highsurrogate = 0;
                }
            }
            if (0xD800 <= cc && cc <= 0xDBFF) { // start of supplementary character
                highsurrogate = cc;
            }
            else { // this is a BMP character
                //outputString += dec2hex(cc) + ' ';
                switch (cc) {
                    case 0: outputString += '\\\\0'; break;
                    case 8: outputString += '\\\\b'; break;
                    case 9: outputString += '\\\\t'; break;
                    case 10: outputString += '\\\\n'; break;
                    case 13: outputString += '\\\\r'; break;
                    case 11: outputString += '\\\\v'; break;
                    case 12: outputString += '\\\\f'; break;
                    case 34: outputString += '\\\\\"'; break;
                    case 39: outputString += '\\\\\''; break;
                    case 92: outputString += '\\\\'; break;
                    default: 
                        if (cc > 0x1f && cc < 0x7F) { outputString += String.fromCharCode(cc); }
                        else { 
                            pad = cc.toString(16).toUpperCase();
                            while (pad.length < 4) { pad = '0'+pad; }
                            outputString += '\\\\u'+pad; 
                        }
                }
            }
        }
        return outputString;
    }
    
    $('#downloadLogs').live('click', function () {
        jQuery('<form action="/chat/download" method="post" target="_blank" accept-charset="UTF-8"><input type="hidden" id="innLogs" name="logs" value="'+ convertCharStr2jEsc(encodeHTMLForDownload($('.messages').html())) +'" /></form>').appendTo('body').submit().remove();
       return false;
    });

    $('#counter').fixedPosition({
        vpos: 'top',
        hpos: 'right'
    });
    $('#about').fixedPosition({
        vpos: 'bottom',
        hpos: 'center'
    });
    $('#share').fixedPosition({
        vpos: 'middle',
        hpos: 'left'
    });
    $('#flashPop').fixedPosition({
        vpos: 'top',
        hpos: 'left'
    });
    
    $gongCaldi = false;
    $gongSaniyeNumber = 10;
    $('#gongDugme').live('click',function(){
        if(!isStrangerConnected){return;};
        if(!$gongCaldi){
            $gongCaldi = true;
            send('chat', room._username, '_sescal.knock');
            $('#gongDugme').attr('src','img/bell_disabled.gif');
            $('#typeSpan').html('<span class="gongSaniye">10<\/span>sn sonra tıklatabilirsiniz.');
            $gongSaniyeTimer = window.setInterval(gongSaniye, 1000 );            
        }

        function gongSaniye(){
            $gongSaniyeNumber = $gongSaniyeNumber - 1;
            $('#typeSpan .gongSaniye').html($gongSaniyeNumber);
            
            if($gongSaniyeNumber<1){
                $('#typeSpan').empty();
                $('#gongDugme').attr('src','img/bell.gif');
                $('#typeSpan .gongSaniye').html('10');
                $gongSaniyeNumber = 10;
                clearTimeout($gongSaniyeTimer);
                $gongCaldi = false;
                $('#flashGong .movie').empty();
            }            
        }
        return false;
    }); 
    
    $('#randomDugme').live('click',function(){
        if(!isStrangerConnected){return;};
        $('#chatTextInput').val(getRandomFirstMessage());
    });
    
    $('#startChat').click(function () {
        if(!isBoxyOpen) { // Birden fazla pencere acilmasi diye onlem
            konujBoxy = new Boxy(' \
            <div id="chatwindow" class="hidden ping"> \
                <div class="somebody fb"> \
                    <div class="somebodyHead"> \
                        <div class="somebodyActions"> \
                            <a href="#" id="downloadLogs"><img src="img/disk.png" \/><\/a> \
                        <\/div> \
                        <h3>Yabanji Birisi<\/h3> \
                    <\/div> \
                    <div class="somebodyBody"> \
                        <div class="log messages"><\/div> \
                    <\/div> \
                    <div class="somebodyMid"> \
                        <div id="typeSpan"><\/div> \
                        <div id="smiley"> \
                            <img src="img/smiley.gif" class="hidden" width="30" height="30" \/> \
                        <\/div> \
                        <div id="random"> \
                            <img src="img/shuffle_disabled.png" width="30" height="30" id="randomDugme" alt="Hadi çekinme, sana rastgele bir mesaj!" title="Hadi çekinme, sana rastgele bir mesaj!" \/> \
                        <\/div> \
                        <div class="gongCal"> \
                            <div class="gong"> \
                                <img src="img/bell_disabled.gif" width="30" height="30" id="gongDugme" alt="Gong! Gong!" title="Gong! Gong!" \/> \
                                <div id="flashGong"><div id="movieGong"><\/div><\/div> \
                            <\/div> \
                            <div class="text"><\/div> \
                        <\/div> \
                    <\/div> \
                    <div class="somebodyFooter"> \
                        <div class="chat"> \
                            <div class="text"> \
                                <input class="chatText" id="chatTextInput" disabled="disabled" value="Konujacak birisi bekleniyor..." \/> \
                            <\/div> \
                            <div class="sendThis"><span class="disabled">Gönder<\/span><\/div> \
                        <\/div> \
                    <\/div> \
                <\/div> \
            <\/div> \
            ', {
                title: "KONUj",
                closeConfirmText: "Cidden konujmayı bitirejen?",
                afterHide: function () {
                    isBoxyOpen = false;
                    if (isChatting) {
                        sendDisconnect();
                    }
                }
            });
            startChat();
            isBoxyOpen = true;
            $('#flashGong #movieGong').flash({swf: 'js/knock.swf',params: {play: false},height: 10,width: 10});
            $('#flashPop #moviePop').flash({swf: 'js/tik.swf',params: {play: false},height: 10,width: 10});
        }
        return false;
    });
    $('.chatText').val('');

});
updateOnlineUserCount();
changeState("startchat");

$('#blog').click(function () {
    new Boxy.load("blog.html", {
        title: "konuj.Blog",
        draggable: true
    });
});

$('#gizlilik').click(function () {
    new Boxy.load("gizlilik.html", {
        title: "KONUj.Com Gizlilik Sözleşmesi",
        modal: true,
        draggable: true
    });
});

defaultLang = geoip_country_code();
if(defaultLang==null || defaultLang!="TR"){
    window.location = "index_en.html";
}


$('#lang').change(function(){
    var lang= $('#lang option:selected').val();
    if(lang=='TR'){
        window.location = "index.html";
    }else{
        window.location = "index_en.html";
    }
    
});