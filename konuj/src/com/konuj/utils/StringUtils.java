package com.konuj.utils;

import java.util.Random;

import org.apache.log4j.Logger;


public class StringUtils {

	
	private static Logger log = Logger.getLogger(StringUtils.class);

  private static final String ALPHA_NUM ="1234567890abcdefghijklmnopqrstuvwxyz";
	
	private static final String	GUID_CHAR_LIST	= "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private	static	Random g_Randomizer = new Random();
  
  public static String createAlphaNumericFileName(int len) {
    StringBuffer sb = new StringBuffer(len);
    for (int i=0; i<len; i++) {
        int ndx = (int)(Math.random()*ALPHA_NUM.length());
        sb.append(ALPHA_NUM.charAt(ndx));
    }
    sb.append(".html");
    return sb.toString();
  }

	
	public static String createGuid(int iLen)
	{
		log.trace("Creating the Guid.");
		StringBuffer strbufGuid = new StringBuffer(iLen);
		synchronized (g_Randomizer)
		{
			for (int i = 0; i < iLen; i++)
			{
				strbufGuid.append(GUID_CHAR_LIST.charAt(g_Randomizer.nextInt(GUID_CHAR_LIST.length())));
			}			
		}
		log.trace("Guid ["+strbufGuid.toString()+"] created.");
		return strbufGuid.toString();
	}
	
    public static StringBuilder escapeHtmlFull(String s)
    {
        StringBuilder b = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++)
        {
          char ch = s.charAt(i);
          if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9')
          {
            // safe
            b.append(ch);
          }
          else if (Character.isWhitespace(ch))
          {
            // paranoid version: whitespaces are unsafe - escape
            // conversion of (int)ch is naive
            b.append("&#").append((int) ch).append(";");
          }
          else if (Character.isISOControl(ch))
          {
            // paranoid version:isISOControl which are not isWhitespace removed !
            // do nothing do not include in output !
          }
          else if (Character.isHighSurrogate(ch))
          {
            int codePoint;
            if (i + 1 < s.length() && Character.isSurrogatePair(ch, s.charAt(i + 1))
              && Character.isDefined(codePoint = (Character.toCodePoint(ch, s.charAt(i + 1)))))
            {
               b.append("&#").append(codePoint).append(";");
            }
            else
            {
            	log.warn("bug:isHighSurrogate");
            }
            i++; //in both ways move forward
          }
          else if(Character.isLowSurrogate(ch))
          {
            // wrong char[] sequence, //TODO: LOG !!!
        	  log.warn("bug:isLowSurrogate");
            i++; // move forward,do nothing do not include in output !
          }
          else
          {
            if (Character.isDefined(ch))
            {
              // paranoid version
              // the rest is unsafe, including <127 control chars
              b.append("&#").append((int) ch).append(";");
            }
            //do nothing do not include undefined in output!
          }
       }
        
        return b;
    }

    public static void main(String args[])
    {
	    System.out.println(StringUtils.createGuid(100));
    }
	
}
