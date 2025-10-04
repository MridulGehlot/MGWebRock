package com.mg.webrock;
import javax.servlet.*;
import javax.servlet.http.*;
import com.mg.webrock.pojo.*;
import com.mg.webrock.model.*;
import com.mg.webrock.annotation.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
record ItemRecord(int priority,Service s) implements Comparable<ItemRecord>
{
@Override
public int compareTo(ItemRecord other)
{
return Integer.compare(this.priority,other.priority);
}
}
public class MGWebRockStarter extends HttpServlet
{
private static WebRockModel model;
private static ArrayList<ItemRecord> startupMethods;
public MGWebRockStarter()
{
model=new WebRockModel();
startupMethods=new ArrayList<>();
}
public void init(ServletConfig config) throws ServletException
{
super.init(config);
ServletContext context=getServletContext();
String servicePackagePrefix = context.getInitParameter("SERVICE_PACKAGE_PREFIX");
/*
if (servicePackagePrefix == null || servicePackagePrefix.trim().isEmpty()) {
throw new ServletException("SERVICE_PACKAGE_PREFIX context parameter is not set or is empty.");
}
*/
System.out.println("MGWebRockStarter: Scanning package: " + servicePackagePrefix);
//SETUP js Folder Structure for Automating Task
setupJS(context);
//SETUP js Folder Structure for Automating Task ENDS HERE
traverse(context.getRealPath("")+File.separator+"WEB-INF"+File.separator+"classes"+File.separator+servicePackagePrefix,context);
context.setAttribute("urlMappings",model.getUrlMappings());
if(startupMethods.size()>0) invokeStarterMethods(startupMethods,context);
}
private static void traverse(String folder,ServletContext context)
{
File f=new File(folder);
File []files=f.listFiles();
for(File ff:files)
{
if(ff.isDirectory()) traverse(ff.getAbsolutePath(),context);
else
{
if(ff.getAbsolutePath().endsWith(".class")) process(ff,context);
}
}
}
private static void process(File f,ServletContext context)
{
System.out.println("_____PROCESSING__FILES_____");
String absolutePath=f.getAbsolutePath();
System.out.println(absolutePath);
String basePackagePath=context.getRealPath("")+File.separator+"WEB-INF"+File.separator+"classes"; 
String relativePath=absolutePath.substring(basePackagePath.length()); 
/*
if (relativePath.startsWith("\\") || relativePath.startsWith("/")) {
    relativePath = relativePath.substring(1); // remove leading slash/backslash
}
*/
String classNameWithSlashes=relativePath.substring(0,relativePath.length()-".class".length());
String classNameWithDots=classNameWithSlashes.replace('\\','.');
System.out.println("Attempting to load class: " + classNameWithDots); 

String className=classNameWithDots;

boolean toGenerate=check(className);
if(toGenerate) generateJS(className,context);

String pathOnClass=null;
String pathOnMethod=null;
String forwardTo=null;
String globalAllowedMethod=null;
boolean isGetAllowed=false;
boolean isPostAllowed=false;
boolean runOnStartup=false;
int priority=0;
boolean injectApplicationDirectory=false;
boolean injectApplicationScope=false;
boolean injectSessionScope=false;
boolean injectRequestScope=false;
boolean isAutoWired=false;
boolean injectRequestParameter=false;
boolean isSecuredAccess=false;
boolean isClassSecured=false;
String checkPost=null;
String guard=null;
Service service=null;
try
{
Class c=Class.forName(className);
//Class
if(c.isAnnotationPresent(SecuredAccess.class))
{
isClassSecured=true;
isSecuredAccess=true;
SecuredAccess securedAccess=(SecuredAccess)c.getAnnotation(SecuredAccess.class);
checkPost=securedAccess.checkPost();
guard=securedAccess.guard();
}
if(c.isAnnotationPresent(InjectApplicationDirectory.class)) injectApplicationDirectory=true;
if(c.isAnnotationPresent(InjectApplicationScope.class)) injectApplicationScope=true;
if(c.isAnnotationPresent(InjectSessionScope.class)) injectSessionScope=true;
if(c.isAnnotationPresent(InjectRequestScope.class)) injectRequestScope=true;
if(c.isAnnotationPresent(Path.class))
{
Path a=(Path)c.getAnnotation(Path.class);
pathOnClass=a.value();
if(c.isAnnotationPresent(Get.class)) 
{
isGetAllowed=true;
globalAllowedMethod="GET";
}
if(c.isAnnotationPresent(Post.class))
{
if(isGetAllowed!=false)
{
System.out.println("Error A Class Cannot Have Multiple Mapping Types : "+c.getName());
return;
}
isPostAllowed=true;
globalAllowedMethod="POST";
}
}
else return;
//Fields
for(Field field:c.getDeclaredFields())
{
if(field.isAnnotationPresent(AutoWired.class)) isAutoWired=true;
if(field.isAnnotationPresent(InjectRequestParameter.class)) injectRequestParameter=true;
}//Field Loop ends here
//Methods
Method []methods=c.getDeclaredMethods();
for(Method m:methods)
{
//Reset All The Fields for Next Iteration
forwardTo=null;
isGetAllowed=false;
isPostAllowed=false;
runOnStartup=false;
priority=0;
//Reset DONE
//local security variables;
boolean secure=false;
String securityClass=null;
String securityMethod=null;
if(m.isAnnotationPresent(SecuredAccess.class)) 
{
secure=true;
SecuredAccess securedAccess=(SecuredAccess)c.getAnnotation(SecuredAccess.class);
securityClass=securedAccess.checkPost();
securityMethod=securedAccess.guard();
}
else if(isClassSecured)
{
secure=isSecuredAccess;
securityClass=checkPost;
securityMethod=guard;
}
//security ends here
if(m.isAnnotationPresent(Get.class) && m.isAnnotationPresent(Post.class))
{
System.out.println("Error : Multiple Annotations Are Not Allowed on a Method");
return;
}
if (m.isAnnotationPresent(Get.class)) isGetAllowed=true;
else if(m.isAnnotationPresent(Post.class)) isPostAllowed=true;
else
{
if(globalAllowedMethod!=null)
{
if(globalAllowedMethod.equals("GET")) isGetAllowed=true;
else isPostAllowed=true;
}
else
{
isGetAllowed=true;
isPostAllowed=true;
}
}
if(m.isAnnotationPresent(Forward.class))
{
Forward forward=(Forward)m.getAnnotation(Forward.class);
forwardTo=forward.value();
}

service=new Service();
service.setServiceClass(c);
service.setServiceMethod(m);
service.setIsGetAllowed(isGetAllowed);
service.setIsPostAllowed(isPostAllowed);
service.setInjectApplicationDirectory(injectApplicationDirectory);
service.setInjectApplicationScope(injectApplicationScope);
service.setInjectSessionScope(injectSessionScope);
service.setInjectRequestScope(injectRequestScope);
service.setInjectRequestParameter(injectRequestParameter);
service.setIsAutoWired(isAutoWired);
service.setForwardTo(forwardTo);
service.setIsSecuredAccess(secure);
service.setCheckPost(null);
service.setGuard(null);
if(secure)
{
try
{
Class sClass=Class.forName(securityClass);
service.setCheckPost(sClass);
for(Method mm:sClass.getDeclaredMethods())
{
if(mm.getName().equals(securityMethod))
{
service.setGuard(mm);
break;
}
}
}catch(Exception errorInSecurityClass)
{
System.out.println("Error in Security Class (Unable to Load)");
System.out.println("Cause - "+errorInSecurityClass);
}
}
if(m.isAnnotationPresent(Path.class))
{
Path d=(Path)m.getAnnotation(Path.class);
if(d.value().length()>0)
{
pathOnMethod=d.value();
service.setPath(pathOnClass+pathOnMethod);
//if(forwardTo!=null) service.setForwardTo(pathOnClass+forwardTo);
//else
System.out.println("Inserted - "+pathOnClass+pathOnMethod);
model.insert(pathOnClass+pathOnMethod,service);
}// if length of path is >0
}// if path annotation present

//To Check Startup Services
if(m.isAnnotationPresent(OnStartup.class))
{
if(!(m.getReturnType().getName()).equals("void"))
{
System.out.println("On Startup Method Should Have Void as a Return Type");
return;
}
if(m.getParameters().length>0)
{
System.out.println("On Startup Method Should Accept No Parameters");
return;
}
runOnStartup=true;
OnStartup os=(OnStartup)m.getAnnotation(OnStartup.class);
priority=os.priority();
service.setRunOnStartup(runOnStartup);
service.setPriority(priority);
startupMethods.add(new ItemRecord(priority,service));
}//on Startup service ends here
}// Method Loop Ends Here
}catch(Exception e)
{
System.out.println("Unable TO LOAD Class");
System.out.println(e);
}
}
private static void invokeStarterMethods(ArrayList<ItemRecord> startupMethods,ServletContext servletContext)
{
Collections.sort(startupMethods);
try
{
Service service;
Class c;
Method m;
for(ItemRecord item:startupMethods)
{
service=item.s();
c=service.getServiceClass();
m=service.getServiceMethod();
Object instance=c.newInstance();
//Check For Injection
try
{
//Injection for startup phase
if(service.getInjectApplicationDirectory())
{
Method setAppDir=c.getMethod("setApplicationDirectory",ApplicationDirectory.class);
setAppDir.invoke(instance,new ApplicationDirectory(new File(servletContext.getRealPath(""))));
}
if(service.getInjectApplicationScope())
{
Method setAppScope=c.getMethod("setApplicationScope",ApplicationScope.class);
setAppScope.invoke(instance,new ApplicationScope(servletContext));
}
//Skip these because no request/session at startup
if(service.getInjectSessionScope() || service.getInjectRequestScope()) {
System.out.println("Warning: SessionScope/RequestScope injection not supported at startup for " + c.getName());
}
}catch(NoSuchMethodException nsme)
{
System.out.println("For All Inject Classes You should write Setter/Getter Functions");
System.out.println(nsme);
}
//Injection Ends Here
m.invoke(instance);
}
}catch(InstantiationException instiationException)
{
System.out.println("Cannot Instantiate Object of the Class");
return;
}catch(IllegalAccessException illegalAccessException)
{
System.out.println("Illegal Access of the Class");
return;
}catch(InvocationTargetException invocationTargetException)
{
System.out.println("Cannot Invoke this Method of the Class");
System.out.println(invocationTargetException.getMessage());
return;
}
}//invoke ends here
private static void setupJS(ServletContext context)
{
try
{
String rootPath=context.getRealPath("");
String folderPath=rootPath+File.separator+"WEB-INF"+File.separator+"js";
String JSFile=context.getInitParameter("JSFile");
String filePath=folderPath+File.separator+JSFile;

File folder=new File(folderPath);
if(!folder.exists())
{
boolean folderCreated=folder.mkdirs();
if(!folderCreated) throw new IOException("Unable to Create Directory Structure - "+folderPath);
}

File jsFile=new File(filePath);
if(jsFile.exists())
{
boolean deleted=jsFile.delete();
if(!deleted) throw new IOException("Unable to Delelte Old JS File - "+filePath);
}

boolean created=jsFile.createNewFile();
if(!created) throw new IOException("Unable to Create new JS File - "+filePath);
}catch(IOException ioe)
{
System.out.println(ioe);
}
}//setupJS ends here
private static boolean check(String className)
{
if(className.indexOf("DAO")!=-1) return false;
try
{
Class c=Class.forName(className);
if(c.isAnnotationPresent(Path.class)) return true;
Method methods[]=c.getDeclaredMethods();
if(methods.length==0) return true;
for(Method m:methods)
{
if(m.getName().startsWith("set") || m.getName().startsWith("get")) return true;
}
}catch(Exception e)
{
System.out.println(e);
}
return false;
}
private static void generateJS(String className,ServletContext context)
{
try
{
Class c=Class.forName(className);
StringBuilder jsString=new StringBuilder("class ");
if(c.isAnnotationPresent(Path.class))
{
Path annotationOnClass=(Path)c.getAnnotation(Path.class);
jsString.append(c.getSimpleName()+"\r\n"+"{"+"\r\n");
for(Method m:c.getDeclaredMethods())
{
if(m.isAnnotationPresent(Path.class))
{
Path annotationOnMethod=(Path)m.getAnnotation(Path.class);
jsString.append(m.getName()+"(");
Parameter parameters[]=m.getParameters();
int i=1;
StringBuilder queryString=new StringBuilder("'");
StringBuilder name=new StringBuilder("");
for(Parameter p:parameters)
{
if(p.isAnnotationPresent(RequestParameter.class))
{
RequestParameter rp=(RequestParameter)p.getAnnotation(RequestParameter.class);
if(i==1)
{
queryString=new StringBuilder("");
queryString.append("?"+rp.value()+"='+"+p.getName());
}
else queryString.append("+'&"+rp.value()+"='+"+p.getName());
}
jsString.append(p.getName());
name=new StringBuilder(p.getName());
if(i<=parameters.length-1) jsString.append(",");
i++;
}//for loop of parameters
jsString.append(")\r\n{");

//content of method
jsString.append("\r\nreturn new Promise(function(resolve,reject){\r\n");
jsString.append("fetch('http://localhost:8080/MGWebRock"+context.getInitParameter("BaseURL")+annotationOnClass.value()+annotationOnMethod.value()+queryString.toString());
if(m.isAnnotationPresent(Post.class))
{
if(parameters.length==0)
{
jsString.append(",{'method':'POST','headers':{'Content-Type':'application/json'}})\r\n");
}
else
{
jsString.append(",{'method':'POST','headers':{'Content-Type':'application/json'},'body':JSON.stringify("+name+")})\r\n");
}
}
else jsString.append(")\r\n");
Class returnType=m.getReturnType();
if(returnType.equals(void.class))
{
jsString.append(".then(response => response.text())\r\n.then(data => {\r\nif(data!=='OK')\r\n{\r\nreject();\r\nreturn;\r\n}\r\nresolve();\r\n})\r\n.catch(error => console.log(error))\r\n});\r\n");
}
else if(returnType.equals(String.class))
{
jsString.append(".then(response => response.text())\r\n.then(data => {resolve(data);})\r\n.catch(error => console.log(error))\r\n});\r\n");
}
else
{
jsString.append(".then(response => {if(!response.ok){throw new Error(`HTTP error! status: ${response.status}`);}return response.json()})\r\n.then(data => {resolve(data);})\r\n.catch(error => console.log(error))\r\n});\r\n");
}
jsString.append("}\r\n");
//content of method ends here
}//if Path present on methods ends here
}//for loop ends
jsString.append("}");
}//if ends for a service class
//else it is A POJO class
else
{
jsString.append(c.getSimpleName()+"\r\n"+"{"+"\r\n");
StringBuilder contentOfConstructor=new StringBuilder("");
jsString.append("constructor(");
int i=1;
Field fields[]=c.getDeclaredFields();
for(Field f:fields)
{
jsString.append(f.getName());
contentOfConstructor.append("this."+f.getName()+"="+f.getName()+";\r\n");
if(i<=fields.length-1) jsString.append(",");
i++;
}
jsString.append(")\r\n{\r\n"+contentOfConstructor+"}\r\n}\r\n");
}//else ends here

//write everything in file in append mode
String rootPath=context.getRealPath("");
String folderPath=rootPath+File.separator+"WEB-INF"+File.separator+"js";
String JSFile=context.getInitParameter("JSFile");
String filePath=null;
if(JSFile!=null) filePath = folderPath + File.separator + JSFile;
else filePath = folderPath + File.separator + c.getSimpleName();
File jsFile=new File(filePath);
if(!jsFile.exists())
{
if(!jsFile.createNewFile())
{
throw new IOException("Unable to create file: " + filePath);
}
}
try(BufferedWriter bw = new BufferedWriter(new FileWriter(jsFile, true)))
{
bw.write(jsString.toString());
bw.newLine();
}
}catch(Exception e)
{
System.out.println(e);
}
}//generateJS ends here
}//class ends here