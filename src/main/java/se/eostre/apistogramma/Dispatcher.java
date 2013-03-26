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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Dispatcher extends HttpServlet {
	
	private static final long serialVersionUID = 1337L;
	
	private List<Controller> controllers;
	private List<Route> routes;
	private Map<String, Controller> routing;
	
	public Dispatcher() {
		controllers = new LinkedList<Controller>();
		routes = new LinkedList<Route>();
		routing = new HashMap<String, Controller>();		
	}
	
	public void setControllers(List<Controller> controllers) {
		this.controllers = controllers;
	}
	
	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	public void init() {
		for (Controller controller : controllers) {
			routing.put(controller.name(), controller);				
			for (Method method : controller.getClass().getMethods()) {
				Action action = method.getAnnotation(Action.class);
				if (action != null) {
					Route route = new Route(action.route());
					route.setController(controller);
					routes.add(route);
				}
			}
		}
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) {
		try {
			Environment environment = new Environment(request, response);
			for (Route route : routes) {
				if (environment.resolve(route)) {
					dispatch(environment, route.controller);
					break;
				}
			}			
		} catch (Exception exception) {
			try {
				response.sendError(500, "Failed to dispatch request!");
				exception.printStackTrace();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	private void dispatch(Environment environment, Controller controller) {
		if (controller == null) {
			controller = routing.get(environment.controller());
		}			
		controller.control(environment);
	}

}
