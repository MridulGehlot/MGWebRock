package com.mg.webrock;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import com.mg.webrock.pojo.*;
import java.lang.reflect.*;
import com.mg.webrock.annotation.*;
import com.mg.webrock.exceptions.*;
import com.google.gson.*;
public class MGWebRock extends HttpServlet
{
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
PrintWriter pw=null;
try
{
BufferedReader br=request.getReader();
StringBuffer sb=new StringBuffer();
String line;
String jsonString=null;
while(true)
{
line=br.readLine();
if(line==null) break;
sb.append(line);
}
if(sb.length()>0)
{
jsonString=sb.toString();
}
String path=request.getPathInfo();
if(path==null || path.isEmpty()) 
{
response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No service path specified.");
return;
}
if(path.endsWith("/"))
{
path=path.substring(0,path.length()-1);
}
ServletContext context=getServletContext();
HashMap<String,Service> urlMappings=(HashMap<String,Service>)context.getAttribute("urlMappings");
if(urlMappings==null)
{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No URL Mappings Found.");
return;
}
Service service=urlMappings.get(path);
if(service==null)
{
sendErrorPage(path,request,response);
return;
}
pw=response.getWriter();
response.setContentType("text/plain");
response.setCharacterEncoding("utf-8");
boolean isGetAllowed=service.getIsGetAllowed();
if(isGetAllowed==false)
{
System.out.println("Cannot Invoke : "+path);
System.out.println("Method Type : GET is not applicable");
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
return;
}
Object result=null;
Class targetClass=null;
targetClass=service.getServiceClass();
Method targetMethod=null;
targetMethod=service.getServiceMethod();
boolean isSecuredAccess=service.getIsSecuredAccess();
if(isSecuredAccess)
{
Class securityClass=service.getCheckPost();
Method securityMethod=service.getGuard();
checkSecurity(securityClass,securityMethod,request,response,path);
}
result=execute(targetClass,targetMethod,request,response,service,path,null,jsonString);
String forwardTo=service.getForwardTo();
while(forwardTo!=null)
{
Service ss=null;
if(forwardTo!=null)
{
ss=urlMappings.get(forwardTo);
if(ss==null)
{
RequestDispatcher rd=request.getRequestDispatcher(forwardTo);
rd.forward(request,response);
return;
}
else
{
targetClass=ss.getServiceClass();
targetMethod=ss.getServiceMethod();
boolean isa=ss.getIsSecuredAccess();
if(isa)
{
Class securityClass=service.getCheckPost();
Method securityMethod=service.getGuard();
checkSecurity(securityClass,securityMethod,request,response,path);
}
result=execute(targetClass,targetMethod,request,response,service,path,result,jsonString);
}
}
forwardTo=null;
forwardTo=ss.getForwardTo();
}//while loop ends here
if(result==null)
{
response.setContentType("text/plain");
pw.print("OK");
}
else if(result instanceof String) pw.print(result);
else
{
response.setContentType("application/json");
Gson gson=new Gson();
pw.print(gson.toJson(result));
}
pw.flush();
}catch(ServiceException se)
{
System.out.println(se);
sendErrorPage(se.getMessage(),request,response);
}
catch(Exception e)
{
System.out.println(e);
try{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}catch(IOException ioe){System.out.println(ioe);}
}
}
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
PrintWriter pw=null;
try
{
BufferedReader br=request.getReader();
StringBuffer sb=new StringBuffer();
String line;
String jsonString=null;
while(true)
{
line=br.readLine();
if(line==null) break;
sb.append(line);
}
if(sb.length()>0)
{
jsonString=sb.toString();
}
pw=response.getWriter();
response.setContentType("text/plain");
response.setCharacterEncoding("utf-8");

String path=request.getPathInfo();
if(path==null || path.isEmpty()) 
{
response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No service path specified.");
return;
}
if(path.endsWith("/"))
{
path=path.substring(0,path.length()-1);
}
ServletContext context=getServletContext();
HashMap<String,Service> urlMappings=(HashMap<String,Service>)context.getAttribute("urlMappings");
if(urlMappings==null)
{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No URL Mappings Found.");
return;
}
Service service=urlMappings.get(path);
if(service==null)
{
sendErrorPage(path,request,response);
return;
}
boolean isPostAllowed=service.getIsPostAllowed();
if(isPostAllowed==false)
{
System.out.println("Cannot Invoke : "+path);
System.out.println("Method Type : POST is not applicable");
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
return;
}
Object result=null;
Class targetClass=null;
targetClass=service.getServiceClass();
Method targetMethod=null;
targetMethod=service.getServiceMethod();
boolean isSecuredAccess=service.getIsSecuredAccess();
if(isSecuredAccess)
{
Class securityClass=service.getCheckPost();
Method securityMethod=service.getGuard();
checkSecurity(securityClass,securityMethod,request,response,path);
}
result=execute(targetClass,targetMethod,request,response,service,path,null,jsonString);
String forwardTo=service.getForwardTo();
while(forwardTo!=null)
{
Service ss=null;
if(forwardTo!=null)
{
ss=urlMappings.get(forwardTo);
if(ss==null)
{
RequestDispatcher rd=request.getRequestDispatcher(forwardTo);
rd.forward(request,response);
return;
}
else
{
targetClass=ss.getServiceClass();
targetMethod=ss.getServiceMethod();
boolean isa=ss.getIsSecuredAccess();
if(isa)
{
Class securityClass=service.getCheckPost();
Method securityMethod=service.getGuard();
checkSecurity(securityClass,securityMethod,request,response,path);
}
result=execute(targetClass,targetMethod,request,response,service,path,result,jsonString);
}
}
forwardTo=null;
forwardTo=ss.getForwardTo();
}//while loop ends here
if(result==null)
{
response.setContentType("text/plain");
pw.print("OK");
}
else if(result instanceof String) pw.print(result);
else
{
response.setContentType("application/json");
Gson gson=new Gson();
pw.print(gson.toJson(result));
}
pw.flush();
}catch(ServiceException se)
{
System.out.println(se);
sendErrorPage(se.getMessage(),request,response);
}catch(Exception ee)
{
System.out.println(ee);
try{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No URL Mappings Found.");
}catch(IOException ioe){System.out.println(ioe);}
}
}
public void sendErrorPage(String path,HttpServletRequest request,HttpServletResponse response)
{
try
{
RequestDispatcher rd=request.getRequestDispatcher("/error.jsp");
System.out.println("Sending error Page for - "+path);
rd.forward(request,response);
}catch(Exception e)
{
System.out.println(e);
}
}

void checkSecurity(Class targetClass,Method targetMethod,HttpServletRequest request,HttpServletResponse response,String path) throws ServiceException
{
try
{
Object instance=null;
Object parameterValues[]=null;
//a try block to handle everything before invocation
try
{
//this will end before invocation
ApplicationDirectory applicationDirectory=null;
ApplicationScope applicationScope=null;
RequestScope requestScope=null;
SessionScope sessionScope=null;
try
{
instance = targetClass.getDeclaredConstructor().newInstance();
}catch(NoSuchMethodException noSuchMethodException)
{
System.out.println(noSuchMethodException);
return;
}//try for creating new instance of class
try
{
Field fields[]=targetClass.getDeclaredFields();

//inject all at class level
if(targetClass.isAnnotationPresent(InjectApplicationDirectory.class))
{
Method setApplicationDirectory=targetClass.getMethod("setApplicationDirectory",ApplicationDirectory.class);
Parameter p[]=setApplicationDirectory.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setApplicationScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return;
}
File directory=new File(getServletContext().getRealPath(path));
applicationDirectory=new ApplicationDirectory(directory);
setApplicationDirectory.invoke(instance,applicationDirectory);
}
if(targetClass.isAnnotationPresent(InjectApplicationScope.class))
{
Method setApplicationScope=targetClass.getMethod("setApplicationScope",ApplicationScope.class);
Parameter p[]=setApplicationScope.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setApplicationScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return;
}
applicationScope=new ApplicationScope(getServletContext());
setApplicationScope.invoke(instance,applicationScope);
}
if(targetClass.isAnnotationPresent(InjectSessionScope.class))
{
Method setSessionScope=targetClass.getMethod("setSessionScope",SessionScope.class);
Parameter p[]=setSessionScope.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setSessionScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return;
}
sessionScope=new SessionScope(request.getSession());
setSessionScope.invoke(instance,sessionScope);
}
if(targetClass.isAnnotationPresent(InjectRequestScope.class))
{
Method setRequestScope=targetClass.getMethod("setRequestScope",RequestScope.class);
Parameter p[]=setRequestScope.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setRequestScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return;
}
requestScope=new RequestScope(request);
setRequestScope.invoke(instance,requestScope);
}
//inject all at class level ends here

for(Field f:fields)
{
if(f.isAnnotationPresent(AutoWired.class)) handleAutoWiring(instance,targetClass,request);
if(f.isAnnotationPresent(InjectRequestParameter.class))
{
InjectRequestParameter irp=f.getAnnotation(InjectRequestParameter.class);
String parameterName=irp.value();
String value=request.getParameter(parameterName);
Object val=null;
if(value!=null)
{
Class type=f.getType();
if(type==String.class) val=value;
else if(type==long.class || type==Long.class) val=Long.parseLong(value);
else if(type==int.class || type==Integer.class) val=Integer.parseInt(value);
else if(type==short.class || type==Short.class) val=Short.parseShort(value);
else if(type==byte.class || type==Byte.class) val=Byte.parseByte(value);
else if(type==char.class || type==Character.class) val=value.charAt(0);
else if(type==boolean.class || type==Boolean.class) val=Boolean.parseBoolean(value);
else if(type==double.class || type==Double.class) val=Double.parseDouble(value);
else if(type==float.class || type==Float.class) val=Float.parseFloat(value);
String identityName=f.getName();
String methodName="set"+identityName.substring(0,1).toUpperCase()+identityName.substring(1);
try
{
Method m = targetClass.getMethod(methodName, f.getType());
m.invoke(instance, val); // reflection handles type check
}catch(NoSuchMethodException e)
{
System.out.println("Setter not found for field: " + f.getName());
}catch(Exception e)
{
e.printStackTrace();
}
}//value != null
}//if Annotation is Present @InjectRequestParameter

}//for loop ends here
}catch(NoSuchMethodException nsme)
{
System.out.println("For All Inject Classes You should write Setter/Getter Functions");
System.out.println(nsme);
}
//We Have Handled Class Level Things Now Handle The Service Method
Parameter parameters[]=targetMethod.getParameters();
parameterValues=new Object[parameters.length];
for(int i=0;i<parameters.length;i++)
{
Parameter p=parameters[i];
Object value=null;
if(p.isAnnotationPresent(RequestParameter.class))
{
RequestParameter rp=(RequestParameter)p.getAnnotation(RequestParameter.class);
String paramName=rp.value();
String val=request.getParameter(paramName);
if(val!=null)
{
Class type=p.getType();
// Handle conversion for all 8 primitives + String
                    if(type == String.class) value = val;
                    else if(type == int.class || type == Integer.class) value = (val!=null && !val.isEmpty())? Integer.parseInt(val) : 0;
                    else if(type == long.class || type == Long.class) value = (val!=null && !val.isEmpty())? Long.parseLong(val) : 0L;
                    else if(type == double.class || type == Double.class) value = (val!=null && !val.isEmpty())? Double.parseDouble(val) : 0.0;
                    else if(type == float.class || type == Float.class) value = (val!=null && !val.isEmpty())? Float.parseFloat(val) : 0.0f;
                    else if(type == boolean.class || type == Boolean.class) value = (val!=null && !val.isEmpty())? Boolean.parseBoolean(val) : false;
                    else if(type == char.class || type == Character.class) value = (val!=null && !val.isEmpty())? val.charAt(0) : '\0';
                    else if(type == short.class || type == Short.class) value = (val!=null && !val.isEmpty())? Short.parseShort(val) : (short)0;
                    else if(type == byte.class || type == Byte.class) value = (val!=null && !val.isEmpty())? Byte.parseByte(val) : (byte)0;
}
}//if @RequestParameter is present ends
else
{
Class type=p.getType();
if(type==ApplicationDirectory.class)
{
if(applicationDirectory==null)
{
File directory=new File(getServletContext().getRealPath(path));
applicationDirectory=new ApplicationDirectory(directory);
}
value=applicationDirectory;
}
else if(type==ApplicationScope.class)
{
if(applicationScope==null) applicationScope=new ApplicationScope(getServletContext());
value=applicationScope;
}
else if(type==SessionScope.class)
{
if(sessionScope==null) sessionScope=new SessionScope(request.getSession());
value=sessionScope;
}
else if(type==RequestScope.class)
{
if(requestScope==null) requestScope=new RequestScope(request);
value=requestScope;
}
}//else part ends here
parameterValues[i]=value;
}//for loop ends here for parameters of target Method
//a try block to handle everything before invocation
}catch(Exception ee)
{
System.out.println(ee);
}
//now we need to invoke the method
targetMethod.invoke(instance,parameterValues);
}catch(InvocationTargetException ite)
{
Throwable cause = ite.getCause(); // unwrap
if (cause instanceof ServiceException) {
throw (ServiceException) cause; // rethrow so caller can catch
} else {
throw new RuntimeException(cause); // or handle/log other exceptions
}
}
catch(IllegalAccessException iae)
{
System.out.println(iae);
}
}// void check security ends here

Object execute(Class targetClass,Method targetMethod,HttpServletRequest request,HttpServletResponse response,Service service,String path,Object args,String jsonString) throws ServiceException
{
Object result=null;
try
{
boolean injectApplicationDirectory=service.getInjectApplicationDirectory();
boolean injectApplicationScope=service.getInjectApplicationScope();
boolean injectSessionScope=service.getInjectSessionScope();
boolean injectRequestScope=service.getInjectRequestScope();
boolean injectRequestParameter=service.getInjectRequestParameter();
ApplicationDirectory applicationDirectory=null;
ApplicationScope applicationScope=null;
RequestScope requestScope=null;
SessionScope sessionScope=null;
try
{
//Object instance=targetClass.newInstance();
Object instance=null;
try
{
instance = targetClass.getDeclaredConstructor().newInstance();
}catch(NoSuchMethodException noSuchMethodException)
{
System.out.println(noSuchMethodException);
return null;
}
if(service.getIsAutoWired()) handleAutoWiring(instance,targetClass,request);
try
{
if(injectRequestParameter)
{
Field fields[]=targetClass.getDeclaredFields();
for(Field f:fields)
{
if(f.isAnnotationPresent(InjectRequestParameter.class))
{
InjectRequestParameter irp=f.getAnnotation(InjectRequestParameter.class);
String parameterName=irp.value();
String value=request.getParameter(parameterName);
Object val=null;
if(value!=null)
{
Class type=f.getType();
if(type==String.class) val=value;
else if(type==long.class || type==Long.class) val=Long.parseLong(value);
else if(type==int.class || type==Integer.class) val=Integer.parseInt(value);
else if(type==short.class || type==Short.class) val=Short.parseShort(value);
else if(type==byte.class || type==Byte.class) val=Byte.parseByte(value);
else if(type==char.class || type==Character.class) val=value.charAt(0);
else if(type==boolean.class || type==Boolean.class) val=Boolean.parseBoolean(value);
else if(type==double.class || type==Double.class) val=Double.parseDouble(value);
else if(type==float.class || type==Float.class) val=Float.parseFloat(value);
String identityName=f.getName();
String methodName="set"+identityName.substring(0,1).toUpperCase()+identityName.substring(1);
try
{
Method m = targetClass.getMethod(methodName, f.getType());
m.invoke(instance, val); // reflection handles type check
}catch(NoSuchMethodException e)
{
System.out.println("Setter not found for field: " + f.getName());
}catch(Exception e)
{
e.printStackTrace();
}
}//value != null
}//if Annotation is Present @InjectRequestParameter
}//for loop ends
}
if(injectApplicationDirectory)
{
Method setApplicationDirectory=targetClass.getMethod("setApplicationDirectory",ApplicationDirectory.class);
Parameter p[]=setApplicationDirectory.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setApplicationScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}
File directory=new File(getServletContext().getRealPath(path));
applicationDirectory=new ApplicationDirectory(directory);
setApplicationDirectory.invoke(instance,applicationDirectory);
}
if(injectApplicationScope)
{
Method setApplicationScope=targetClass.getMethod("setApplicationScope",ApplicationScope.class);
Parameter p[]=setApplicationScope.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setApplicationScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}
applicationScope=new ApplicationScope(getServletContext());
setApplicationScope.invoke(instance,applicationScope);
}
if(injectSessionScope)
{
Method setSessionScope=targetClass.getMethod("setSessionScope",SessionScope.class);
Parameter p[]=setSessionScope.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setSessionScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}
sessionScope=new SessionScope(request.getSession());
setSessionScope.invoke(instance,sessionScope);
}
if(injectRequestScope)
{
Method setRequestScope=targetClass.getMethod("setRequestScope",RequestScope.class);
Parameter p[]=setRequestScope.getParameters();
if(p.length<1 || p.length>1)
{
System.out.println("Invalid Number Of Parameters In setRequestScope");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}
requestScope=new RequestScope(request);
setRequestScope.invoke(instance,requestScope);
}
}catch(NoSuchMethodException nsme)
{
System.out.println("For All Inject Classes You should write Setter/Getter Functions");
System.out.println(nsme);
}
//check for args
if(args!=null)
{
Parameter p[]=targetMethod.getParameters();
if(p.length==1)
{
//perform type check and then invoke
Class<?> paramType = p[0].getType();
// Null can be assigned to any reference type.
if (args == null || paramType.isInstance(args) || (paramType.isPrimitive() && args != null && 
        ((paramType == int.class && args instanceof Integer) ||
         (paramType == long.class && args instanceof Long) ||
         (paramType == double.class && args instanceof Double) ||
         (paramType == float.class && args instanceof Float) ||
         (paramType == boolean.class && args instanceof Boolean) ||
         (paramType == char.class && args instanceof Character) ||
         (paramType == byte.class && args instanceof Byte) ||
         (paramType == short.class && args instanceof Short)))) 
    {
        result = targetMethod.invoke(instance, args);
    }
    else
    {
        System.out.println("Cannot assign argument to parameter type: " + paramType.getName());
    }
}//if parameter.length==1
}//if args != null
else
{
Parameter parameters[]=targetMethod.getParameters();
Object parameterValues[]=new Object[parameters.length];
boolean onlyOneJSON=false;
for(int i=0;i<parameters.length;i++)
{
Parameter p=parameters[i];
Object value=null;
if(p.isAnnotationPresent(RequestParameter.class))
{
RequestParameter rp=(RequestParameter)p.getAnnotation(RequestParameter.class);
String paramName=rp.value();
String val=request.getParameter(paramName);
Class type=p.getType();
// Handle conversion for all 8 primitives + String
                    if(type == String.class) value = val;
                    else if(type == int.class || type == Integer.class) value = (val!=null && !val.isEmpty())? Integer.parseInt(val) : 0;
                    else if(type == long.class || type == Long.class) value = (val!=null && !val.isEmpty())? Long.parseLong(val) : 0L;
                    else if(type == double.class || type == Double.class) value = (val!=null && !val.isEmpty())? Double.parseDouble(val) : 0.0;
                    else if(type == float.class || type == Float.class) value = (val!=null && !val.isEmpty())? Float.parseFloat(val) : 0.0f;
                    else if(type == boolean.class || type == Boolean.class) value = (val!=null && !val.isEmpty())? Boolean.parseBoolean(val) : false;
                    else if(type == char.class || type == Character.class) value = (val!=null && !val.isEmpty())? val.charAt(0) : '\0';
                    else if(type == short.class || type == Short.class) value = (val!=null && !val.isEmpty())? Short.parseShort(val) : (short)0;
                    else if(type == byte.class || type == Byte.class) value = (val!=null && !val.isEmpty())? Byte.parseByte(val) : (byte)0;
}//if @RequestParameter is present ends
else
{
Class type=p.getType();
if(type==ApplicationDirectory.class)
{
if(applicationDirectory==null)
{
File directory=new File(getServletContext().getRealPath(path));
applicationDirectory=new ApplicationDirectory(directory);
}
value=applicationDirectory;
}
else if(type==ApplicationScope.class)
{
if(applicationScope==null) applicationScope=new ApplicationScope(getServletContext());
value=applicationScope;
}
else if(type==SessionScope.class)
{
if(sessionScope==null) sessionScope=new SessionScope(request.getSession());
value=sessionScope;
}
else if(type==RequestScope.class)
{
if(requestScope==null) requestScope=new RequestScope(request);
value=requestScope;
}
//JSON Object Required
else
{
if(onlyOneJSON)
{
//More Than 1 JSON Objects Found 
//Generate Service Exception
//send 500 Error
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"More than 1 JSON requested in service");
throw new ServiceException("More than 1 JSON requested in service");
//return null;
}
onlyOneJSON=true;
Gson gson=new Gson();
try
{
System.out.println("JSON - "+jsonString);
System.out.println("type - "+type.getName());
value=gson.fromJson(jsonString,type);
}catch(Exception unableToConvert)
{
System.out.println(unableToConvert);
//Generate Service Exception
//send 500 Error
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"More than 1 JSON requested in service");
throw new ServiceException("We Cannot Convert JSON in Suiteble Format (No Matching Class Found)");
}
}//else part ends
}//check for Request/Session/Application Scope or Application Directory
parameterValues[i]=value;
}//for loop ends

//get parameters of targetMethod
// check if annotation is applied on them @RequestParameter
// Get all the things from request object and then do typecast as per types
// we will allow these 8 basic + 1 string type
//set values and call the method

result=targetMethod.invoke(instance,parameterValues);
}//else ends
return result;
}catch(InstantiationException instiationException)
{
System.out.println("Cannot Instantiate Object of the Class");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}catch(IllegalAccessException illegalAccessException)
{
System.out.println("Illegal Access of the Class");
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}catch(InvocationTargetException invocationTargetException)
{
System.out.println(targetClass.getName()+"Colleeesst"+targetMethod.getName());
System.out.println("Cannot Invoke this Method of the Class");
System.out.println(invocationTargetException.getMessage());
//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
return result;
}
}catch(IOException ioe)
{
System.out.println(ioe);
}
return result;
}//execute ends here
private void handleAutoWiring(Object instance,Class c,HttpServletRequest request)
{
ServletContext context=getServletContext();
for(Field field:c.getDeclaredFields())
{
if(field.isAnnotationPresent(AutoWired.class))
{
AutoWired aw=(AutoWired)field.getAnnotation(AutoWired.class);
String name=aw.name();
Object object;
object=request.getAttribute(name);
if(object==null)
{
HttpSession session=request.getSession();
object=session.getAttribute(name);
if(object==null) object=context.getAttribute(name);
}
if(object!=null && field.getType().isInstance(object))
{
String identityName=field.getName();
String methodName="set"+identityName.substring(0,1).toUpperCase()+identityName.substring(1);
try
{
Method m = c.getMethod(methodName, field.getType());
m.invoke(instance, object); // reflection handles type check
}catch(NoSuchMethodException e)
{
System.out.println("Setter not found for field: " + field.getName());
}catch(Exception e)
{
e.printStackTrace();
}
}//if object is not null
}//if annotation present
}//For Loop ends here
}//handleAutoWiring ends here

}//class ends here