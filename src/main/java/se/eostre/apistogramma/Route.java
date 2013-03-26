package se.eostre.apistogramma;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Route {
	
	Controller controller;
	
	private Pattern pattern;
	private String[] parts;
	
	public Route(String uri) {
		pattern = Pattern.compile(uri.replaceAll(":[^/]*", "[^/]*"));
		parts = uri.split("/");
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	Map<String, String> parse(String uri) {
		Map<String, String> attributes = null;
		String[] params = uri.split("/");
		if (pattern.matcher(uri).matches() && parts.length == params.length) {
			attributes = new HashMap<String, String>();
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].startsWith(":")) {
					attributes.put(parts[i].replaceFirst(":", ""), params[i]);
				}
			}
		}
		return attributes;
	}
	

}
