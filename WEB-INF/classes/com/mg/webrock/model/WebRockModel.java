package com.mg.webrock.model;
import java.util.*;
import com.mg.webrock.pojo.*;
public class WebRockModel
{
private Map<String,Service> urlMappings;
public WebRockModel()
{
this.urlMappings=new HashMap<>();
}
public void setUrlMappings(java.util.Map urlMappings)
{
this.urlMappings=urlMappings;
}
public java.util.Map getUrlMappings()
{
return this.urlMappings;
}
public void insert(String path,Service service)
{
this.urlMappings.put(path,service);
}
public Service get(String path)
{
return this.urlMappings.get(path);
}
}