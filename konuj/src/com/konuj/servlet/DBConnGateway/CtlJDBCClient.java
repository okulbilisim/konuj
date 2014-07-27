package com.konuj.servlet.DBConnGateway;


import org.apache.log4j.Logger;

import com.konuj.utils.threadpool.CtlThreadPool;




public class CtlJDBCClient extends CtlDBCP
{	
	//Common ThreadPool will be shared between OLAP and OLTP instances
	private CtlThreadPool	m_oDBCrudThreadPool = null;
	private final Logger logger;
	
	public CtlJDBCClient() {
		logger = Logger.getLogger("CtlJDBCClient");
	}
	
	public void init(DbConnGatewayConfig dbConnGatewayConfig)
	{
		try
		{			
			super.init(dbConnGatewayConfig);
			
			//If threadpool is already initialized
			if(m_oDBCrudThreadPool == null)
			{
				// Creating separate Thread pool for DB CRUD operations
				m_oDBCrudThreadPool = new CtlThreadPool(dbConnGatewayConfig.m_iDBCP_ThreadPoolSize);
			}
	
		}
		catch(Exception e)
		{
		 	logger.error("Exception while initializing DBCP connection objects.", e);
		}
	}
	
	public CtlThreadPool getDBThreadPool()
	{
		return m_oDBCrudThreadPool;
	}
	

}
