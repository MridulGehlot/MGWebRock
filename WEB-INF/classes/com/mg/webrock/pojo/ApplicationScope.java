package com.mg.webrock.pojo;
import javax.servlet.*;
public class ApplicationScope
{
private ServletContext servletContext;
public ApplicationScope(ServletContext servletContext)
{
this.servletContext=servletContext;
}
public void setAttribute(String param,Object value)
{
servletContext.setAttribute(param,value);
}
public Object getAttribute(String param)
{
return servletContext.getAttribute(param);
}
}