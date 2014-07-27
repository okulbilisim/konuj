package com.konuj.servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet
{
	String str = "";
	
	public HelloServlet() {
		this.str="Hello World";
	}
	public HelloServlet(String str) {
		this.str = str;
	}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException    , IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>" + this.str + "</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
    }
}