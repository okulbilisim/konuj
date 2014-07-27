package com.konuj.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import com.konuj.utils.StringUtils;
import java.io.*;

import org.apache.log4j.Logger;

import com.konuj.utils.properties.KonujProperties;

public class DownloadServlet extends HttpServlet
{
	private static Logger log = Logger.getLogger(DownloadServlet.class);
	
	public DownloadServlet() {
		
	}
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try{
    		request.setCharacterEncoding("UTF-8");
    		String fileName = StringUtils.createAlphaNumericFileName(KonujProperties.iDOWNLOAD_FilenameLength);
    		while(true){
    			if(!(new File(KonujProperties.strDOWNLOAD_FilePath + fileName)).exists()){
    				break;
    			}
    			fileName = StringUtils.createAlphaNumericFileName(KonujProperties.iDOWNLOAD_FilenameLength);
    			
    		}
    		//log.debug("================== DOWNLOAD SERVLET =================");
    		String logs = request.getParameter("logs");
    		//log.debug("\n\nrequest logs parameter clean:" + logs);
    		
    		// Log html uret
    		String html ="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"><head><title>KONUj.Com: Konujma Gecmisi</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><meta property=\"og:title\" content=\"KONUj: Konujma Gecmisi..\" /> <meta property=\"og:description\" content=\"KONUj: Ben de konujdum, hadi okusana : Lög Kaydı :)\" /><meta property=\"og:image\" content=\"http://konuj.com/img/facebook_share.png\" /><link rel=\"image_src\" type=\"image/jpeg\" href=\"http://konuj.com/img/facebook_share.png\" /><style type=\"text/css\">body{margin:0;padding:0;background:#EEE;height:100%;font-family:\"lucida grande\",tahoma,verdana,arial,sans-serif;position:relative;}#main{background:#eee;margin:0 auto;width:400px;display:block;}.logItem,.logItemKnock{display:block;border-bottom:1px solid #ccc;margin-bottom:4px;padding:5px;line-height:14px;font-size:14px;}.logItem .nic{display:block;font-weight:bold;color:#405d9f;}.logItem .time,.logItemKnock .time{display:block;font-weight:bold;color:#333;float:right;margin:5px;font-size:10px;}.logItem .msg,.logItemKnock .msg{display:block;color:#000;}.boxy-wrapper{position:absolute;empty-cells:show;}.boxy-wrapper.fixed{}.boxy-modal-blackout{position:absolute;background-color:black;left:0;top:0;}.boxy-wrapper .boxy-top-left,.boxy-wrapper .boxy-top-right,.boxy-wrapper .boxy-bottom-right,.boxy-wrapper .boxy-bottom-left{width:10px;height:10px;padding:0;}.boxy-wrapper .boxy-top-left{background:url('http://konuj.cdnturkiye.com/logs/1/img/boxy-nw.png');}.boxy-wrapper .boxy-top-right{background:url('http://konuj.cdnturkiye.com/logs/1/img/boxy-ne.png');}.boxy-wrapper .boxy-bottom-right{background:url('http://konuj.cdnturkiye.com/logs/1/img/boxy-se.png');}.boxy-wrapper .boxy-bottom-left{background:url('http://konuj.cdnturkiye.com/logs/1/img/boxy-sw.png');}.boxy-wrapper .boxy-top,.boxy-wrapper .boxy-bottom{height:10px;background-color:black;opacity:.6;filter:alpha(opacity=60);padding:0;}.boxy-wrapper .boxy-left,.boxy-wrapper .boxy-right{width:10px;background-color:black;opacity:.6;filter:alpha(opacity=60);padding:0;}.boxy-wrapper .title-bar{background-color:black;padding:6px;position:relative;cursor:move;}.boxy-wrapper .title-bar.dragging{cursor:move;}.boxy-wrapper .title-bar h2{font-size:12px;color:white;line-height:1;margin:0;padding:0;font-weight:normal;}.boxy-wrapper .title-bar .close{color:white;position:absolute;top:6px;right:6px;font-size:90%;line-height:1;}.boxy-inner{background-color:white;padding:0;}.boxy-content{padding:15px;}.boxy-wrapper .title-bar{background-color:#3b5999;}.fb .somebodyHead{background-color:#ececec;}.fb .chatText{color:#777;border:1px solid #BDC7D8;font-family:\"lucida grande\",tahoma,verdana,arial,sans-serif;padding:3px;}.fb a,.fb a:hover{color:#3B5998;}.fb .logItem{border-bottom-color:#efefef;}.fb .logItem .nic{color:#405d9f;}.fb .you .nic{color:#333;}.fb .logItem .msg{color:#ccc;}a img{border:none;}</style></head><body><div id=\"ads-head\"></div><div id=\"share\" style=\"position: absolute; top: 100px; left: 0px; \"> <a title=\"Konuj: Ben de konujdum :) Hadi paylaş\" href=\"http://www.facebook.com/share.php?u=<===LINK===>&amp;title=KONUj+%3A+Ben+de+konujdum,+hadi+okusana+%3A+Log+Kaydı+:)\" target=\"_blank\"><img src=\"http://konuj.cdnturkiye.com/logs/1/img/facebook.gif\" alt=\"facebook\" title=\"facebook\"></a><br> <a title=\"Konuj: Ben de konujdum :) Hadi paylaş\" href=\"http://friendfeed.com/?url=<===LINK===>&amp;title=KONUj+%3A+Ben+de+konujdum,+hadi+okusana+%3A+Log+Kaydı+:)\" target=\"_blank\"><img src=\"http://konuj.cdnturkiye.com/logs/1/img/friendfeed.gif\" alt=\"friendfeed\" title=\"friendfeed\"></a><br> <a title=\"Konuj: Ben de konujdum :) Hadi paylaş\" href=\"http://twitter.com/home?status=KONUj+%3A+Ben+de+konujdum,+hadi+okusana+%3A+Log+Kaydı+:)+%3A+<===LINK===>\" target=\"_blank\"><img src=\"http://konuj.cdnturkiye.com/logs/1/img/twitter.gif\" alt=\"twitter\" title=\"twitter\"></a><br><a title=\"Konuj: Ben de konujdum :) Hadi paylaş\" href=\"http://delicious.com/save?jump=close&amp;v=4&amp;partner=[partner]&amp;noui&amp;url=<===LINK===>&amp;title=KONUj+%3A+Ben+de+konujdum,+hadi+okusana+%3A+Log+Kaydı+:)\" target=\"_blank\"><img src=\"http://konuj.cdnturkiye.com/logs/1/img/delicious.gif\" alt=\"delicious\" title=\"delicious\"></a><br><a title=\"Konuj: Ben de konujdum :) Hadi paylaş\" href=\"http://www.digg.com/submit?phase=2&amp;url=<===LINK===>&amp;tile=KONUj+%3A+Ben+de+konujdum,+hadi+okusana+%3A+Log+Kaydı+:)\" target=\"_blank\"><img src=\"http://konuj.cdnturkiye.com/logs/1/img/digg.gif\" alt=\"digg\" title=\"digg\"></a></div><div style=\"display:block; width:600px; margin:30px auto;\"><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"boxy-wrapper fixed\"><tbody><tr><td class=\"boxy-top-left\"></td><td class=\"boxy-top\"></td><td class=\"boxy-top-right\"></td></tr><tr><td class=\"boxy-left\"></td><td class=\"boxy-inner\"><div class=\"title-bar\"><h2>KONUj.Com Log Kaydı</h2></div><div style=\"width: 550px;display: block; margin:0 auto;overflow-x:hidden; overflow:auto; height:600px; \" class=\"boxy-content\"><===LOG===></div></td><td class=\"boxy-right\"></td></tr><tr><td class=\"boxy-bottom-left\"></td><td class=\"boxy-bottom\"></td><td class=\"boxy-bottom-right\"></td></tr></tbody></table></div><div id=\"ads-foot\"></div></body></html>";
			html = html.replaceAll("<===LINK===>", KonujProperties.strDOWNLOAD_URLPath + fileName); // Linki replace et
			logs = java.net.URLDecoder.decode(logs);
			//log.debug("\n\nrequest logs parameter after decoded:" + logs + "\n\n");
			logs = logs.replaceAll("'", "\\'");
			//log.debug("\n\nrequest logs parameter after \' replaced as \\\':" + logs + "\n\n");

			html = html.replaceAll("<===LOG===>", "<script>document.write('" + logs + "');</script>"); // Loglari replace et
			html = html.replaceAll("/img/smileys/", KonujProperties.strDOWNLOAD_CDNLocation); // smileylerin pathini replace et
    		
    		// Log html kaydet
    		Writer out = new OutputStreamWriter(new FileOutputStream(KonujProperties.strDOWNLOAD_FilePath + fileName), KonujProperties.strDOWNLOAD_FileEncoding);
		    try {
		      out.write(html);
		    }
		    finally {
		      out.close();
		    }
			
	        response.setContentType("application/octet-stream");
			response.setContentLength( (int)html.length() );
	        response.setHeader( "Content-Disposition", "attachment; filename=" + fileName);
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().print(html);
	        response.getWriter().flush();
	        response.getWriter().close();
	    }
	    catch(Exception ex){
	    	log.error("Download file error Request:", ex);
	    	log.error(request);
	    	ex.printStackTrace();
	    	String result = "konujma erörü";
	    	response.setContentType("application/octet-stream");
			response.setContentLength( (int)result.length() );
	        response.setHeader( "Content-Disposition", "attachment; filename=konuj.html" );
	        response.setStatus(HttpServletResponse.SC_OK);
	        response.getWriter().print(result);
	        response.getWriter().flush();
	        response.getWriter().close();
	    }
    }
}