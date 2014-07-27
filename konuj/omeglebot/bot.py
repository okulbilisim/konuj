#!/usr/bin/python
import os
import sys
import urllib
import random
import string
import logging
import httplib
import ConfigParser
import simplejson as json

from threading import Thread, Event, Timer as DTimer


def start_thread():
    start = Bot().konuj_start
    thr = Thread(target=start)
    thr.start()

class Bot:
    
    announce_use = False
    announce_delay = 20
    announce_msg = 'Hello'
    
    headers = {
        "Content-type": "application/x-www-form-urlencoded; charset=utf-8",
        "Accept": "application/json",
    }    

    def __init__(self):
    
        self.konuj_host = 'konuj.com'
        self.omegle_host = 'omegle.com'    
        
        self.konuj_poll_interval = 0.5
        self.omegle_poll_interval = 0.5
        
        self.konuj_name = None
        self.omegle_name = None    
        
        self.konuj_connected = False
        self.konuj_confirmed = False
        
        self.omegle_connected = False
        self.omgele_confirmed = False
        
        self.announce_use = Bot.announce_use
        self.announce_delay = Bot.announce_delay
        self.announce_msg = Bot.announce_msg
        
        self.logger = logging.getLogger(self.__class__.__name__)
        

    def announce(self):
                
        if self.konuj_connected and self.konuj_confirmed:
            self.omegle_send(self.announce_msg)


    def request(self, host, method, path, params):

        headers = Bot.headers.copy()
        headers['Host'] = host
        headers['Content-Length'] = str(len(params))
        try:
            conn = httplib.HTTPConnection(host)
            conn.request(method, path, params, headers)
            r = conn.getresponse()
            body = r.read()
        except BaseException, e:
            print "Except: %s" % e
        else:
            return body
        finally:
            conn.close()
            

    def utf_encoder(self, msg):
        
        try:
            msg = msg.encode("utf8")
        except UnicodeEncodeError,e:
            self.logger.critical(e)
            
        return msg

            
    def create_userid(self):
        return ''.join(random.choice(string.letters + string.digits) for i in range(59))
    
    
    def konuj_start(self):
    
        self.logger.info("konuj_start()")
        
        if not self.konuj_connected:
            self.konuj_name = self.create_userid()
            params = urllib.urlencode({'action': 'join', 'message': 'null', 'user': self.konuj_name})
            resp = self.request("konuj.com", "POST", "/chat/", params)
            try:
                js = json.loads(resp)
            except:
                self.logger.error("konuj_start(): Error by JSON load: %s" % resp)
            else:            
                if js['action'] == 'join':
                    self.logger.info("konuj_start(): Konuj connected id=%s" % self.konuj_name)
                    self.konuj_connected = True
                    self.konuj_timer = Timer(self._konuj_events, self.konuj_poll_interval)
                    self.konuj_timer.start()
                else:
                    self.logger.error("konuj_start(): Bad response: %s" % resp)
        else:
            self.logger.error("konuj_start(): Already connected")
            
            
    def omegle_start(self):
    
        self.logger.info("omegle_start()")
        
        if not self.omegle_connected:
            resp = self.request("omegle.com", "POST", "/start", {})
            id = resp.split("\"")
            
            if id.__len__() == 3:
                self.omegle_name = id[1]
                self.logger.info("omegle_start(): Omegle connected id=%s" % self.omegle_name)
                self.omegle_connected = True
                self.omegle_timer = Timer(self._omegle_events, self.omegle_poll_interval)
                self.omegle_timer.start()
            else:
                self.logger.error("omegle_start(): Bad response: %s" % resp)
        else:
            self.logger.error("omegle_start(): Already connected")
            
            
    def konuj_disconnect(self):
    
        self.logger.info("konuj_disconnect()")
        self.konuj_confirmed = False
    
        if self.konuj_connected:
            params = urllib.urlencode({'action': 'end', 'message': 'null', 'user': self.konuj_name})
            resp = self.request("konuj.com", "POST", "/chat/", params)
            if True:
                self.konuj_name = None        
                self.konuj_connected = False                
            else:
                self.logger.error("konuj_disconnect(): Bad response: %s" % resp)
    
    
    def omegle_disconnect(self):
    
        self.logger.info("omegle_disconnect()")    
        self.omegle_confirmed = False
        
        if self.omegle_connected:
            params = urllib.urlencode({'id': self.omegle_name})
            resp = self.request("omegle.com", "POST", "/disconnect", params)
            if resp == "win":
                self.omegle_name = None
                self.omegle_connected = False        
            else:
                self.logger.error("omegle_disconnect(): Bad response: %s" % resp)            
        
      
    def _konuj_events(self):
        
        params = urllib.urlencode({'action' : 'poll', 'message': 'null', 'user': self.konuj_name})
        headers = Bot.headers.copy()
        headers['Content-Length'] = str(len(params))
        try:
            conn = httplib.HTTPConnection(self.konuj_host)    
            conn.request("POST", "/chat/", params, Bot.headers)
            r = conn.getresponse()
            body = r.read()
        except:
            pass
        else:
            self._konuj_dispatch_event(conn,body)
        finally:
            conn.close()    


    def _omegle_events(self):

        params = urllib.urlencode({'id' : self.omegle_name})
        headers = Bot.headers.copy()
        headers['Host'] = "omegle.com"
        headers['Content-Length'] = str(len(params))
        try:
            conn = httplib.HTTPConnection(self.omegle_host)
            conn.request("POST", "/events", params, Bot.headers)
            r = conn.getresponse()
            body = r.read()
        except:
            pass
        else:
            self._omegle_dispatch_event(conn, body)
        finally:
            conn.close()
            
            
    def konuj_send(self, msg):
    
        self.logger.info("konuj_send()")
        
        if self.konuj_confirmed:
            msg = self.utf_encoder(msg)
            params = urllib.urlencode({'action': 'chat','message':msg, 'user': self.konuj_name})            
            resp = self.request("konuj.com", "POST", "/chat/", params)
            try:
                js = json.loads(resp)
            except:
                self.logger.error("konuj_send(): Error by JSON load: %s" % resp)
            else:  
                if js['action'] != "chat":
                    self.logger.error("konuj_send(): Bad response %s" % resp)
        else:
            self.logger.error("konuj_send(): Can't send message if not confirmed")

    
    
    def omegle_send(self, msg):
    
        self.logger.info("omegle_send()")
    
        if self.omegle_confirmed:
            msg = self.utf_encoder(msg)
            params = urllib.urlencode({'id':self.omegle_name,'msg':msg})    
            resp = self.request("omegle.com", "POST", "/send", params)
            if resp != "win":
                self.logger.error("omegle_send(): Bad response %s" % resp)
        else:
            self.logger.error("omegle_send(): Can't send message if not confirmed")
            
            
    def konuj_typing(self):
    
        self.logger.info("konuj_typing()")
        
        if self.konuj_confirmed:
            params = urllib.urlencode({'action': 'typing', 'message': 'null', 'user': self.konuj_name})
            resp = self.request("konuj.com", "POST", "/chat/", params)
            if resp:
                self.logger.error("konuj_typing(); Bad response %s" % resp)
        else:
            self.logger.error("konuj_typing(): Can't typing if not confirmed")
        
    
    def omegle_typing(self):
    
        self.logger.info("omegle_typing()")
    
        if self.omegle_confirmed:
            params = urllib.urlencode({'id':self.omegle_name})    
            resp = self.request("omegle.com", "POST", "/typing", params)
            if resp != "win":
                self.logger.error("omegle_typing(); Bad response %s" % resp)
        else:
            self.logger.error("omegle_typing(): Can't typing if not confirmed")                                                                   


    def _konuj_dispatch_event(self, conn, body):
        
        try:
            data = json.loads(body.decode("utf8"))
        except:
            self.logger.error("_konuj_dispatch_event(): Error on load json. Body: %s" % body)
        else:
            if len(data) == 3:
                try:
                    action = data['from']
                except KeyError:
                    self.logger.error("_konuj_dispatch_event(): Missed JSON fromat. Body: %s" % body)
                else:
                    if action == 'JOINED':
                        self.logger.info("_konuj_dispatch_event(): Received JOINED")
                        self.konuj_confirmed = True
                        self.omegle_start()
                        start_thread()
                    elif action == 'TYPING':
                        self.logger.info("_konuj_dispatch_event(): Received TYPING")
                        self.omegle_typing()
                    elif action == 'DISCONNECTED':
                        self.logger.info("_konuj_dispatch_event(): Received DISCONNECTED")
                        self.konuj_confirmed = False
                        self.konuj_connected = False
                        self.konuj_timer.stop()
                        self.disconnect()
                    elif action == 'Yabanji':
                        self.logger.info("_konuj_dispatch_event(): Received MSG")
                        self.omegle_send(data['chat'])
                        
                        
    def _omegle_dispatch_event(self, conn, body):
        
        try:
            data_set = json.loads(body.decode("utf8"))
            if not data_set:
                return
            for data in data_set:
                if not self.omegle_connected:
                    break
                if data[0] == "typing":
                    self.logger.info("_omegle_dispatch_event(): Received TYPING")
                    self.konuj_typing()
                elif data[0] == "gotMessage" and data.__len__() == 2:
                    self.logger.info("_omegle_dispatch_event(): Received MSG")
                    self.konuj_send(data[1])
                elif data[0] == "connected":
                    self.logger.info("_omegle_dispatch_event(): Received CONNECTED")
                    self.omegle_confirmed = True
                    if self.announce_use:
                        t = DTimer(self.announce_delay, self.announce)
                        t.start()
                elif data[0] == "strangerDisconnected":
                    self.logger.info("_omegle_dispatch_event(): Received DISCONNECTED")
                    self.omegle_confirmed = False
                    self.omegle_connected = False
                    self.omegle_timer.stop()
                    self.disconnect()
                elif data[0] == "stoppedTyping":
                    self.logger.info("_omegle_dispatch_event(): Received STOPPED_TYPING")
                elif data[0] == "waiting":
                    self.logger.info("_omegle_dispatch_event(): Received WAITING")
                else:
                    self.logger.error("_omegle_dispatch_event(): Unknown JSON Data: %s" % body)
        except ValueError:
            self.logger.error("_omegle_dispatch_event(): Missed JSON format. Body: %s" % body)
            
            
    def disconnect(self):
    
        self.logger.info("disconnect()")
        
        if self.omegle_connected:
            self.omegle_disconnect()
            
        if self.konuj_connected:
            self.konuj_disconnect()
            
            
class Timer(Thread):

    def __init__(self, func, interval=0.50):
        
        Thread.__init__(self)
        self.logger = logging.getLogger(__name__ + "." + self.__class__.__name__)
        self.func = func
        self.interval = interval

        self.running = False

        self.event = Event()

    def run(self):
        
        self.running = True
        while self.running:
            self.func()
            self.event.wait(self.interval)
            self.event.clear()
        self.logger.debug("self.running == False")                                               
                

    def stop(self):

        self.running = False
        self.event.set()
        
        
    
if __name__ == "__main__":
    
    def exit():
        os._exit(os.EX_OK)
    
    logging.basicConfig(level=logging.INFO)
    logger = logging.getLogger()
    try:
        cfg = ConfigParser.ConfigParser()
        cfg.read('bot.cfg')
        announce_use =  cfg.getboolean('announce', 'use')
        announce_delay = cfg.getint('announce', 'delay')
        announce_msg = cfg.get('announce', 'message')
    except Exception, e:
        logger.error("Announce disabled: %s" % e)
    else:
        Bot.announce_use = announce_use
        Bot.announce_delay = announce_delay
        Bot.announce_msg = announce_msg
        

    start_thread()
    
    while True:
        try:
            cmd = sys.stdin.readline().strip()
        except KeyboardInterrupt:
            exit()
        else:
            if cmd == "quit":
                exit()
            
    
