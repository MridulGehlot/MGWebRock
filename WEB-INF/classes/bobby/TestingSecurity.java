package bobby;
import com.mg.webrock.annotation.*;
import com.mg.webrock.pojo.*;
@SecuredAccess(checkPost="bobby.Checkpost",guard="guard")
@Path("/secure")
@InjectApplicationScope
public class TestingSecurity
{
private ApplicationScope as;
public void setApplicationScope(ApplicationScope as)
{
this.as=as;
System.out.println("Got The Application Scope");
}
@Path("/service")
public void doSomething()
{
System.out.println("SuccessFully Invoked a Secured Service");
}
@OnStartup(priority=1)
public void runner()
{
if(as==null) System.out.println("application scope is null");
as.setAttribute("testingSecurity","MGCompanies");
System.out.println("Successfully set up things is application scope");
}
}