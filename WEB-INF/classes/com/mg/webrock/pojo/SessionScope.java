package com.mg.webrock.pojo;
import javax.servlet.http.*;
public class SessionScope
{
private HttpSession httpSession;
public SessionScope(HttpSession httpSession)
{
this.httpSession=httpSession;
}
public void setAttribute(String param,Object value)
{
httpSession.setAttribute(param,value);
}
public Object getAttribute(String param)
{
return httpSession.getAttribute(param);
}
}