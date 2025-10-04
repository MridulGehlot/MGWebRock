# 🧠 MGWebRock Framework

### A lightweight, annotation-driven Java web framework built to simplify backend service creation — inspired by JAX-RS and Spring MVC.

---

## ⚙️ Overview

**MGWebRock** lets developers build modular REST-like services using plain Java classes and annotations — without needing complex XML or Spring configurations.  
It supports dependency injection, request/response scope management, security guards, and auto-generated PDF documentation of all services.

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
| `@AutoWired` | Field | Automatically injects dependency of another service (requires `set<FieldName>()` setter). |
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
    @AutoWired
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

@Path("/school")
public class SchoolService {

    @InjectRequestScope
    private RequestScope req;

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
- If using `@AutoWired`, the field name’s first letter should be capitalized in the setter name.  
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
- Example folder structure:
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
_Operations Lead @ GDGOC AITR_  
_Founder & CEO @ MGCompanies_

> “Write clean code. Automate your backend. Let MGWebRock handle the rest.”
