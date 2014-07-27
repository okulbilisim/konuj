package com.konuj.servlet.DBConnGateway;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;



public class CtlDBCP
{
	
	private GenericObjectPool m_oConnectionPool = null;
	private GenericObjectPool.Config m_oPoolConfig = null; 	 
	private ConnectionFactory m_oConnectionFactory = null; 
	private PoolableConnectionFactory m_oPoolableConnectionFactory = null;
	private DbConnGatewayConfig m_oDbConnGatewayConfig  = null;
	private PoolingDataSource m_oPooingDataSource = null; 
	private Logger logger;
	
	public void init(DbConnGatewayConfig dbConnGatewayConfig)
	{
		logger = Logger.getLogger("dd");
		m_oDbConnGatewayConfig = dbConnGatewayConfig;
		setupDataSource();
		initializeInitialConnections();
	}
	
    private void setupDataSource() 
    {   
    	
    	// First we load the underlying JDBC driver.
        // You need this if you don't use the jdbc.drivers
        // system property.
        //
        logger.trace("Loading underlying JDBC driver. " +
        		"DriverClassName[" + m_oDbConnGatewayConfig.m_strDBCP_DriverClassName + "]");
        try 
        {
            Class.forName(m_oDbConnGatewayConfig.m_strDBCP_DriverClassName);
        } catch (ClassNotFoundException e) 
        {
            logger.error("Exception while loading JDBC driver " +
            		"DriverClassName[" + m_oDbConnGatewayConfig.m_strDBCP_DriverClassName + "]", e);
        }
    	
    	logger.info("Initializing DataSource for the database connections. " +
    			"ConnectURI[" + m_oDbConnGatewayConfig.m_strDBCP_ConnectURI + "] " +
    			"DriverClassName[" + m_oDbConnGatewayConfig.m_strDBCP_ConnectURI + "]");
    	try
    	{
	    	// First, we'll need a ObjectPool that serves as the
	        // actual pool of connections.
	        //
	        // We'll use a GenericObjectPool instance, although
	        // any ObjectPool implementation will suffice.
	        
	    	m_oPoolConfig = new GenericObjectPool.Config();
	    	m_oPoolConfig.maxActive = m_oDbConnGatewayConfig.m_iDBCP_MaxConnections;
	    	m_oPoolConfig.minIdle = m_oDbConnGatewayConfig.m_iDBCP_MaxIdle;
	    	//m_oPoolConfig.testOnBorrow = false;
	    	m_oPoolConfig.timeBetweenEvictionRunsMillis = -1;
	    	m_oPoolConfig.minEvictableIdleTimeMillis = -1;
	    	m_oPoolConfig.softMinEvictableIdleTimeMillis=-1;
	    	m_oPoolConfig.numTestsPerEvictionRun = -1;

	        m_oConnectionPool = new GenericObjectPool(null, m_oPoolConfig);
	        
	        // Next, we'll create a ConnectionFactory that the
	        // pool will use to create Connections.
	        // We'll use the DriverManagerConnectionFactory,
	        // using the connect string configured to connect to database        
	        m_oConnectionFactory = new DriverManagerConnectionFactory(m_oDbConnGatewayConfig.m_strDBCP_ConnectURI, m_oDbConnGatewayConfig.m_strDBCP_UserId , m_oDbConnGatewayConfig.m_strDBCP_Password);
	        	        
	        // Now we'll create the PoolableConnectionFactory, which wraps
	        // the "real" Connections created by the ConnectionFactory with
	        // the classes that implement the pooling functionality.
	        //
	        m_oPoolableConnectionFactory = new PoolableConnectionFactory(m_oConnectionFactory, m_oConnectionPool, null, null, false, true);
		        
	        //
	        // Finally, we create the PoolingDriver itself,
	        // passing in the object pool we created.
	        //
	        //m_oPooingDataSource = new PoolingDataSource(m_oConnectionPool);
    	}
    	catch(Exception e)
    	{
    		logger.error("Exception while initializing the DataSource for the database.", e);
    	}
            	
    	
    }
    
    private void initializeInitialConnections()
    {
    	logger.trace("Initializing the initial[" + m_oDbConnGatewayConfig.m_iDBCP_InitialSize + "] database connections objects.");
    	try
    	{
	    	for(int i = 0 ; i < m_oDbConnGatewayConfig.m_iDBCP_InitialSize ; i++)
	        {
	    		m_oConnectionPool.addObject();
	        }
    	}
    	catch(Exception e)
    	{
    		logger.error("Exception while initializing the initial[" + m_oDbConnGatewayConfig.m_iDBCP_InitialSize + "] database connection objects.", e);
    	}
    }
    
    public synchronized Connection borrowObject() throws Exception
    {	
    	logger.trace("in CtlDBCP.borrowObject()");
    	Connection conn = (Connection) m_oConnectionPool.borrowObject();
    	logger.trace("Borrowed Connection object [" + conn.hashCode() + "]");
    	/*
    	CtlLog.log(CtlLog.LL_DETAIL, "#######################");
    	CtlLog.log(CtlLog.LL_DETAIL, "MaxActive : [" + m_oConnectionPool.getMaxActive() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "MaxIdle : [" + m_oConnectionPool.getMaxIdle() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "MaxWait : [" + m_oConnectionPool.getMaxWait() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "MinEIdleTime : [" + m_oConnectionPool.getMinEvictableIdleTimeMillis() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "MinIdle : [" + m_oConnectionPool.getMinIdle() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "NumActive : [" + m_oConnectionPool.getNumActive() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "NumIdle : [" + m_oConnectionPool.getNumIdle() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "NumTest : [" + m_oConnectionPool.getNumTestsPerEvictionRun() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "Soft : [" + m_oConnectionPool.getSoftMinEvictableIdleTimeMillis() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "TimeBetween : [" + m_oConnectionPool.getTimeBetweenEvictionRunsMillis() + "]");
    	CtlLog.log(CtlLog.LL_DETAIL, "=======================");
    	*/
    	
    	return conn;
    }
    
    
    public synchronized void invalidateConnection(Connection connectionObj) throws Exception
    {	
    	logger.trace("in CtlDBCP.invalidateConnection()");
    	m_oConnectionPool.invalidateObject(connectionObj);

    }
    
    public static void close(Connection connObj) throws Exception
    {
    	if(connObj != null)
    	{
    		connObj.close();
    	}
    }   
    
    public static void close(ResultSet rs) throws Exception 
    {
		if (rs != null) 
		{
			rs.close();
		}		
	}

	public static void close(ResultSet rs, Statement stmt) throws Exception 
	{
		if (rs != null) 
		{
			rs.close();
		}
		if (stmt != null) 
		{
			stmt.close();
		}
	}

	public static void close(Statement stmt) throws Exception 
	{
		if (stmt != null) 
		{
			stmt.close();
		}
	}
	
	public static void rollback(Connection conn) throws Exception
	{	
		Statement stmt = conn.createStatement();
		stmt.executeQuery("rollback");
		close(stmt);
	}

	public static void commit(Connection conn) throws Exception
	{
		Statement stmt = conn.createStatement();
		stmt.executeQuery("commit");
		close(stmt);
	}
}
