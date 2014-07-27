package com.konuj.objects;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;


import org.eclipse.jetty.continuation.Continuation;

public class Member
{
	private Date join_time;
	private String strIPAddress;
	private String username;
	private String cookie_id;
	public Continuation _continuation;
    public Queue<String> _queue = new LinkedList<String>();
    public boolean shouldDisconnect = false;
    public boolean isSessionTimedout = true;
    private long m_lastActivity;
    
    public Member() {
		join_time = new Date();
	}
    
    public long getIdleTime()
	{
		return System.currentTimeMillis() - m_lastActivity;
	}
    
    public void setLastActivityTime(long lastActivityTime)
	{
		m_lastActivity = lastActivityTime;
	}
    
    public String getIPAddress() {
		return strIPAddress;
	}

	public void setIPAddress(String strIPAddress) {
		this.strIPAddress = strIPAddress;
	}


    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCookie_id() {
		return cookie_id;
	}

	public void setCookie_id(String cookieId) {
		cookie_id = cookieId;
	}
	public Date getJoinTime() {
		return join_time;
	}




}
