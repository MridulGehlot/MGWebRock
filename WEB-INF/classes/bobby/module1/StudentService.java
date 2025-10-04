package bobby.module1;
import com.mg.webrock.annotation.*;
import java.util.*;
import com.mg.webrock.pojo.*;
import java.sql.*;
@Path("/student")
@InjectApplicationScope
public class StudentService
{
private ApplicationScope applicationScope;
public void setApplicationScope(ApplicationScope as)
{
this.applicationScope=as;
}
@OnStartup(priority=1)
public void createDAO()
{
this.applicationScope.setAttribute("StudentDAO",new StudentDAO());
}
@AutoWired(name="StudentDAO")
private StudentDAO studentDAO;
public void setStudentDAO(StudentDAO s)
{
this.studentDAO=s;
}
@Post
@Path("/add")
public void add(Student s)
{
studentDAO.add(s);
}
@Post
@Path("/update")
public void update(Student s)
{
studentDAO.update(s);
}
@Path("/delete")
public void delete(@RequestParameter("id") int id)
{
studentDAO.delete(id);
}
@Path("/getById")
public Student getById(@RequestParameter("id") int id)
{
return studentDAO.getById(id);
}
@Path("/getAll")
public List<Student> getAll()
{
return studentDAO.getAll();
}
}