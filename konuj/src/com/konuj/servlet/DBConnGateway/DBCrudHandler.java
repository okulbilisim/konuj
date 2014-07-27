package com.konuj.servlet.DBConnGateway;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.konuj.ChatServer;
import com.konuj.utils.threadpool.CtlObjectMember;




public abstract class DBCrudHandler implements CtlObjectMember
{

	protected CtlJDBCClient m_oDBClient = null;
		
	public void init()
	{	
		m_oDBClient = ChatServer.getJDBCClient();
	}
	
	protected Connection getConnection() throws Exception
	{		
		return m_oDBClient.borrowObject();
	}
	
	protected void invalidateConnection(Connection connectionObj) throws Exception
	{		
		 m_oDBClient.invalidateConnection(connectionObj);
	}
	
	public void rollback(Connection conn) throws Exception
	{	
		CtlJDBCClient.rollback(conn);
	}

	public void commit(Connection conn) throws Exception
	{
		CtlJDBCClient.commit(conn);
	}	
	
	public void close(Connection conn) throws Exception
	{
		CtlJDBCClient.close(conn);
	}
	
	public void close(Statement stmt) throws Exception
	{
		CtlJDBCClient.close(stmt);
	}

	public void close(ResultSet rs, Statement stmt) throws Exception
	{
		CtlJDBCClient.close(rs, stmt);
	}
	
	public void execute()
	{
		init();
		
		handleDBCrudOperation();
		
	}
	
	/*
	 * Subclasses need to implement this method to process the DB operation.
	 */
	public abstract void handleDBCrudOperation();


	public String getActionHandlerGuid()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void init(Object o1, Object o2)
	{
		// TODO Auto-generated method stub
		
	}

	public void release()
	{
		// TODO Auto-generated method stub
		
	}

	public void release(String guid)
	{
		// TODO Auto-generated method stub
		
	}
	
}
