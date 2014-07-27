package com.konuj.utils.properties;

public class KonujPropertyDefaults {

	public static final String SERVER_PORT = "8080";
	public static final String SESSION_IDLE_TIME_IN_SEC = "120";
	public static final String MONITOR_THREAD_DELAY_IN_SEC = "120";
	public static final String SERVER_RUN_ONLY_LOCAL = "false";
	
	public static final String DBCP_THREADPOOL_SIZE = "16"; 
	public static final String DBCP_INITIAL_SIZE = "4";
	public static final String DBCP_MAX_CONNECTIONS = "16"; 
	public static final String DBCP_MAX_IDLE = "-1";
	public static final String DBCP_CONNECT_URI = "jdbc:mysql://localhost:3306/konuj";
	public static final String DBCP_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	public static final String DBCP_DB_USER_ID = "root";
	public static final String DBCP_DB_PASSWORD = "root";
	
	public static final String DOWNLOAD_FILENAME_LENGTH = "8";
	public static final String DOWNLOAD_FILE_PATH = "/opt/konuj/dist/webapp/l/";
	public static final String DOWNLOAD_URL_PATH = "http://127.0.0.1:8080/l/";
	public static final String DOWNLOAD_FILE_ENCODING = "UTF-8";
	public static final String DOWNLOAD_CDN_LOCATION = "http://konuj.cdnturkiye.com/logs/1/img/smileys/";
	
}
