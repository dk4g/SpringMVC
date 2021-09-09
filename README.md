# SpringMVC without Spring Boot

We can create a Spring MVC application without using Spring Boot. This will give a better understanding of 
what happens in the background since Spring Boot will do a lot of magic preventing us from getting a 
deeper understanding of how a Web Application is bootstrapped.

### Maven Dependencies
We start creating our Spring MVC app by first creating a simple maven app without any archetype.

Then add the following dependencies in `pom.xml` to get Spring MVC relevant libraries into our project.


```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>5.3.9</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.9</version>
</dependency>
```
Additionally we need to add servlet dependency.

```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
```
To support JSON responses from a rest controller we need to add dependency on jackson library.

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.5</version>
</dependency>
```
and finally if we use JSP pages then to support compilation and rendering of JSP pages we need to add 
 `tomcat-embed-jasper dependency`. It is recommended to not use JSPs in Spring based applications but 
rather use Thymeleaf of Freemarker as alternatives to JSP.

```xml
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
    <version>10.0.8</version>
    <scope>provided</scope>
</dependency>
```

And this is all what we need to start building a bare minimum Spring MVC app without Spring Boot.

### Bootstrapping

#### Initializer class
Spring MVC follows the front controller pattern where a central `Servlet` which is `DispatcherServlet` 
receives all requests and _dispatches_ them to the right handlers(controllers). How the 
`DispatcherServlet` knows about the right handler depends on the configuration.

Next step is to bootstrap our Spring MVC app by configuring and registering the `DispatcherServlet` which 
is auto detected by Servlet Container (e.g Tomcat). We can do so by creating a class named 
`AppInitializer` which implements the interface `WebApplicationInitializer`. In this class we need 
to override the method `onStartup`.
```java
public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(WebAppConfig.class);

        //Create DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);

        //Register DispatcherServlet
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet("springmvc", dispatcherServlet);
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");

        servletContext.addListener(new ContextLoaderListener(appContext));
    }
}
```

Generally it is sufficient for most of the applications to have one `DispatcherServlet` and one 
`WebApplicationContext`and the above way of bootstrapping is enough for that purpose. 
_Note: A `DispatcherServlet` expects a `WebApplicationContext` for its own configuration._
If it is needed that a single `WebApplicationContext` is shared across multiple `DispatcherServlet` or 
other `Servlet` then the bootstrapping can be done by configuring a `WebApplicationContext` hierarchy by 
extending `AbstractAnnotationConfigDispatcherServletInitializer`and overriding specific methods.

```java
public class AppInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebAppConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
```


#### Configuration class
Create one more configuration class `WebAppConfig` which will be annotated with `@Configuration` and 
`@EnableWebMvc` annotations. The `@EnableWebMvc` imports the Spring MVC configuration from 
`WebMvcConfigurationSupport`. If customization of imported configurations is needed then the class can 
implement `WebMvcConfigurer` interface and declare additional beans or override methods. In our case we define a bean for 
`ViewResolver` which will tell spring where to look for view JSP files in our application and additionally 
set the view name for root '/' path which is `index.jsp`.

```java
@Configuration
@EnableWebMvc
@ComponentScan("com.dk.SpringMVC")
public class WebAppConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/jsp/");
        bean.setSuffix(".jsp");
        bean.setOrder(0);
        return bean;
    }
}
```
Create an `index.jsp` file in `src/main/webapp/WEB-INF/jsp` folder which will be displayed when the 
applications starts.

This is all what we need to do to create a barebones Spring MVC app which can be hosted on Tomcat or Jetty.

We can add one `RestController` to serve JSON responses by creating a class and annotating it with 
`@RestController` annotation. 

Similarly one or more controller classes can be created to handle different paths in the application. 
Contoller classes will be annotated with `@Controller` annotation and the get methods in these controller 
classes will return the view name in string format.