# Apistogramma

Apistogramma is a lightweight Java MVC-replacement. It is targeted for those projects where you could just as easily use plain servlets and JSP, but want a minimal abstraction of controllers, models and views.

This project is work in progress. Suggestions are welcome!

## Intention

The following is an example of the intensions for usage of Apistogramma.

##  com.example.controllers.Users.java

    package com.example.controllers;
    
    import com.example.records.User;
    import se.eostre.apistogramma.*;
    
    public class Users extends Controller {
        
        @Action(route="/:controller")
        public void index(Environment environment) throws Status {
            environment.setModel("users", Users.findAll());
            environment.render("index");
        }
        
        @Action(route="/:controller/:id")
        public void view(Environment environment) throws Status {
            environment.setModel("user", User.find(environment.getAttribute("id")));
            environment.render("view");
        }
        
        @Action(route="/:controller/:id/:action")
        public void edit(Environment environment) throws Status {
            environment.setModel("user", User.find(environment.getAttribute("id")));
            environment.render();
        }
        
        @Action(route="/:controller/:id/:action")
        public void update(Environment environment) {
            User user = User.find(environment.getAttribute("id"));
            user.setStatus(environment.getParamter("user[status]"));
            user.save();
            environment.redirect("view");
        }
        
        @Action(route="/custom/path/with/neither/controller/nor/action")
        public void custom(Environment environment) {
            environment.render("users", "custom");
        }
        
    }


##  com.example.Dispatcher.java

    package com.example;
    
    import com.example.controllers.*;
    
    public class Dispatcher extends se.eostre.apistogramma.Dispatcher {
        public Dispatcher() {
            super();
            add(new Users());
        }
    }

## WEB-INF/web.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <web-app version="2.5"
             xmlns="http://java.sun.com/xml/ns/javaee"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
    
        <display-name>Example.com</display-name>
    
        <servlet>
            <servlet-name>Dispatcher</servlet-name>
            <servlet-class>com.example.Dispatcher</servlet-class>
        </servlet>
    
        <servlet-mapping>
            <servlet-name>Dispatcher</servlet-name>
            <url-pattern>*.act</url-pattern>
        </servlet-mapping>
    
    </web-app>
