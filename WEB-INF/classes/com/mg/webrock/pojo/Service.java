package com.mg.webrock.pojo;
import java.lang.reflect.*;
public class Service
{
private Class serviceClass;
private Method serviceMethod;
private String path;
private String forwardTo;
private boolean isGetAllowed;
private boolean isPostAllowed;
private boolean runOnStartup;
private int priority;
private boolean injectApplicationDirectory;
private boolean injectApplicationScope;
private boolean injectSessionScope;
private boolean injectRequestScope;
private boolean isAutoWired;
private boolean injectRequestParameter;
private boolean isSecuredAccess;
private Class checkPost;
private Method guard;
public Service()
{
this.serviceClass=null;
this.serviceMethod=null;
this.path="";
this.forwardTo="";
this.isGetAllowed=false;
this.isPostAllowed=false;
this.runOnStartup=false;
this.priority=0;
this.injectApplicationDirectory=false;
this.injectApplicationScope=false;
this.injectSessionScope=false;
this.injectRequestScope=false;
this.isAutoWired=false;
this.injectRequestParameter=false;
this.isSecuredAccess=false;
this.checkPost=null;
this.guard=null;
}
public void setServiceClass(java.lang.Class serviceClass)
{
this.serviceClass=serviceClass;
}
public java.lang.Class getServiceClass()
{
return this.serviceClass;
}
public void setServiceMethod(java.lang.reflect.Method serviceMethod)
{
this.serviceMethod=serviceMethod;
}
public java.lang.reflect.Method getServiceMethod()
{
return this.serviceMethod;
}
public void setPath(java.lang.String path)
{
this.path=path;
}
public java.lang.String getPath()
{
return this.path;
}
public void setForwardTo(java.lang.String forwardTo)
{
this.forwardTo=forwardTo;
}
public java.lang.String getForwardTo()
{
return this.forwardTo;
}
public void setIsGetAllowed(boolean isGetAllowed)
{
this.isGetAllowed=isGetAllowed;
}
public boolean getIsGetAllowed()
{
return this.isGetAllowed;
}
public void setIsPostAllowed(boolean isPostAllowed)
{
this.isPostAllowed=isPostAllowed;
}
public boolean getIsPostAllowed()
{
return this.isPostAllowed;
}
public void setRunOnStartup(boolean runOnStartup)
{
this.runOnStartup=runOnStartup;
}
public boolean getRunOnStartup()
{
return this.runOnStartup;
}
public void setPriority(int priority)
{
this.priority=priority;
}
public int getPriority()
{
return this.priority;
}
public void setInjectApplicationDirectory(boolean injectApplicationDirectory)
{
this.injectApplicationDirectory=injectApplicationDirectory;
}
public boolean getInjectApplicationDirectory()
{
return this.injectApplicationDirectory;
}
public void setInjectApplicationScope(boolean injectApplicationScope)
{
this.injectApplicationScope=injectApplicationScope;
}
public boolean getInjectApplicationScope()
{
return this.injectApplicationScope;
}
public void setInjectSessionScope(boolean injectSessionScope)
{
this.injectSessionScope=injectSessionScope;
}
public boolean getInjectSessionScope()
{
return this.injectSessionScope;
}
public void setInjectRequestScope(boolean injectRequestScope)
{
this.injectRequestScope=injectRequestScope;
}
public boolean getInjectRequestScope()
{
return this.injectRequestScope;
}
public void setIsAutoWired(boolean isAutoWired)
{
this.isAutoWired=isAutoWired;
}
public boolean getIsAutoWired()
{
return this.isAutoWired;
}
public void setInjectRequestParameter(boolean injectRequestParameter)
{
this.injectRequestParameter=injectRequestParameter;
}
public boolean getInjectRequestParameter()
{
return this.injectRequestParameter;
}
public void setIsSecuredAccess(boolean isSecuredAccess)
{
this.isSecuredAccess=isSecuredAccess;
}
public boolean getIsSecuredAccess()
{
return this.isSecuredAccess;
}
public void setCheckPost(Class checkPost)
{
this.checkPost=checkPost;
}
public Class getCheckPost()
{
return this.checkPost;
}
public void setGuard(Method guard)
{
this.guard=guard;
}
public Method getGuard()
{
return this.guard;
}
}