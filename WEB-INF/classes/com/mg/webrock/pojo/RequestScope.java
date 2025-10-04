package com.mg.webrock.pojo;
import javax.servlet.http.*;
public class RequestScope
{
private HttpServletRequest httpServletRequest;
public RequestScope(HttpServletRequest httpServletRequest)
{
this.httpServletRequest=httpServletRequest;
}
public void setAttribute(String param,Object value)
{
httpServletRequest.setAttribute(param,value);
}
public Object getAttribute(String param)
{
return httpServletRequest.getAttribute(param);
}
}