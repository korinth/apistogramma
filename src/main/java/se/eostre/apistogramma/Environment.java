package se.eostre.apistogramma;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Environment {
	
	static final String FILE_SUFFIX = "(\\.[a-zA-Z0-9]{3,})?$";
	
	String uri;
	HttpServletRequest request;
	HttpServletResponse response;
	Map<String, String> attributes;

	Environment(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		uri = request.getPathInfo().replaceAll("FILE_SUFFIX", "");
	}
	
	boolean resolve(Route route) {
		attributes = route.parse(uri);
		return attributes != null;
	}
	
	String controller() {
		return attributes.get("controller");
	}
	
	String action() {
		return attributes.get("action");
	}
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public String getParamter(String name) {
		return request.getParameter(name);
	}
	
	public String[] getParameters(String name) {
		return request.getParameterValues(name);
	}
	
	public void setModel(String name, Object object) {
		request.setAttribute(name, object);
	}
	
	public String getUri() {
		return uri;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	public void render() {
		render(controller(), action());
	}
	
	public void render(String action) {
		render(controller(), action);
	}
	
	public void render(String controller, String action) {
		// TODO: if not AJAX
		request.getRequestDispatcher("/" + controller + "/" + action + ".jsp");
	}
	
	// TODO: redirect(String action) or redirect(String controller, String action) ?
	
}
