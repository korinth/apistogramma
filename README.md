# Apistogramma

Apistogramma is a lightweight Java MVC-replacement. It is targeted for those projects where you could just as easily use plain servlets and JSP, but want a minimal abstraction of controllers, models and views.

## Intention

This is the intended conceptual usage:

    package com.example.controllers
    
    public class Users extends Controller {
        
        @Action(route="/:controller")
        public void index(Environment environment) {
            environment.setModel("users", Database.findUsers());
            environment.render("index");
        }
        
        @Action(route="/:controller/:id")
        public void view(Environment environment) {
            environment.setModel("user", User.find(environment.getAttribute("id")));
            environment.render("view");
        }
        
        @Action(route="/:controller/:id/:action")
        public void edit(Environment environment) {
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
        public void (Environment environment) {
            environment.render("users", "custom");
        }
        
    }
