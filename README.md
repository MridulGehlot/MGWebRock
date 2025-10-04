# 🧠 MGWebRock Framework

### A lightweight, annotation-driven Java web framework built to simplify backend service creation — inspired by JAX-RS and Spring MVC.

---

## ⚙️ Overview

**MGWebRock** lets developers build modular REST-like services using plain Java classes and annotations — without needing complex XML or Spring configurations.  
It supports dependency injection, request/response scope management, security guards, and auto-generated PDF documentation of all services.

Built and tested with **JDK 22**.

---

## 📦 Package Structure Overview

The MGWebRock framework is modular and neatly divided into packages.  
Each package has a specific purpose as described below:

---

### 🧩 `com.mg.webrock.annotation.*`

This package contains **all the custom annotations** used by the framework.

**Included Annotations:**
- `@Path` — defines base or sub-path for services.
- `@Get`, `@Post` — specify HTTP methods.
- `@Forward` — forwards control to another service path.
- `@InjectApplicationDirectory`, `@InjectApplicationScope`, `@InjectSessionScope`, `@InjectRequestScope` — used for dependency injection.
- `@InjectRequestParameter` — injects request parameters.
- `@AutoWired` — enables field-level dependency injection.
- `@OnStartup` — runs methods on startup.
- `@SecuredAccess` — adds security verification.

These annotations are lightweight and processed via Java Reflection at runtime by `MGWebRockStarter`.

---

### ⚠️ `com.mg.webrock.exceptions.*`

Contains framework-specific exception classes.

- **`ServiceException`**  
  A custom exception that can be thrown inside your service methods when you want to indicate business logic errors or invalid conditions.  
  Example:
  ```java
  if(studentId <= 0) {
      throw new ServiceException("Invalid student ID");
  }

## 🧱 com.mg.webrock.pojo.*

Contains all core helper classes used internally and for dependency injection.
| Class                  | Purpose                                                                                                                         |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| `ApplicationDirectory` | Provides access to the base directory of the web application. Useful for reading/writing files relative to the deployment root. |
| `ApplicationScope`     | Wrapper around `ServletContext`. Used to store global attributes accessible across the entire web app.                          |
| `SessionScope`         | Wrapper around `HttpSession`. Used to store user-specific session data.                                                         |
| `RequestScope`         | Wrapper around `HttpServletRequest`. Used to manage per-request attributes and parameters.                                      |
| `Service`              | Internal data structure representing a discovered service — stores annotations, class metadata, and method mappings.            |

## Directory Snapshot:
```makefile
D:\tomcat9\webapps\MGWebRock\WEB-INF\classes\com\mg\webrock\pojo
│
├── ApplicationDirectory.java
├── ApplicationScope.java
├── RequestScope.java
├── SessionScope.java
└── Service.java
```


---

## ⚙️ Important Configuration Notes

### 1️⃣ `SERVICE_PACKAGE_PREFIX` (Root Package)
This parameter defines the **root package** inside `WEB-INF/classes` where your service classes are located.

```xml
<context-param>
    <param-name>SERVICE_PACKAGE_PREFIX</param-name>
    <param-value>example</param-value>
</context-param>
```

## 📁 Example Folder Structure:
```makefile
D:\tomcat9\webapps\testing\WEB-INF\classes\example\
```
All your annotated Java files (e.g. SchoolService.java, StudentService.java) should be inside this root package folder.

## 2️⃣ BaseURL (Primary Service Mapping)

This defines the base URL prefix for all your service endpoints.
```xml
<context-param>
    <param-name>BaseURL</param-name>
    <param-value>/api</param-value>
</context-param>
```
💡 Every request to your framework services will start with this prefix.
For example:
```bash
http://localhost:8080/testing/api/schoolService/add
http://localhost:8080/testing/api/schoolService/getAll
```

## 3️⃣ MGWebRock Servlet Mapping

Your main servlet mapping must match the same base path as above.
```xml
<servlet-mapping>
    <servlet-name>MGWebRock</servlet-name>
    <url-pattern>/api/*</url-pattern>
</servlet-mapping>
```
This ensures all incoming URLs with /schoolService/* are handled by the MGWebRock framework.

## 4️⃣ Forwarding Notes

When using the @Forward annotation, always use the full path, including the base URL.
### ✅ Correct:
```java
@Forward("/school/add")
```
### ❌ Incorrect:
```java
@Forward("/add")
```

---

## 🧩 Annotation Reference

| Annotation | Target | Description |
|-------------|---------|-------------|
| `@Path("/path")` | Class / Method | Defines a base URL path for a service class or method. |
| `@Get` | Method | Marks a service method as accessible via HTTP GET. |
| `@Post` | Method | Marks a service method as accessible via HTTP POST. |
| `@Forward("/path")` | Method | Automatically forwards control to another service path after execution. |
| `@InjectApplicationDirectory` | Class | Injects the base application directory (using `setApplicationDirectory(ApplicationDirectory ad)`). |
| `@InjectApplicationScope` | Class | Injects global application scope (using `setApplicationScope(ApplicationScope scope)`). |
| `@InjectSessionScope` | Class | Injects session scope (using `setSessionScope(SessionScope session)`). |
| `@InjectRequestScope` | Class | Injects request scope (using `setRequestScope(RequestScope req)`). |
| `@InjectRequestParameter` | Field | Automatically injects request parameters into that field. |
| `@AutoWired(name="xyz")` | Field | Automatically injects dependency of another service (requires `set<FieldName>()` setter). |
| `@SecuredAccess(checkPost="ClassName", guard="methodName")` | Class / Method | Adds a security layer that checks before execution. |
| `@OnStartup(priority=1)` | Method | Marks a method to run automatically at framework startup. Must be `void` and accept no parameters. |

---

## 🧰 Dependency Injection Rules

### 🔹 Application Directory Injection
```java
@InjectApplicationDirectory
public class FileService {
    private ApplicationDirectory appDir;
    public void setApplicationDirectory(ApplicationDirectory ad) {
        this.appDir = ad;
    }
}
```

### 🔹 Application Scope Injection
```java
@InjectApplicationScope
public class GlobalService {
    private ApplicationScope scope;
    public void setApplicationScope(ApplicationScope s) {
        this.scope = s;
    }
}
```

### 🔹 Session Scope Injection
```java
@InjectSessionScope
public class LoginManager {
    private SessionScope session;
    public void setSessionScope(SessionScope s) {
        this.session = s;
    }
}
```

### 🔹 Request Scope Injection
```java
@InjectRequestScope
public class RequestHandler {
    private RequestScope request;
    public void setRequestScope(RequestScope r) {
        this.request = r;
    }
}
```

### 🔹 AutoWired Field Injection
```java
@Path("/student")
public class StudentService {
    //Should be Available in
    //Request/Session/Application Scope
    @AutoWired(name="student")
    private StudentDAO studentDAO;

    // Required setter for AutoWired to work
    public void setStudentDAO(StudentDAO sdao) {
        this.studentDAO = sdao;
    }
}
```

---

## 🧾 Example Service

```java
import com.mg.webrock.annotation.*;
import com.mg.webrock.pojo.*
@Path("/school")
@InjectRequestScope
public class SchoolService {

    private RequestScope req;
    public void setRequestScope(RequestScope rq)
    {
        this.req=rq;
    }

    @Path("/add")
    @Post
    public String add() {
        return "Student Added";
    }

    @Path("/get")
    @Get
    @Forward("/school/add")
    public String get() {
        return "Fetching student data";
    }
}
```

---

## ⚙️ Required `web.xml` Configuration

Add the following entries in your `WEB-INF/web.xml` file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
  version="4.0"
  metadata-complete="true">

    <description>
      A J2EE Complaint Web Services Framework
    </description>
    <display-name>MGWebRock</display-name>

    <request-character-encoding>UTF-8</request-character-encoding>

    <context-param>
      <param-name>SERVICE_PACKAGE_PREFIX</param-name>
      <param-value>bobby</param-value>
    </context-param>

    <context-param>
      <param-name>JSFile</param-name>
      <param-value>main.js</param-value>
    </context-param>

    <context-param>
      <param-name>BaseURL</param-name>
      <param-value>/schoolService</param-value>
    </context-param>

    <servlet>
      <servlet-name>MGWebRockStarter</servlet-name>
      <servlet-class>com.mg.webrock.MGWebRockStarter</servlet-class>
      <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet>
      <servlet-name>MGWebRock</servlet-name>
      <servlet-class>com.mg.webrock.MGWebRock</servlet-class>
    </servlet>

    <servlet-mapping>
      <servlet-name>MGWebRock</servlet-name>
      <url-pattern>/schoolService/*</url-pattern>
    </servlet-mapping>

    <servlet>
      <servlet-name>ServeJS</servlet-name>
      <servlet-class>com.mg.webrock.ServeJS</servlet-class>
    </servlet>

    <servlet-mapping>
      <servlet-name>ServeJS</servlet-name>
      <url-pattern>/serveJS</url-pattern>
    </servlet-mapping>
</web-app>
```

> **Note:**  
> If `JSFile` is not mentioned, JS files will be generated automatically with class names inside `WEB-INF/js/`.

---

## 🧮 How to Compile User Services

After placing your **`mgwebrock.jar`** in  
`D:\tomcat9\webapps\testingMGWebRock\WEB-INF\lib\`

run the following command inside your `classes/com` folder:

```bash
D:\tomcat9\webapps\testingMGWebRock\WEB-INF\classes\com>javac -classpath ..\..\lib\mgwebrock.jar;. *.java
```

✅ This compiles all user-defined service classes using the MGWebRock framework.

---

## 📄 Generating the Service Documentation PDF

To automatically generate a **Service Documentation PDF** containing all annotated services and injection details, run:

```bash
D:\tomcat9\webapps\testingMGWebRock>java -classpath WEB-INF\lib\mgwebrock.jar;d:\itext\itextpdf-5.5.13.4.jar;d:\tomcat9\webapps\testingMGWebRock\WEB-INF\classes;. com.mg.webrock.ServicesDoc D:\tomcat9\webapps\testingMGWebRock\WEB-INF\classes
```

Output Example:
```
PDF created successfully at D:\tomcat9\webapps\testingMGWebRock\ss.pdf
```

---

## 🪶 PDF Features

- Automatically scans all service classes marked with `@Path`.
- Generates a table of methods, paths, return types, and security info.
- Lists all injections and dependencies.
- Adds a neat **Error Summary** section for unprocessed classes.
- Appends developer signature:
  ```
  Software By : Mridul Gehlot (CEO @ MGCompanies)
  ```

---

## 💡 Tips for Developers

- Always ensure `@Path` is applied on both class and method for service mapping.  
- Each injected field must have a proper setter (e.g., `setApplicationScope` for `@InjectApplicationScope`).  
- If using `@AutoWired(name="xyz")`, the field name’s first letter should be capitalized in the setter name.  
  Example:  
  ```java
  @AutoWired private StudentDAO studentDAO;
  public void setStudentDAO(StudentDAO sdao) {}
  ```
- Use `@SecuredAccess` for login-required endpoints.  
- `@OnStartup` methods help initialize configuration or preload data.

---

## 🚀 Distribution

Include the following files when distributing **MGWebRock**:
- `mgwebrock.jar` → Core framework + annotations  
- `itextpdf-5.5.13.4.jar` → Required for PDF generation  

You can find both in the **downloadables** folder.

Example folder structure:
```
├── WEB-INF
    ├── classes
    │   └── com
    │       └── YourService.java
    ├── lib
    │   ├── mgwebrock.jar
    │   └── itextpdf-5.5.13.4.jar
```

---

## 🧑‍💻 Author

**Mridul Gehlot**  
_Founder & CEO @ MGCompanies_

> “Write clean code. Automate your backend. Let MGWebRock handle the rest.”
