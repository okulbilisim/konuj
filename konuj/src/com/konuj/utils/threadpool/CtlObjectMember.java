package com.konuj.utils.threadpool;

import java.io.Serializable;

 /**
 *	Class behaving as a memeber object of a pool must implement this interface. 
 *	Functions significant to life cycle of an object.    
 */

public interface CtlObjectMember extends Serializable
{ 
	
	public void init();
	
	/**
	 * Subclass will override this method and process the given task.
	 */
	public void execute();
	
	 /**
	 * Subclass will override this method and releases all the reosurces occupied in the init() and dereferenced the memeber varaibles.
	 */
	 public void release();
	 
	 /**
	 * Subclass will override this method and releases all the reosurces from pending action list.
	 */
	 public void release(String guid);

	 
	/**
	 * Method will retrun Guid associated with ActionHandler 
	 * @return
	 * 		String
	 */
	public String getActionHandlerGuid();

	 
}

