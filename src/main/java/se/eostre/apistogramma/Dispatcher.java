/*
 * Copyright (C) 2013 Eostre (Martin Korinth)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package se.eostre.apistogramma;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Dispatcher extends HttpServlet {
    
    private static final long serialVersionUID = 1337L;
    
    private Map<String, Controller> controllers;
    private List<Route> routes;
    
    public Dispatcher() {
        controllers = new HashMap<String, Controller>();
        routes = new LinkedList<Route>();    
    }
    
    public void setControllers(List<Controller> controllers) {
        for (Controller controller : controllers) {
            this.controllers.put(controller.name(), controller);
        }
    }
    
    public void add(Controller... controllers) {
        for (Controller controller : controllers) {
            this.controllers.put(controller.name(), controller);        
        }
    }
    
    public void init() {
        for (Entry<String, Controller> entry : controllers.entrySet()) {    
            for (Method method : entry.getValue().getClass().getMethods()) {
                Action action = method.getAnnotation(Action.class);
                if (action != null) {
                    Route route = new Route(action.route(), action.method());
                    route.controller = entry.getKey();
                    route.action = method.getName().toLowerCase();
                    routes.add(route);
                }
            }
        }
    }
    
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        Environment environment = new Environment(request, response);
        try {
            dispatch(environment);
        } catch (Status status) {
            try {
                handle(status, environment);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void dispatch(Environment environment) throws Status {
        for (Route route : routes) {
            if (environment.resolve(route)) {
                Controller controller = controllers.get(route.controller);
                controller.control(environment);
                return;
            }
        }
    }
    
    private void handle(Status status, Environment environment) throws Exception {
        status.printStackTrace(); // XXX: remove
        environment.setModel("status", status);
        switch (status.code) {
        case 200:
            environment.render();
            break;
        default:
            environment.getResponse().sendError(status.code, status.getMessage()); // TODO: use a configured status controller!
            break;
        }
    }

}
