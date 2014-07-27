package com.konuj.utils.properties;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class KonujProperties {

	private static Logger log = Logger.getLogger(KonujProperties.class);
	
	public static int ServerPort;
	public static int SessionIdleTimeInSec;
	public static int MonitorThreadDelayInSec;
	public static Boolean ServerRunOnlyLocal;
	
	public static int iDBCP_ThreadPoolSize;
	public static int iDBCP_InitialSize;
	public static int iDBCP_MaxConnections; 
	public static int iDBCP_MaxIdle;
	public static String strDBCP_ConnectURI;
	public static String strDBCP_DriverClassName;
	public static String strDBCP_DBUserId;
	public static String strDBCP_DBPassword;
	
	public static int iDOWNLOAD_FilenameLength;
	public static String strDOWNLOAD_FilePath;
	public static String strDOWNLOAD_URLPath;
	public static String strDOWNLOAD_FileEncoding;
	public static String strDOWNLOAD_CDNLocation;	
	
	
	private static Properties properties = new Properties(); 
	
	
	public static void init()
	{
		try 
		{ 
			log.info("Loading properties...");
			
			properties.load(new FileInputStream("./properties/konuj.properties"));
			
			ServerPort = Integer.parseInt(properties.getProperty("konuj.ServerPort", KonujPropertyDefaults.SERVER_PORT));
			SessionIdleTimeInSec = Integer.parseInt(properties.getProperty("konuj.SessionIdleTimeInSec", KonujPropertyDefaults.SESSION_IDLE_TIME_IN_SEC));
			MonitorThreadDelayInSec = Integer.parseInt(properties.getProperty("konuj.MonitorThreadDelayInSec", KonujPropertyDefaults.MONITOR_THREAD_DELAY_IN_SEC));
			ServerRunOnlyLocal = Boolean.parseBoolean(properties.getProperty("konuj.ServerRunOnlyLocal", KonujPropertyDefaults.SERVER_RUN_ONLY_LOCAL));
			
			//Loading DBCP related properties from konuj.properties file
			iDBCP_ThreadPoolSize = Integer.parseInt(properties.getProperty("konuj.dbcp.ThreadPoolSize", KonujPropertyDefaults.DBCP_THREADPOOL_SIZE));
			iDBCP_InitialSize = Integer.parseInt(properties.getProperty("konuj.dbcp.InitialSize", KonujPropertyDefaults.DBCP_INITIAL_SIZE));
			iDBCP_MaxConnections = Integer.parseInt(properties.getProperty("konuj.dbcp.MaxConnections", KonujPropertyDefaults.DBCP_MAX_CONNECTIONS));
			iDBCP_MaxIdle = Integer.parseInt(properties.getProperty("konuj.dbcp.MaxIdle", KonujPropertyDefaults.DBCP_MAX_IDLE));
			
			strDBCP_ConnectURI = properties.getProperty("konuj.dbcp.ConnectURI", KonujPropertyDefaults.DBCP_CONNECT_URI);
			strDBCP_DriverClassName = properties.getProperty("konuj.dbcp.DriverClassName", KonujPropertyDefaults.DBCP_DRIVER_CLASS_NAME);
			strDBCP_DBUserId = properties.getProperty("konuj.dbcp.DBUserId", KonujPropertyDefaults.DBCP_DB_USER_ID);
			strDBCP_DBPassword = properties.getProperty("konuj.dbcp.DBPassword", KonujPropertyDefaults.DBCP_DB_PASSWORD);
			
			//Loading Download related properties from konuj.properties file
			iDOWNLOAD_FilenameLength = Integer.parseInt(properties.getProperty("konuj.download.filename_length", KonujPropertyDefaults.DOWNLOAD_FILENAME_LENGTH));
			
			strDOWNLOAD_FilePath = properties.getProperty("konuj.download.file_path", KonujPropertyDefaults.DOWNLOAD_FILE_PATH);
			strDOWNLOAD_URLPath = properties.getProperty("konuj.download.url_path", KonujPropertyDefaults.DOWNLOAD_URL_PATH);
			strDOWNLOAD_FileEncoding = properties.getProperty("konuj.download.file_encoding", KonujPropertyDefaults.DOWNLOAD_FILE_ENCODING);
			strDOWNLOAD_CDNLocation = properties.getProperty("konuj.download.cdn_location", KonujPropertyDefaults.DOWNLOAD_CDN_LOCATION);
			
			log.info("Properties loaded successfully.");
			
		} catch (Exception e) 
		{ 
			log.error("Exception loading property file", e);			
		}
		
	}
}