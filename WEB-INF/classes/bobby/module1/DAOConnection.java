package bobby.module1;
import java.sql.*;
public class DAOConnection
{
public static Connection getConnection()
{
Connection c=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
c=DriverManager.getConnection("jdbc:mysql://localhost:3307/newdb","newUser","newUser");
}catch(Exception e)
{
System.out.println(e);
}
return c;
}
}