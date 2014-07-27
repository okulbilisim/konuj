package com.konuj;

import java.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import com.konuj.objects.ChatTranscript;
import com.konuj.objects.Member;
import com.konuj.servlet.ChatServlet;
import com.konuj.servlet.DownloadServlet;
import com.konuj.servlet.UserCountServlet;
import com.konuj.servlet.DBConnGateway.CtlJDBCClient;
import com.konuj.servlet.DBConnGateway.DbConnGatewayConfig;
import com.konuj.servlet.DBCrudHandlers.InsertChatTranscriptHandler;
import com.konuj.servlet.admin.ChatTranscriptServlet;
import com.konuj.servlet.admin.ListTranscriptsServlet;
import com.konuj.utils.properties.KonujProperties;

public class ChatServer {

	private static final String LOG_PROPERTIES_FILE = "properties/log4j.properties";
	private static Logger log = Logger.getLogger(ChatServer.class);
	private static CtlJDBCClient g_jdbcClient			= null;
	private static Map<String, ChatTranscript> g_ChatTranscriptMap = new ConcurrentHashMap<String, ChatTranscript>();
	private static Timer g_Timer = new Timer();
	
	public ChatServer() {
		initializeLogger();
		
	}
	public void startServer()
	{
		try
		{
			KonujProperties.init();
			initDBCP();
			
			System.setProperty("org.mortbay.util.URI.charset","UTF-8");
			
			log.info("Going to start the jetty server on " + KonujProperties.ServerPort + " port.");
			Server server = new Server();
			SelectChannelConnector connector = new SelectChannelConnector();
			if(KonujProperties.ServerRunOnlyLocal)
				connector.setHost("localhost"); // bind jetty to run only from localhost
			connector.setPort(KonujProperties.ServerPort);
			server.addConnector(connector);


			server.setSendServerVersion(false); // Don't send server info

			ContextHandlerCollection contextHandlers = new ContextHandlerCollection();
			
			
			WebAppContext webappcontext = new WebAppContext();
		    webappcontext.setContextPath("/");		    
		    File warPath = new File("./webapp/");
		    webappcontext.setWar(warPath.getAbsolutePath());
			webappcontext.getInitParams().put("useFileMappedBuffer", "false"); // file lock problemi icin    
			webappcontext.setDescriptor("./properties/web.xml"); // file lock problemi icin ve ekstra ayarlar
		    
		    
	        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context.setContextPath("/chat");	        
	        
	        context.addServlet(new ServletHolder(new ChatServlet()),"/");
	        context.addServlet(new ServletHolder(new UserCountServlet()), "/count");
	        context.addServlet(new ServletHolder(new DownloadServlet()), "/download");
			//context.getInitParams().put("useFileMappedBuffer", "false");
	        
	        //Admin pages
	        //ServletContextHandler adminContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        //adminContext.setContextPath("/admin");	        
	        //adminContext.addServlet(new ServletHolder(new ListTranscriptsServlet()),"/");
	        //adminContext.addServlet(new ServletHolder(new ChatTranscriptServlet()),"/detail");
	        
	        
	        //contextHandlers.setHandlers(new Handler[]{ webappcontext,context, adminContext});
	        contextHandlers.setHandlers(new Handler[]{ webappcontext,context});
		    server.setHandler(contextHandlers);
		           
		    log.trace("Registered the servlets with the context path.");
	 
	        server.start();
	        server.join();
	        
	        log.info("Jetty server started");               
		}
		catch(Exception e)
		{
			log.error("Unable to start the Jetty server", e);			
		}
	}

	private static void print(Map map, String message) {
		 log.info("message: " + message);
		 Set entries = map.entrySet();
		 Iterator iterator = entries.iterator();
		 while (iterator.hasNext()) {
		   Map.Entry entry = (Map.Entry)iterator.next();
		   log.info(entry.getKey() + " : " + entry.getValue());
		 }
		 System.out.println();
	   }

	
	
	private void initDBCP()
	{
		try
		{
			log.info("Initializing DBCP...");
						
			DbConnGatewayConfig oDbConnGwCfg = new DbConnGatewayConfig();
			oDbConnGwCfg.m_iDBCP_ThreadPoolSize  = KonujProperties.iDBCP_ThreadPoolSize;
			oDbConnGwCfg.m_iDBCP_InitialSize     = KonujProperties.iDBCP_InitialSize;
			oDbConnGwCfg.m_iDBCP_MaxConnections  = KonujProperties.iDBCP_MaxConnections;
			oDbConnGwCfg.m_iDBCP_MaxIdle         = KonujProperties.iDBCP_MaxIdle;
			oDbConnGwCfg.m_strDBCP_ConnectURI    = KonujProperties.strDBCP_ConnectURI;
			oDbConnGwCfg.m_strDBCP_DriverClassName = KonujProperties.strDBCP_DriverClassName;
			oDbConnGwCfg.m_strDBCP_UserId 		  = KonujProperties.strDBCP_DBUserId;
			oDbConnGwCfg.m_strDBCP_Password 	  = KonujProperties.strDBCP_DBPassword;
			
			g_jdbcClient = new CtlJDBCClient();
			g_jdbcClient.init(oDbConnGwCfg);
						
			
			log.info("DBCP initialized successfully.");
			
			
			
			 g_Timer.schedule(new TimerTask() {
					
					
					public void run() {
						//log.info("In DB updater thread.");
						
						try
						{
							Iterator<Entry<String, ChatTranscript>> itr = g_ChatTranscriptMap.entrySet().iterator();
							while(itr.hasNext())
							{
								Entry<String, ChatTranscript> entry = itr.next();
								ChatTranscript oChatTranscript = entry.getValue();
								if(oChatTranscript.getRecordCount() >= 10)
								{
									Member member1 = null;
									Member member2 = null;
									
									if(oChatTranscript.getUser1_SessionId() != null )
										member1 = ChatServlet.members.get(oChatTranscript.getUser1_SessionId());
									
									if(oChatTranscript.getUser1_SessionId() != null )
										member2 = ChatServlet.members.get(oChatTranscript.getUser2_SessionId());
									
									InsertChatTranscriptHandler oInsertChatTranscriptHandler = new InsertChatTranscriptHandler(member1, member2, oChatTranscript);								
									getJDBCClient().getDBThreadPool().assign(oInsertChatTranscriptHandler, "");
								}
							}
						}
						catch(Exception e)
						{
							log.error("Error while writing to DB ...",e );
						}
						
					}
				}, 30 * 1000, 30 * 1000);
		        
		}
		catch(Exception e)
		{
			log.error("Error while initializing the DBCP", e);
		}
	}
	
	public static CtlJDBCClient getJDBCClient()
	{
		return g_jdbcClient;
	}
	
	private void initializeLogger()
	{
		Properties logProperties = new Properties();

	    try
	    {
	      // load our log4j properties / configuration file
	      logProperties.load(new FileInputStream(LOG_PROPERTIES_FILE));
	      PropertyConfigurator.configure(logProperties);
	      
	      log.info("Logging initialized.");
	    }
	    catch(IOException e)
	    {
	      throw new RuntimeException("Unable to load logging property " + LOG_PROPERTIES_FILE);
	    }
	}

	    
	public static void addChatMessage(String strSessionId, String strMessage)
	{
		log.trace("Adding new chat message into cache");
		ChatTranscript oChatTranscript = g_ChatTranscriptMap.get(strSessionId);
		if(oChatTranscript != null)
		{
			oChatTranscript.addMessage(strSessionId, strMessage);			
		}
		else
		{
			log.warn("ChatTranscript object not found for session[" + strSessionId + "]");
		}
	}
	
	public static void createChatTranscript(String strSessionId_1, String strSessionId_2)
	{
		log.trace("Creating new ChatTranscript object");
		
		ChatTranscript oChatTranscript = new ChatTranscript(strSessionId_1, strSessionId_2);
		g_ChatTranscriptMap.put(strSessionId_1, oChatTranscript);
		g_ChatTranscriptMap.put(strSessionId_2, oChatTranscript);
	}
	
	private static ChatTranscript removeChatTranscript(String strSessionId)
	{
		log.trace("Removing ChatTranscript object");
		ChatTranscript oChatTranscript = g_ChatTranscriptMap.remove(strSessionId);
		
		if(oChatTranscript != null)
		{
			//Removing all the references
			g_ChatTranscriptMap.remove(oChatTranscript.getUser1_SessionId());
			g_ChatTranscriptMap.remove(oChatTranscript.getUser2_SessionId());
		}
		
		return oChatTranscript;		
	}
	
	public static void writeFullChatTranscriptToDB(Member member1, Member member2)
	{
		log.trace("Writing chat transcript to DB");
		ChatTranscript oChatTranscript = removeChatTranscript(member1.getUsername());
		InsertChatTranscriptHandler oInsertChatTranscriptHandler = new InsertChatTranscriptHandler(member1, member2, oChatTranscript);
		
		getJDBCClient().getDBThreadPool().assign(oInsertChatTranscriptHandler, "");
		
	}

	public static void main(String[] args) throws Exception
    {
		ChatServer server = new ChatServer();
		server.startServer();
    }

}