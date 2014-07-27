var isTyping = false;
var isChatting = false;
var isStrangerConnected = false;
var isBoxyOpen = false;
var state = 'startchat';
var konujBoxy;

var firstMessages = [
    "Is there any concert nowadays?",
    "Ohh, I have to break. Let's chat with me", 
    "Do you know any cinema film begin to play?", 
    "Ýf you have supernatural power, what it should be? Invisibility?", 
    "Do you know anywhere to rent a bike ?", 
    "Hey, did you watch the match at last night?", 
    "I'm not a hunter, just wanna talk with you.(and maybe follow you until home.).", 
    "Hey, how much time have you been chatting on here?", 
    "I look at something to do at the weekend, any idea ?",
    "Help me !, I'm tooooo bored.. !",
    "Do you know a good night club for tonight?", 
    "Do you know a good Italian restaurant?",
    "I'm a secret agent. Can you keep the secrets ?", 
    "Hey, do you have any suggestion for holiday?", 
    "Do you know a place for live music in somewhere?", 
    "I'm gonna go to Ukrain for 2012 Euro Cup, do u wanna join me ?", 
    "Hey, do you know any music festival soon?", 
    "I think that we have many common points, do you agree ?", 
    "Do you wanna date with me ?", 
    "Do you wanna talk about photos?", 
    "Do you like tea or coffee, i heard that it says many things about personality.", 
    "Can you help me on the conquering the world?", 
    "Skydiving? Let's go !", 
    "I really need to breake, so let's chat !", 
    "Did you see my last photo albume?", 
    "Have you never get experience on the scenes?", 
    "If you have only an option, which place do you choose for travel?", 
    "How many meters away, i can throw my computer from window?", 
    "Mac or PC?", 
    "Do you accept, if weekend will be 3 days?", 
    "A metropol or a small village?", 
    "Why always people take my post-its in office?", 
    "Do you know where is the next olympic games, London?", 
    "Where is the Red Square?", 
    "Have you never flight on business class?", 
    "What do you think about vegetarians?", 
    "Swimming or running? ", 
    "Was it too late to wish happy years ?", 
    "Poll: What's your favorite film?", 
    "Poll: What's your favorite music band?", 
    "Pol: What's your favorite football team?"
];

function getRandomFirstMessage(){
    return firstMessages[Math.round(Math.random() * (firstMessages.length - 1))];
}

window.onbeforeunload= function (evt) {
    if(isChatting)
        return "Are you serious to finijj?";   
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
    document.title ='KONUj: ding..dong';
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
        $('#counter').html('Now ' + count + ' people are konujjing.');
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
                $('.sendThis').html('<a href="#" id="sendThisLink">Send<\/a>');
                $('#typeSpan').html('A stranJer connected, Let\'s talk!');
                $('link[rel=icon]').attr('href','img/favicon_online.ico');
                isStrangerConnected = true;
                document.title ='KONUj: A stranJer connected. Let\'s talk!.';
                updateOnlineUserCount();
                konujBoxy.options.closeConfirm = true; // konujboxy connect olunca kapatmayÄ± confirm ettir
            }
            else if (m.from == "TYPING") {
                addTypingNotification();
            }
            else if (m.from == "DISCONNECTED") {
                removeTypingNotification();
                changeState('disconnect');
                $('#typeSpan').html('To save conservation click to<a href="#" id="downKonuj">OK<\/a>\here. To start new conservation <a href="#" id="konujStart">click<\/a>');
                $('#chatTextInput').val('').attr('disabled', true);
                $('.sendThis').html('<span class="disabled">Send<\/span>');
                $('link[rel=icon]').attr('href','img/favicon_dis.ico');
                document.title ='KONUj: the stranJer disconnected';
                setTimeout(function () {
                    updateOnlineUserCount();
                }, 2000);
                isChatting = false;
                isStrangerConnected = false;
                konujBoxy.options.closeConfirm = false; // konujboxy disconnect edilince kapatilabilir
                return;
            }
            else {
                if (m.from == 'StranJer') {
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
    $('#chatTextInput').val('Find someone to konuJ')
    $('.messages').empty();
    return false;
});

function disconnect() {
    if (isChatting && confirm("Are you really serious to finijj")) {
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
    document.title ='KONUj: Waiting someone to konuJ';
    $username = generateUserID();
    $('#username').val($username);
    room.join($username);    
}

function removeTypingNotification() {
    $('#typeSpan').empty();
    isTyping = false;
}

function addTypingNotification() {
    $('#typeSpan').html('stranJer is writing');
    isTyping = true;
}

function addRow(from, msg) {
    now = new Date();
    timeOfChat = now.getHours() + ":" + now.getMinutes() + ':' + now.getSeconds();
    who = from.toLowerCase();
    if(who=='stranJer'){
        playPop();
    }
    classOfLog = who == "yabanji" ? " stranger" : " you";
    if(msg != "&#95;sescal&#46;knock"){
        $('#chatwindow .messages').append('<div class="logItem' + classOfLog + '"><div class="time">' + timeOfChat + '<\/div><div class="nic">' + from + '<\/div><div class="msg">' + parsesmiley(msg) + '<\/div><\/div>').animate({
        scrollTop: $('#chatwindow .messages').attr('scrollHeight')
        }, 200);
    }else{
        playGong();
        knockMessage = who == "StranJer" ? "[*] knocked your screen" : "[*] You knocked stranJer's screen";
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
        subject: 'KONUj Contact'
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
        <p id="introTitle">Just click to chat a stranJer !.<\/p><br \/> \
        <p><a href="#chatwindow" class="boxy" id="startChat">KONUj<\/a><\/p> \
    <\/div> \
    ', {
        title: "KONUj: Let's KonuJ !",
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
                outputString += 'KONUj error: beklenmedik charCodeAt durumu, cc=' + cc + '!';
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
            $('#typeSpan').html('<span class="gongSaniye">10<\/span>later you can knock');
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
                        <h3>The StranJer<\/h3> \
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
                            <img src="img/shuffle_disabled.png" width="30" height="30" id="randomDugme" alt="It\'s a small gift from us, random message :)" title="It\'s a small gift from us, random message :)"  \/> \
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
                                <input class="chatText" id="chatTextInput" disabled="disabled" value="Are you serious to finijj ?..." \/> \
                            <\/div> \
                            <div class="sendThis"><span class="disabled">Send<\/span><\/div> \
                        <\/div> \
                    <\/div> \
                <\/div> \
            <\/div> \
            ', {
                title: "KONUj",
                closeConfirmText: "Are you serious to finijj?",
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
        title: "KONUj.Com Agreements and Rights",
        modal: true,
        draggable: true
    });
});

defaultLang = "TR";
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