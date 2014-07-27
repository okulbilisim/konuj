package com.konuj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;


import com.konuj.ChatServer;
import com.konuj.objects.CleanUpTask;
import com.konuj.objects.Member;
import com.konuj.utils.StringUtils;
import com.konuj.utils.properties.KonujProperties;



public class ChatServlet extends HttpServlet
{
	private static Logger log = Logger.getLogger(ChatServlet.class);
	public static final int SECONDS_PER_YEAR = 60*60*24*365;
	
	private static Timer g_Timer = new Timer();
	public static ConcurrentHashMap<String, String> userMapper = new ConcurrentHashMap<String, String>();
    //private static String strFirstUser = "";
    private static Queue<String> queMemberWaitingToChat = new LinkedList<String>();
    
    public static ConcurrentHashMap<String, Member> members = new ConcurrentHashMap<String, Member>();
    
    static
    {
    	log.trace("Going to start Monitor Thread.");
    	startMonitorThread();
    }
    


    //Map<String,Map<String,Member>> _rooms = new HashMap<String,Map<String, Member>>();
    
    
    // Handle Ajax calls from browser
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {   
        // Ajax calls are form encoded
        String action = request.getParameter("action");
        String message = request.getParameter("message");
        String username = request.getParameter("user");

        updateLastActivity(username);
        
        if (action.equals("join"))
            join(request,response,username);
        else if (action.equals("poll"))
            poll(request,response,username);
        else if (action.equals("chat"))
            chat(request,response,username,message);
        else if (action.equals("end"))
        	end(request,response,username);
        else if (action.equals("typing"))
        	typing(request,response,username);
    }
    
    private void updateLastActivity(String username)
    {
    	if(username != null)
    	{
    		Member member = members.get(username);
    		if(member != null)
    			member.setLastActivityTime(System.currentTimeMillis());
    		
    	}
    }
    
    private static void startMonitorThread()
    {
    	g_Timer.schedule(new TimerTask() {
			
			public void run() {
				log.trace("Running Monitor Thread");
				try
				{
					Iterator<Entry<String, Member>> itr = members.entrySet().iterator();
					while(itr.hasNext())
					{
						Entry<String, Member> entry = itr.next();
						String firstUsername = entry.getKey();
						Member firstMember = entry.getValue();
						
						long lSessIdleTime = firstMember.getIdleTime();
						if (lSessIdleTime >= (long) 1000 * KonujProperties.SessionIdleTimeInSec)
						{
							log.debug("Session TimedOut for user [" + firstUsername + "]");
							firstMember.shouldDisconnect = true;
							firstMember.isSessionTimedout = true;
							sendMsgToMember(firstMember, "DISCONNECTED", "end");
							
							// find second member if any
							String secondUser = userMapper.get(firstUsername);
							Member secondMember = null;
							if(secondUser != null)
								secondMember = members.get(secondUser);
							if(secondMember != null)
							{
								secondMember.shouldDisconnect = true;
								secondMember.isSessionTimedout = true;
								sendMsgToMember(secondMember, "DISCONNECTED", "end");
								log.debug("HIS friend user[" + secondUser + "] is also disconnected.");
							}
							
							//In case if the user is not connected to the server, then we should clean up his session forcefully after 2 seconds
							TimerTask task = new CleanUpTask(firstUsername, secondUser);
							g_Timer.schedule(task, 2000);
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				log.trace("Done monitor thread");
			}
		}, KonujProperties.MonitorThreadDelayInSec * 1000, KonujProperties.MonitorThreadDelayInSec * 1000);
    	
    	log.info("Monitor Thread started.");
    }

    private void typing(HttpServletRequest request,HttpServletResponse response,String username)
    {
    	String strOtherMemberId = userMapper.get(username);
    	if(strOtherMemberId != null)
    	{
	    	Member otherMember = members.get(strOtherMemberId);
	    	if(otherMember != null)
	    	{
	    		//Send the typing notification
	    		sendMsgToMember(otherMember, "TYPING", "typing");
	    	}
    	}
    }
    
    private void end(HttpServletRequest request,HttpServletResponse response,String username)
    {
    	Member firstMember = members.get(username);
    	Member secondMember = null;
    	
    	String secondUserId = userMapper.get(username);
    	if(secondUserId != null)
    	{
    		secondMember = members.get(secondUserId);
    		sendMsgToMember(secondMember, "DISCONNECTED", "end");
    		secondMember.shouldDisconnect = true;
    		//secondMember._continuation.complete();
    	}
    	else
    	{
    		//This user was not connect to any other user
    		synchronized (queMemberWaitingToChat) 
    		{
    			queMemberWaitingToChat.remove(username);
    			log.debug("removing username [" + username + "]");
    		}
    	}
    	
    	if(firstMember != null)
    	{
	    	sendMsgToMember(firstMember, "DISCONNECTED", "end");
	    	firstMember.shouldDisconnect = true;
    	}
    	//firstMember._continuation.complete();
    	//cleanMappingsAndDisconnect(username, secondUserId);
    	
    	//Write the ChatTranscript to DB
    	ChatServer.writeFullChatTranscriptToDB(firstMember, secondMember);
    	
    }
    
    private void cleanMappingsAndDisconnect(String username)
    {
    	if(username != null)
    	{
    		userMapper.remove(username);
    		Member m = members.remove(username);
    		if(m != null)
    		{
    			if(m._continuation != null)
    				m._continuation.complete();
    		}
    	}    	
    	log.debug("DISCONNECTED [" + username + "]");
    }
    
    
    private String getKonujCookieId(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        // Check to see if any cookies exists
        if (cookies != null)
        {        	
        	for (int i =0; i< cookies.length; i++)
        	{
        		Cookie aCookie = cookies[i];
        		if(aCookie.getName().equals("konuj_cookie"))
        		{
        			return aCookie.getValue();
        		}
        	}
        }
        return null;
    }
    
    private synchronized void join(HttpServletRequest request,HttpServletResponse response,String username)
    throws IOException
    {
        Member member = new Member();
       
        //Adding IP address of the client
        member.setIPAddress(request.getRemoteAddr());
        member.setUsername(username);
        
        //TODO generate new cookie if not received from the client side
        //If received, then use that cookie only
        
        String strCookieId = getKonujCookieId(request);
        if(strCookieId == null)
        {        	
	        //Creating cookie for the user
	        strCookieId = StringUtils.createGuid(100);
	        Cookie cookie_id = new Cookie("konuj_cookie", strCookieId);
	        cookie_id.setPath("/");        
	        cookie_id.setMaxAge(SECONDS_PER_YEAR);
	        response.addCookie(cookie_id);
	        log.trace("Created new cookie :" + strCookieId);
        }       
        else
        {
        	log.trace("Found existing cookie : " + strCookieId);
        }
        
        member.setCookie_id(strCookieId);
        
        members.put(username, member);
        log.debug("adding username[" + username + "]");
        
        synchronized (queMemberWaitingToChat) {			
			
        	if(queMemberWaitingToChat.size() == 0)
        	{
        		queMemberWaitingToChat.add(username);		        
        	}
	        else
	        {
	        	String strFirstUser = queMemberWaitingToChat.poll();
	        	//Here we are connecting both the users
	        	userMapper.put(strFirstUser, username);
	        	userMapper.put(username, strFirstUser);
	        		        	
	        	//Send notification to the users that they are now connected with each other
	        	Member otherMember = members.get(strFirstUser);
	        	sendMsgToMember(member, "JOINED", "join");
	        	sendMsgToMember(otherMember, "JOINED", "join");	      
	        	
	        	//Initialing the ChatTranscript Cache
	        	ChatServer.createChatTranscript(strFirstUser, username);
	        	
	        	//Resetting the first user id
	        	strFirstUser = "";	        	
	        	
	        }
        }
        
        response.setContentType("text/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.print("{\"action\":\"join\"}");
    }

    private synchronized void poll(HttpServletRequest request,HttpServletResponse response,String username)
    throws IOException
    {    	
    	Member member = members.get(username);
    	
        synchronized(member)
        {
            if (member._queue.size()>0)
            {
                // Send one chat message
                response.setContentType("text/json;charset=utf-8");
                StringBuilder buf=new StringBuilder();

                buf.append("{\"action\":\"poll\",");
                buf.append("\"from\":\"");
                String from = member._queue.poll();
                buf.append(from);
                buf.append("\",");

                String message = member._queue.poll();
                
                buf.append("\"chat\":\"");
                buf.append(StringUtils.escapeHtmlFull(message));
                buf.append("\"}");
                byte[] bytes = buf.toString().getBytes("utf-8");
                response.setContentLength(bytes.length);
                response.getOutputStream().write(bytes);
                
                if(member.shouldDisconnect)
                {
                	log.trace("Going to clean " + username);
                	cleanMappingsAndDisconnect(username);
                }
            }
            else 
            {
                Continuation continuation = ContinuationSupport.getContinuation(request,response);
                
                if (continuation.isInitial()) 
                {
                    // No chat in queue, so suspend and wait for timeout or chat
                    continuation.suspend();
                    member._continuation=continuation;                    
                }
                else
                {
                    // Timeout so send empty response
                    response.setContentType("text/json;charset=utf-8");
                    PrintWriter out=response.getWriter();
                    out.print("{\"action\":\"poll\"}");
                }
            }
        }
    }

    private synchronized void chat(HttpServletRequest request,HttpServletResponse response,String username,String message)
    throws IOException
    {
    	String otherUser = userMapper.get(username);
    	
    	Member firstMember = members.get(username);
    	Member secondMember = members.get(otherUser);

    	sendMsgToMember(firstMember, "Ben", message);
    	sendMsgToMember(secondMember, "Yabanji", message);
    	
    	//Adding Chat message into ChatTranscript
    	ChatServer.addChatMessage(username, message);
    	
        response.setContentType("text/json;charset=utf-8");
        PrintWriter out=response.getWriter();
        out.print("{\"action\":\"chat\"}");  
    }
    
    private static void sendMsgToMember(Member m, String username, String message)
    {
    	synchronized (m)
        {
    		try
    		{
	            m._queue.add(username); // from
	            m._queue.add(message);  // chat
	
	            // wakeup member if polling
	            if (m._continuation!=null)
	            {
	            	try
	            	{
		                m._continuation.resume();                
		                m._continuation=null;
	            	}
	            	catch(IllegalStateException e)
	            	{
	            		log.debug("Illegal state IDLE " + m.getUsername());
	            		//e.printStackTrace();
	            	}
                    catch(Exception e)
                    {
                        log.debug("Exception caught sendMsgToMember :" + m.getUsername());
                        //e.printStackTrace();
                    }
	            }
	            else
	            {
	            	log.debug("Found _continuation null for user " + m.getUsername());
	            }
    		}
    		catch(Exception e)
    		{
    			log.error("Exception with user[" + m.getUsername() + "]", e);    			
    		}
        }

    }
    
    // Serve the HTML with embedded CSS and Javascript.
    // This should be static content and should use real JS libraries.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	log.debug(request.getQueryString());
    	log.debug(request.getRequestURL());
        if (request.getParameter("action")!=null)
            doPost(request,response);
        else
        {
            //getServletContext().getNamedDispatcher("default").forward(request,response);
        	/* Musa Comment
            response.setContentType("text/json;charset=utf-8");
        	PrintWriter out=response.getWriter();
        	out.print("{fake:\"fake\"}"); 
            */
        }
    }    
}