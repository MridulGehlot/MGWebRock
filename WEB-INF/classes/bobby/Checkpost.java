package bobby;
import com.mg.webrock.exceptions.*;
import com.mg.webrock.pojo.*;
public class Checkpost
{
public void guard(ApplicationScope as) throws ServiceException
{
String name=(String)as.getAttribute("testingSecurity");
System.out.println(name);
System.out.println("I Am Gurad Method Of Checkpost Class");
throw new ServiceException("I am not allowing you to invoke the service");
}
}