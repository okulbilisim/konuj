package com.konuj.objects;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.konuj.servlet.ChatServlet;



public class CleanUpTask extends TimerTask {

	private static org.apache.log4j.Logger log = Logger.getLogger(CleanUpTask.class);
	
	private String user1;
	private String user2;
	
	public CleanUpTask(String user1, String user2) {
		this.user1 = user1;
		this.user2 = user2;
	}
	public void run() {
		
		cleanUser(user1);
		cleanUser(user2);
		
	}
	
	private void cleanUser(String user)
	{
		log.info("Cleaning up user[" + user + "]");
		if(user != null)
		{
			Member m1 = ChatServlet.members.remove(user);
			if(m1 != null && m1._continuation != null)
			{
				try
            	{
					 m1._continuation.complete();
					 m1._continuation = null;
            	}
				catch(IllegalStateException e)
            	{
					log.error("Illegal state IDLE " + m1.getUsername(), e);            		
            	}
			}
			ChatServlet.userMapper.remove(user);
		}
	}

	
}
