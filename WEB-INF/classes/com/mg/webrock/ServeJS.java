package com.mg.webrock;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class ServeJS extends HttpServlet
{
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
doGet(request,response);
}
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
try
{
String name=request.getParameter("name");
PrintWriter pw=response.getWriter();
response.setContentType("text/javascript");
ServletContext servletContext=getServletContext();
File file=null;
if(name==null)
{
file=new File(servletContext.getRealPath("")+File.separator+"WEB-INF"+File.separator+"js"+File.separator+servletContext.getInitParameter("JSFile"));
}
else
{
file=new File(servletContext.getRealPath("")+File.separator+"WEB-INF"+File.separator+"js"+File.separator+name);
}
RandomAccessFile raf=new RandomAccessFile(file,"r");
while(raf.getFilePointer()<raf.length())
{
pw.println(raf.readLine());
}
raf.close();
pw.flush();
}catch(Exception e)
{
System.out.println(e); //remove after testing
}
}
}