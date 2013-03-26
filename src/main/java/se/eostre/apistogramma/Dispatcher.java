package se.eostre.apistogramma;

import java.io.IOException;
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
	
	
	void setControllers(List<Controller> controllers) {
		this.controllers = controllers;
	}
	
	void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	public void init() {
		for (Controller controller : controllers) {
			routing.put(controller.name(), controller);
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
