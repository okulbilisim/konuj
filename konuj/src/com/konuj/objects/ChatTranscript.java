package com.konuj.objects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

public class ChatTranscript {
	
	private static Logger log = Logger.getLogger(ChatTranscript.class);

	private Integer iDBPrimaryKey = null;
	private String strUser1_SessionId;
	private String strUser2_SessionId;
	
	private Queue<ChatMsg> lstTranscriptQueue = new LinkedList<ChatMsg>();
	//private List<ChatMsg> lstTranscript = new CopyOnWriteArrayList<ChatMsg>();
	
	public ChatTranscript(String session1, String session2) {
		this.strUser1_SessionId = session1;
		this.strUser2_SessionId = session2;
	}
	
	public String getUser1_SessionId() {
		return strUser1_SessionId;
	}

	public String getUser2_SessionId() {
		return strUser2_SessionId;
	}
	
	public void addMessage(String sessionid, String msg)
	{
		ChatMsg oChatMsg = new ChatMsg(sessionid, msg);
		lstTranscriptQueue.add(oChatMsg);		
	}
	
	public String toString(int size)
	{
		StringBuffer strResult = new StringBuffer();
	
		for (int i = 0; i < size; i++) 
		{
			ChatMsg oChatMsg = lstTranscriptQueue.poll();
			if(oChatMsg != null)
			{				
				String strSessionId = oChatMsg.getStrSessionId();
				String strMsg = oChatMsg.getStrMessage();
				if(strSessionId.equals(strUser1_SessionId))
					strResult.append("User-1 : ");
				else
					strResult.append("User-2 : ");
				
				strResult.append(strMsg + "\n");	
			}
		}
		
		return strResult.toString();
	}
	
/*	public String toString()
	{
		StringBuffer strResult = new StringBuffer();
		
		Iterator<ChatMsg> itr = lstTranscript.iterator();
		while(itr.hasNext())
		{
			ChatMsg oChatMsg = itr.next();
			String strSessionId = oChatMsg.getStrSessionId();
			String strMsg = oChatMsg.getStrMessage();
			
			if(strSessionId.equals(strUser1_SessionId))
				strResult.append("User-1 : ");
			else
				strResult.append("User-2 : ");
			
			strResult.append(strMsg + "\n");	
		}
		
		
		return strResult.toString();
	}
*/
	
	public Integer getDBPrimaryKey() {
		return iDBPrimaryKey;
	}

	public void setDBPrimaryKey(int iDBPrimaryKey) {
		this.iDBPrimaryKey = iDBPrimaryKey;
	}

	public int getRecordCount()
	{
		log.debug("ChatTranscript::lstTranscriptQueue->size:" + lstTranscriptQueue.size());
		return lstTranscriptQueue.size();
	}
	
	
	class ChatMsg
	{
		private String strSessionId;
		private String strMessage;
		
		
		public ChatMsg(String sessionid, String message) {
			this.strSessionId = sessionid;
			this.strMessage = message;
		}
		public String getStrSessionId() {
			return strSessionId;
		}

		public String getStrMessage() {
			return strMessage;
		}

	}
}
