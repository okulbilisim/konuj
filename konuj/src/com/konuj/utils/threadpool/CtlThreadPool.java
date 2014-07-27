package com.konuj.utils.threadpool;

import java.util.*;

import org.apache.log4j.Logger;


/**
 * This is a class which implements all the features of <thread pool> 
 * One needs to create an object of this class and assign task to it.  
 * 
 */
public class CtlThreadPool
{	
	/**
	 * Class store the request(task) object along with Id.
	 * Id is used for the serialization of the task to be processed.    
	 */
	class TaskInfo
	{
		TaskInfo(CtlObjectMember r, String s)
		{
			m_runner = r;
			m_strSerialId = s;
		}
		public String			m_strSerialId;	// id for serialization of tasks 
		public CtlObjectMember	m_runner;		// task (object from the EventHandler <object pool>)
	}
	
	/**
	 * Private logger
	 */
	private final Logger logger = Logger.getLogger(CtlThreadPool.class);
	
	/**
	 * This boolean keeps track if the very first thread has started or not.
	 * This prevents the object from falsely reporting that the ThreadPool is
	 * done, just because the first thread has not yet started.
	 */
	private boolean			m_started					= false;
	
	/**
	 * The number of Worker object threads that are currently working on
	 * something.
	 */
	private int				m_activeThreads				= 0;
	
	/**
	 * The capacity of the pool.
	 */
	protected Thread		m_threads[]					= null;
	
	
	/**
	 * The backlog of tasks that need to be checked to see if they have an active serial id. 
	 * If they have an active serial id, then they are moved to m_lstBusyTasks.
	 * If not then they are given to a thread for processing.
	 */
	ArrayList<TaskInfo>		m_lstWatingTasks			= new ArrayList<TaskInfo>(16);
	
	/**
	 * These are tasks that have an active serial id.
	 */
	ArrayList<TaskInfo>		m_lstBusyTasks				= new ArrayList<TaskInfo>(16);
	
	
	/**
	 * Used to serialize on a given key so only one thread is performing actions for a given key.
	 */
	Map<String, Boolean>	m_mapActiveTasksSerialId	= new TreeMap<String, Boolean>();
	
	/**
	 * The constructor. 
	 * @param size
	 *            Number of threads in the thread pool.
	 */
	public CtlThreadPool(int size)
	{		
		m_threads = new EvtWorkerThread[size];
		for (int i = 0; i < m_threads.length; i++)
		{
			m_threads[i] = new EvtWorkerThread(this);
			m_threads[i].setName("Thread"+i);
			m_threads[i].start();
		}

		logger.info("No of threads started [" + size + "] for DBCP thread-pool");
	}
	

	/**
	 * Add a task to the thread pool. Any class which implements the Runnable interface may be assigned. 
	 * When this task runs, its run method will be called.
	 * 
	 * @param r
	 * 			An object that implements the CtlObjectMemeber <interface>
	 * @param strSerialId
	 * 			If not null, then all runnable tasks with the same serial id will be executed in the order in which they are given.
	 */
	
	public synchronized void assign(CtlObjectMember r, String strSerialId)
	{
		logger.trace("New task is added to waiting task list, with SerialId [" + strSerialId + "]");
		TaskInfo ti = new TaskInfo(r, strSerialId);		
		m_lstWatingTasks.add(ti);
		beginTask();
		logger.trace("Notify waiting threads to start servicing the task in the waiting list");
		notify();
	}
	

	/**
	 * Get a new work assignment.
	 * 
	 * @return TaskInfo 
	 * 			A new assignment
	 */
	public synchronized TaskInfo getTask()
	{
		//TODO Function should not reurn null, because null will stop the processing of scanning and executing of task.
		try
		{
			
			/******************************
			if(m_lstBusyTasks!=null)
			{
				TaskInfo tiBusy;
				for (Iterator<TaskInfo> it = m_lstBusyTasks.iterator(); it.hasNext();)
				{
					tiBusy = it.next();
					CtlLog.log(CtlLog.LL_DETAIL, "~~~~$$$$$$$~~~~ Busy list having IDs:  SerialId[" + tiBusy.m_strSerialId +"  " + tiBusy.m_runner.getClass()+"] ~~~~$$$$$$$~~~~");
				}
			}
			*****************************/
			
			// If there are no tasks, then wait for a new tasks
			while (!m_lstWatingTasks.iterator().hasNext())
			{
				wait();
			}
			
			// Tasks is waiting. Check if the the serial id is active.
			while (m_lstWatingTasks.iterator().hasNext())
			{
				// Get first waiting task that does not have an active serial id
				TaskInfo ti = (TaskInfo) m_lstWatingTasks.iterator().next();
				
				if ((ti.m_strSerialId != null) && (ti.m_strSerialId.length() > 0))
				{
				 	logger.debug("Removing the task with SerialId [" + ti.m_strSerialId + "] from waiting task list.");
					m_lstWatingTasks.remove(ti);
					
					if (m_mapActiveTasksSerialId.containsKey(ti.m_strSerialId))
					{
						// This task has an active serial id, so dont use it.
						// Put it in the busy list and continue waiting for
						// another task.
						logger.debug("The task contains an active serial id, this should not be used now. So adding the task in busy list with active SerialId[" + ti.m_strSerialId + "]");
						m_lstBusyTasks.add(ti);
						//wait(); Current thread must be waiting for the other task to be complete first for the same Id.  
						//return null;
					}
					else
					{
						// This task does not have an active serial id, so use
						// it.
						logger.debug("The task does not contain an active SerialId[" + ti.m_strSerialId + "]. So adding the SerialId in active serialId list.");
						m_mapActiveTasksSerialId.put(ti.m_strSerialId, true);
						return ti;
					}
				}
				else
				{
					// Task is not serialized, so use it
					logger.debug("Remove the task from from wait list. As it did not contain the SerialId");
					m_lstWatingTasks.remove(ti);
					return ti;
				}
			}
		}
		catch (InterruptedException e)
		{
			logger.error("Thread is unable to process the task. Remove the thread from the active thread list", e);			
			endTask();
		}
		catch(Exception e)
		{
			logger.error("Exception while getting the task from ThreadPool.", e);
			endTask();
		}		
		return null;
	}
	

	/**
	 * When thread is done executing, then this must be called to clean up task serialization management.
	 * @param ti
	 * 			task
	 */
	public synchronized void releaseTask(TaskInfo ti)
	{
		logger.debug("Releasing the finished task from task list");
		String strSerialId = null;

		if(ti != null)
		{
			strSerialId = ti.m_strSerialId;
			logger.trace("Remove pending ActionHandlers ["+ti.m_runner.getClass().getName()+"] from task list");
			ti.m_runner.release(ti.m_runner.getActionHandlerGuid());
			ti.m_runner = null;
			ti = null;
		}
		
		// Clean the executed TaskInfo object irrespective of serial-id is null opr not
		
		if ((strSerialId != null) && (strSerialId.length() > 0))
		{
				
			// Put next task for the same id from the busy list to the waiting list
			logger.debug("Remove the SerialId [" + strSerialId + "] from active task SerialId list.");
			m_mapActiveTasksSerialId.remove(strSerialId);
			
			
			// int iBusyIdx = m_lstBusyTasks.indexOf(ti.m_strSerialId);
			// Check if a busy list has task with the same serial
			TaskInfo tiBusy;
			for (Iterator<TaskInfo> it = m_lstBusyTasks.iterator(); it.hasNext();)
			{
				tiBusy = it.next();
				
				
				// Find first occurance
				if (tiBusy.m_strSerialId.equals(strSerialId))
				{
					// Additional tasks with same id are waiting. Put it
					// at the beginning of the list to make sure it gets
					// processed in correct order for the same serial id.
					logger.debug( "Task with SerialId[" + tiBusy.m_strSerialId + "] are waiting to run. Task will be removed from busy list and added to wait list to run");
					m_lstBusyTasks.remove(tiBusy);
					m_lstWatingTasks.add(0, tiBusy);
					notify();
					return;
				}
			}
		}
	}
	
	/**
	 * Called to block the current thread until the thread pool has no more work.
	 */
	public void complete()
	{
		beginWait();
		blockTask();
	}
	
	/**
	 * Called to reset the threads, put all the threads in the waiting state.   
	 */
	protected void finalize()
	{
		reset();
		for (int i = 0; i < m_threads.length; i++)
		{
			if (m_threads[i].isAlive())
			{
				m_threads[i].interrupt();
				beginTask();
			}
		}
		blockTask();
	}
	
	/**
	 * Called by a Task object to indicate that it has begun working on workload.
	 */
	synchronized public void beginTask()
	{
		logger.debug("Begin the active task.");
		m_activeThreads++;
		m_started = true;
	}
	

	/**
	 * Called by a Task object to indicate that it has ended working on workload.
	 */
	synchronized public void endTask()
	{
		logger.debug("End the active task.");
		m_activeThreads--;
		notify();
	}
	

	/**
	 * All the task's have completed working on a workload's.
	 */
	synchronized public void beginWait()
	{
		try
		{
			while (!m_started)
			{
				wait();
			}
		}
		catch (InterruptedException eIntExp)
		{
			// TODO Log the exception and Destroy the Thread
			logger.error("Destroying the active tasks, found exception while bringing thread pool on wait state.", eIntExp);
			endTask();
		}
	}
	

	/**
	 * This method can be called to block the current thread until the ThreadPool is done.
	 */
	synchronized public void blockTask()
	{
		try
		{
			while (m_activeThreads > 0)
			{
				wait();
			}
		}
		catch (InterruptedException e)
		{
			// TODO Log the exception and Destroy The Thread
			logger.error("Found exception while bringing the thread pool on wait state.", e);
		}
	}
	

	/**
	 * Called to reset this object to its initial state.
	 */
	synchronized public void reset()
	{
		m_activeThreads = 0;
		m_started = false;
	}
	
	
	
	/**
	 * The EvtWorker threads that make up the thread pool.
	 */
	class EvtWorkerThread extends Thread
	{
		/**
		 * The thread pool that this object belongs to.
		 */
		public CtlThreadPool	m_owner;
		

		/**
		 * The constructor.
		 * @param o
		 *            the thread pool
		 */
		EvtWorkerThread(CtlThreadPool o)
		{
			m_owner = o;
		}
		

		/**
		 * Scan for and execute tasks.
		 */
		public void run()
		{
			TaskInfo target = null;
			do
			{
				// 1. Get the task if any present in the wait list, and associate serial id with this thread
				target = m_owner.getTask();
				
				if (target != null)
				{
					logger.debug("Executing the task with SerialId [" + target.m_strSerialId + "]");

					try
					{
						// 2 .execute the task						
						target.m_runner.execute();
					}
					catch( Exception exp )
					{
						logger.error("Unable to execute the task with SerialId [" + target.m_strSerialId  + "]", exp);
					}
					finally
					{
						// 3. Finally, realease the serial id associated with this thread by removing it from Active list & Busy list					
						m_owner.releaseTask(target);
						target = null;
					}
				}
			}
			while (true);
		}
	}
}
