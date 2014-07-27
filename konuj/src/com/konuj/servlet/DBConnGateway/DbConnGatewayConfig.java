package com.konuj.servlet.DBConnGateway;



/**
 * Configuration class for DB Connection Gateway settings.
 * 
 */
public class DbConnGatewayConfig
{
	
	public int					m_iDBCP_ThreadPoolSize	= -1;
	public int					m_iDBCP_MaxConnections	= -1;
	public int					m_iDBCP_MaxIdle			= -1;
	public int					m_iDBCP_InitialSize		= -1;
	public String				m_strDBCP_ConnectURI;
	public String				m_strDBCP_DriverClassName;
	public String				m_strDBCP_UserId;
	public String				m_strDBCP_Password;
}
