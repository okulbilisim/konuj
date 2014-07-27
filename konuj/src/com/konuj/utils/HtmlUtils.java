package com.konuj.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

public class HtmlUtils {

	  public String createHtmlHeader(String title) {

	    String htmlHeader = null;
	    htmlHeader = "<HTML><HEAD><TITLE> " + title + " </TITLE></HEAD><BODY>";
	    return htmlHeader;
	  }

	  public String getHtmlFooter() {

	    String htmlFooter = "</BODY></HTML>";
	    return htmlFooter;
	  }

	  public String getHead(int level, String heading) {
	    return "<H" + level + "> " + heading + "</H" + level + ">";
	  }

	  public String getTableHead(String align, int border) {

	    String tableHeader = null;
	    tableHeader = "<TABLE width='100%' align=" + align + " border=" + border + ">";
	    return tableHeader;

	  }

	  public String getTR(String align) {
	    String TRCell = null;
	    TRCell = "<TR align=" + align + ">";
	    return TRCell;
	  }

	  public String getTR() {
	    String TRCell = null;
	    TRCell = "<TR>";
	    return TRCell;
	  }

	  public String getTD(String align, String value) {
	    String TDCell = null;
	    TDCell = "<TD align=" + align + "> " + value + " </TD>";
	    return TDCell;
	  }

	  public String getTD() {
	    String TDCell = null;
	    TDCell = "<TD>";
	    return TDCell;
	  }

	  public String getTD(int width) {
	    String TDCell = null;
	    TDCell = "<TD WIDTH=" + width + ">";
	    return TDCell;
	  }

	  public String getTH(String align, String value) {
	    String THCell = null;
	    THCell = "<TH align=" + align + "> " + value + " </TH>";
	    return THCell;
	  }

	  public String getTableContents(String align, Vector values,
	      int elementCounter) throws IOException {

	    StringWriter Cells = new StringWriter();
	    String contents = new String();
	    int vsize = values.size();

	    Cells.write("<TR>");

	    for (int i = 0; i < vsize; i++) {
	    	Object obj = values.elementAt(i);
	    	String value = null;
	    	if(obj != null)	    		
	    		value = obj.toString();

	      if (i != 0) {
	        if (i >= elementCounter) {

	          if (i % elementCounter == 0) {
	            Cells.write("</TR>\n\n<TR>");
	          }
	        }
	      }

	      Cells.write("<TD align=" + align + "> " + value + " </TD> \n");
	    }

	    Cells.write("</TR>");

	    contents = Cells.toString();
	    Cells.flush();
	    Cells.close();

	    return contents;
	  }

	  public String getClosedTR() {
	    String TRCell = null;
	    TRCell = "</TR>";
	    return TRCell;
	  }

	  public String getClosedTD() {
	    String TDCell = null;
	    TDCell = "</TD>";
	    return TDCell;
	  }

	  public String getBR(int lines) {

	    StringWriter lineBR = new StringWriter();
	    String lineBRs = new String();

	    for (int i = 0; i <= lines; i++) {
	      lineBR.write("<BR>\n");
	    }
	    lineBRs = lineBR.toString();

	    return lineBRs;
	  }

	  public String getLI(String item) {

	    String li = new String("<LI>");
	    li += item;
	    return li;

	  }
	}
