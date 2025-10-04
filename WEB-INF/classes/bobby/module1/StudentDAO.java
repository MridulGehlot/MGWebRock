package bobby.module1;
import java.util.*;
import java.sql.*;
public class StudentDAO
{
public void add(Student s)
{
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("insert into student(name,gender) values(?,?)");
ps.setString(1,s.getName());
ps.setString(2,s.getGender());
ps.executeUpdate();
ps.close();
c.close();
}catch(Exception e)
{
System.out.println(e);
}
}
public void update(Student s)
{
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("update student set name=?,gender=? where id=?");
ps.setString(1,s.getName());
ps.setString(2,s.getGender());
ps.setInt(3,s.getId());
ps.executeUpdate();
ps.close();
c.close();
}catch(Exception e)
{
System.out.println(e);
}
}
public void delete(int id)
{
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("delete from student where id=?");
ps.setInt(1,id);
ps.executeUpdate();
ps.close();
c.close();
}catch(Exception e)
{
System.out.println(e);
}
}
public Student getById(int id)
{
Student s=new Student();
s.setId(id);
try
{
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("select name,gender from student where id=?");
ps.setInt(1,id);
ResultSet rs=ps.executeQuery();
if(rs.next())
{
s.setName(rs.getString("name").trim());
s.setGender(rs.getString("gender").trim());
}
rs.close();
ps.close();
c.close();
}catch(Exception e)
{
System.out.println(e);
}
return s;
}
public List<Student> getAll()
{
List<Student> students=new ArrayList<>();
try
{
Student s;
Connection c=DAOConnection.getConnection();
PreparedStatement ps=c.prepareStatement("select * from student");
ResultSet rs=ps.executeQuery();
while(rs.next())
{
s=new Student();
s.setId(rs.getInt("id"));
s.setName(rs.getString("name").trim());
s.setGender(rs.getString("gender").trim());
students.add(s);
}
rs.close();
ps.close();
c.close();
}catch(Exception e)
{
System.out.println(e);
}
return students;
}
}