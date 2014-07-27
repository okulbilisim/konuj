package com.konuj.servlet.admin;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.konuj.ChatServer;
import com.konuj.servlet.ChatServlet;
import com.konuj.servlet.DBConnGateway.CtlJDBCClient;

public class ChatTranscriptServlet extends HttpServlet
{
	private static Logger log = Logger.getLogger(ChatTranscriptServlet.class);
	private CtlJDBCClient m_oDBClient;
	
	public ChatTranscriptServlet() {
		
	}
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	Connection oConnection = null;
    	SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Timestamp timestamp = null;
    	try
    	{
	        response.setContentType("text/html");
	        response.setStatus(HttpServletResponse.SC_OK);
	        //response.getWriter().println(ChatServlet.members.size());        
	        PrintWriter out = response.getWriter();
	        
	        String strId = request.getParameter("id");
	        if(strId == null)
	        {
	        	out.println("Please provide 'id' to get the details.");
	        	return;
	        }
	        
	        int iId = 0;
	        
	        try
	        {
	        	iId = Integer.parseInt(strId);
	        }
	        catch(NumberFormatException nfe)
	        {
	        	out.print("Id provided is not a number.");
	        	return;
	        }
        
        
	        m_oDBClient = ChatServer.getJDBCClient();
	    	oConnection = m_oDBClient.borrowObject();
	    	String strSelectQuery = "SELECT * FROM konuj.chat_transcripts where id=" + iId;
			Statement stmt= oConnection.createStatement();
			ResultSet rs = stmt.executeQuery(strSelectQuery);
			
			boolean isFound = rs.next();
			if( ! isFound)
			{
				out.println("No record found for id[" + iId + "]");
				return;
			}
			
			out.println("<table border='1'>");
				out.println("<tr>");
					out.println("<td>ID</td>");
					out.println("<td>" + rs.getInt("id") + "</td>");
				out.println("</tr>");
				
				//User-1 Details
				out.println("<tr>");
					out.println("<td align='center' colspan='2'><b>User-1 Details</b></td>");
				out.println("</tr>");
				
					out.println("<tr>");
						out.println("<td>User-1 Cookie Id</td>");
						out.println("<td>" + rs.getString("user1_cookie_id") + "</td>");
					out.println("</tr>");
					
					out.println("<tr>");
						out.println("<td>User-1 IP Address</td>");
						out.println("<td>" + rs.getString("user1_ip") + "</td>");
					out.println("</tr>");
					
					try
					{
						timestamp = rs.getTimestamp("user1_join_time");
					}catch(Exception e){}
										
					out.println("<tr>");
						out.println("<td>User-1 Join Time</td>");
						out.println("<td>" + timestamp + "</td>");
					out.println("</tr>");
					
				
				
				
					
					
				//User-2 Details
				out.println("<tr>");
					out.println("<td align='center' colspan='2'><b>User-2 Details</b></td>");
				out.println("</tr>");
				
					
					out.println("<tr>");
						out.println("<td>User-2 Cookie Id</td>");
						out.println("<td>" + rs.getString("user2_cookie_id") + "</td>");
					out.println("</tr>");
	
					out.println("<tr>");
						out.println("<td>User-2 IP Address</td>");
						out.println("<td>" + rs.getString("user2_ip") + "</td>");
					out.println("</tr>");

					try
					{
						timestamp = rs.getTimestamp("user2_join_time");
					}catch(Exception e){}
										
					out.println("<tr>");
						out.println("<td>User-2 Join Time</td>");
						out.println("<td>" + timestamp + "</td>");
					out.println("</tr>");

					
					
					
				
				out.println("<tr>");
					out.println("<td>Transcript</td>");
					out.println("<td>" + convertTranscript(rs.getString("transcript")) + "</td>");
				out.println("</tr>");
			out.println("</table>");
			
			out.flush();
        }
        catch(Exception e)
        {
        	log.error("Exception while loading chat transcript.", e);
        }
        finally
		{
			try{
				CtlJDBCClient.close(oConnection);
				}catch(Exception e) { log.error("Exception while releasing mysql connection object.", e); }	
		}
    }
    
    private String convertTranscript(String strTranscript)
    {
    	//StringBuilder sb = new StringBuilder();
    	
    	strTranscript = strTranscript.replace("\n", "<br>");
    	
    	//return sb.toString();
    	return strTranscript;
    }
}