package bobby;
import com.mg.webrock.annotation.*;
@Path("/gyansagar")
public class School
{
@Post
@Path("/add")
@Forward("/error.jsp")
public void add()
{
System.out.println("Success Fully Invoked add Method School.java");
}
@Path("/get")
@Forward("/gyansagar/m2")
public void get()
{
System.out.println("Success Fully Invoked get Method of School.java");
}
@Path("/m2")
public void m2(@RequestParameter("xyz") int xyz,@RequestParameter("pqr") int pqr)
{
System.out.println("M2 of School.java");
System.out.println("I am In Forwarded Method");
}
@Path("/greet")
public Customer greet()
{
Customer c=new Customer();
c.name="Amit";
c.age=55;
return c;
}
}