package bobby;
import com.mg.webrock.annotation.*;
import com.mg.webrock.pojo.*;
@Path("/org")
@InjectApplicationScope
public class Organization
{
private ApplicationScope applicationScope;
public void setApplicationScope(ApplicationScope applicationScope)
{
System.out.println("Got The Application SCOPE");
this.applicationScope=applicationScope;
}
@OnStartup(priority=1)
public void init()
{
Stud s=new Stud();
s.setRollNumber(101);
s.setName("Amit");
System.out.println("SET The OBJECT iN Application Scope");
applicationScope.setAttribute("xyz",s);
}
@AutoWired(name="xyz")
private Stud stud;
public void setStud(Stud stud)
{
System.out.println("Got The Stud Object from Applciation SCOPE");
this.stud=stud;
}
public Stud getStud()
{
return this.stud;
}
@Path("/get")
public void get()
{
System.out.println("I Am In Get Method of Organization");
System.out.println("Student Roll Number - "+stud.getRollNumber());
System.out.println("Student Name - "+stud.getName());
}
@Path("/testing")
public void somemethod(@RequestParameter("pqr") int x,@RequestParameter("xyz") String name,ApplicationScope ap)
{
System.out.println("int x = "+x);
System.out.println("String name = "+name);
Stud s=(Stud)ap.getAttribute("xyz");
if(s==null) System.out.println("Stuednt is null");
else
{
System.out.println("Student Roll Number - "+s.getRollNumber());
System.out.println("Student Name - "+s.getName());
}
}
@InjectRequestParameter("summer")
private int summer;
public void setSummer(int sm)
{
System.out.println("Setter got called");
summer=sm;
}
@Path("/summer")
public void summ()
{
System.out.println("summer - "+this.summer);
}

//Testing JSON Object
@Path("/customer")
public void customDetails(Customer c)
{
System.out.println("Name - "+c.name);
System.out.println("Age - "+c.age);
}
}