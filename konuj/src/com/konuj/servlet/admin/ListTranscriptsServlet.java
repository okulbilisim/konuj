package com.konuj.servlet.admin;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Vector;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.konuj.ChatServer;
import com.konuj.servlet.DBConnGateway.CtlJDBCClient;
import com.konuj.utils.HtmlUtils;


public class ListTranscriptsServlet extends HttpServlet
{
	private static Logger log = Logger.getLogger(ListTranscriptsServlet.class);
	
	
	private CtlJDBCClient m_oDBClient;
	
	public ListTranscriptsServlet() {
		
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException    , IOException
    {
    	Connection oConnection = null;
    	
    	try
    	{
    		Timestamp timestamp1 = null;
    		Timestamp timestamp2 = null;
    		
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        //response.getWriter().println(ChatServlet.members.size());
	        SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        PrintWriter out = response.getWriter();
        
        
        	
        	m_oDBClient = ChatServer.getJDBCClient();
        	oConnection = m_oDBClient.borrowObject();
        	String strSelectQuery = "SELECT id,user1_ip, user2_ip, user1_join_time, user2_join_time FROM konuj.chat_transcripts";
			Statement stmt= oConnection.createStatement();
			ResultSet rs = stmt.executeQuery(strSelectQuery);
			
			HtmlUtils hu = new HtmlUtils();
			
			out.println("<HTML><HEAD><TITLE>Chat Transcripts</TITLE>" +
					"<script type=\"text/javascript\" src=\"/js/common.js\"></script>" +
							"</HEAD><BODY>");
			out.println("<TABLE width='100%' border='1'>");
			
			out.println("<TH align='center'>Id</TH>");
			out.println("<TH align='center'>User-1 IP</TH>");
			out.println("<TH align='center'>User-2 IP</TH>");
			out.println("<TH align='center'>User-1 Join Time</TH>");
			out.println("<TH align='center'>User-2 Join Time</TH>");
			
		    
			
			while(rs.next())
			{
				
				int id = rs.getInt("id");
				String user1_ip = rs.getString("user1_ip");
				String user2_ip = rs.getString("user2_ip");
				
				//Datetimes with all-zero components (0000-00-00 ...) throws an exception by default when these values are encountered
				try
				{
					timestamp1 = rs.getTimestamp("user1_join_time");					
				}
				catch(Exception e){	}
				try
				{					
					timestamp2 = rs.getTimestamp("user2_join_time");
				}
				catch(Exception e){	}
				
			
				out.println("<tr onmouseover=\"ChangeColor(this, true);\" onmouseout=\"ChangeColor(this, false);\" onclick=\"DoNav('/admin/detail?id=" + id + "')\">");
					out.println("<td>" + id + "</td>");
					out.println("<td>" + user1_ip + "</td>");
					out.println("<td>" + user2_ip + "</td>");
					out.println("<td>" + timestamp1 + "</td>");
					out.println("<td>" + timestamp2 + "</td>");
				out.println("</tr>");
									
			}
					
					
			

			out.println("</TABLE>");
			out.println("</BODY>");
			out.println("</HTML>");
			
		    
			out.flush();
        }
        catch(Exception e)
        {
        	log.error("Exception while creating listTranscript page.", e);
        }
        finally
		{
			try{
				CtlJDBCClient.close(oConnection);
				}catch(Exception e) { log.error("Exception while releasing mysql connection object.", e); }	
		}
    }
}