package bobby.module1;
public class Student
{
public int id;
public String name;
public String gender;
public Student()
{
this.id=0;
this.name="";
this.gender="";
}
public void setId(int id)
{
this.id=id;
}
public int getId()
{
return this.id;
}
public void setName(java.lang.String name)
{
this.name=name;
}
public java.lang.String getName()
{
return this.name;
}
public void setGender(java.lang.String gender)
{
this.gender=gender;
}
public java.lang.String getGender()
{
return this.gender;
}
}