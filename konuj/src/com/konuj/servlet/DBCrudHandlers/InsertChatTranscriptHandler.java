package com.konuj.servlet.DBCrudHandlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;


import com.konuj.objects.ChatTranscript;
import com.konuj.objects.Member;
import com.konuj.servlet.DBConnGateway.DBCrudHandler;




public class InsertChatTranscriptHandler extends DBCrudHandler
{
	private static Logger log = Logger.getLogger(InsertChatTranscriptHandler.class);
	
	private Member m_oMember1;
	private Member m_oMember2;
	private ChatTranscript m_oChatTranscript;
	
	
	
	public InsertChatTranscriptHandler(Member member1, Member member2, ChatTranscript oChatTranscript)
	{
		this.m_oMember1 = member1;
		this.m_oMember2 = member2;
		this.m_oChatTranscript = oChatTranscript;
		
	}
	
	
	public void handleDBCrudOperation()
	{
		Connection oConnection = null;
		SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		try
		{
            if(m_oChatTranscript == null)
                return;
			synchronized (m_oChatTranscript) {
				
			
			
			if(m_oChatTranscript.getRecordCount() <= 0)
			{
				log.info("No messages are present in cache, so returning");
				return;
			}
			
			log.trace("Going to insert record in chat_transcripts table");
			
			oConnection = getConnection();
			
			
			
			if(m_oChatTranscript.getDBPrimaryKey() != null)
			{
				//Get the old transcript
				String strSelectQuery = "select transcript from chat_transcripts where id=" + m_oChatTranscript.getDBPrimaryKey();
				Statement stmt= oConnection.createStatement();
				ResultSet rs = stmt.executeQuery(strSelectQuery);
				rs.next();
				String strChatTranscript = rs.getString(1);
				
				//Send the update query
				String strUpdateQuery = "update chat_transcripts set transcript=concat(?,?) where id=?";
				PreparedStatement stmtQueryUpdate = oConnection.prepareStatement(strUpdateQuery);
				stmtQueryUpdate.setString(1, strChatTranscript);				
				stmtQueryUpdate.setString(2, m_oChatTranscript.toString(m_oChatTranscript.getRecordCount()));
				stmtQueryUpdate.setInt(3, m_oChatTranscript.getDBPrimaryKey());
				
				stmtQueryUpdate.executeUpdate();
				log.info("Updated record with id[" + m_oChatTranscript.getDBPrimaryKey() + "]");
			}
			else
			{
				
				
				String strQuery = "insert into chat_transcripts(user1_cookie_id, user1_ip, user1_join_time," +
				" user2_cookie_id, user2_ip, user2_join_time, transcript) values(?,?,?,?,?,?,?)";
				
				log.trace("Insert query is [" + strQuery + "]");
				//SQL injection attacks are prevented using PreparedStatement :-)
				PreparedStatement stmtQueryInsert = oConnection.prepareStatement(strQuery, Statement.RETURN_GENERATED_KEYS);
				
				if(m_oMember1 != null)
				{
					stmtQueryInsert.setString(1, m_oMember1.getCookie_id());
					stmtQueryInsert.setString(2, m_oMember1.getIPAddress());
					String sqlDate = SQL_DATE_FORMAT.format(m_oMember1.getJoinTime());			
					stmtQueryInsert.setString(3, sqlDate);
				}
				else
				{
					stmtQueryInsert.setNull(1, Types.VARCHAR);
					stmtQueryInsert.setNull(2, Types.VARCHAR);
					stmtQueryInsert.setNull(3, Types.DATE);
				}
				
				
				if(m_oMember2 != null)
				{
					stmtQueryInsert.setString(4, m_oMember2.getCookie_id());
					stmtQueryInsert.setString(5, m_oMember2.getIPAddress());
					String sqlDate = SQL_DATE_FORMAT.format(m_oMember2.getJoinTime());			
					stmtQueryInsert.setString(6, sqlDate);
				}
				else
				{
					stmtQueryInsert.setNull(4, Types.VARCHAR);
					stmtQueryInsert.setNull(5, Types.VARCHAR);
					stmtQueryInsert.setNull(6, Types.DATE);
				}	
						
				stmtQueryInsert.setString(7, m_oChatTranscript.toString(m_oChatTranscript.getRecordCount()));				
				stmtQueryInsert.executeUpdate();
				
				
				ResultSet rsKeys = stmtQueryInsert.getGeneratedKeys();
				rsKeys.next();
				int iGeneratedId = rsKeys.getInt(1); 
				
				//Save the generated id into ChatTranscript object
				m_oChatTranscript.setDBPrimaryKey(iGeneratedId);
				log.info("Genrated Id is [" + iGeneratedId + "]");

			}
			
			}
			commit(oConnection);
			
			log.trace("ChatTranscript inserted into DB successfully.");
			
		}
		catch(Exception e)
		{
			log.error("Exception while creating an entry in the chat_transcripts table.", e);
			
			try
			{
				this.invalidateConnection(oConnection);
				
			}catch(Exception ex)
			{
				log.error("Exception while invalidating the connection object.", ex);
			}
		}
		finally
		{
			try{
				close(oConnection);
				}catch(Exception e) { log.error("Exception while releasing mysql connection object.", e); }	
		}
		
	}
	
}
