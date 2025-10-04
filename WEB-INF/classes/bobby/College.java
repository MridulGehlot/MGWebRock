package bobby;
import com.mg.webrock.annotation.*;
import com.mg.webrock.pojo.*;
@Path("/acro")
@InjectRequestScope
public class College
{
private RequestScope requestScope;
public void setRequestScope(RequestScope requestScope)
{
System.out.println("Setter Got Called");
this.requestScope=requestScope;
}
@Path("/add")
@Forward("/acro/get")
public void add()
{
requestScope.setAttribute("name","MGCompanies");
System.out.println("Success Fully Invoked add Method");
}
@Path("/get")
@Forward("/acro/m2")
public void get()
{
String name="DUMMY";
name=(String)requestScope.getAttribute("name");
System.out.println("Successfull Fetched name - "+name);
System.out.println("Success Fully Invoked get Method");
}
@Path("/m2")
public String m2()
{
System.out.println("I am In Forwarded Method of College.java");
return "Acro M2";
}
@OnStartup(priority=1)
public void m3()
{
System.out.println("I am In M3 Method");
}
@OnStartup(priority=2)
public void m4()
{
System.out.println("I am In M4 Method");
}
}