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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Environment {
    
    String uri;
    String method;
    String route;
    String controller;
    String action;
    HttpServletRequest request;
    HttpServletResponse response;
    Map<String, String> attributes;

    Environment(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        uri = request.getServletPath().replace(".act", "");
        method = request.getMethod().toLowerCase();
    }
    
    boolean resolve(Route route) {
        if (route.method.isEmpty() || route.method.equals(method)) {
            attributes = route.parse(uri);
            if (attributes != null) {
                if (route.isMatch(attributes.get("controller"), attributes.get("action"))) {
                    this.route = route.route;                
                    controller = route.controller;                
                    action = route.action;                
                } else {
                    attributes = null;
                }                
            }
        }
        return attributes != null;
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
    
    public String getRoute() {
        return route;
    }
    
    public String getUri() {
        return uri;
    }
    
    public String getMethod() {
        return method;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
    
    public void render() throws Status {
        render(controller, action);
    }
    
    public void render(String action) throws Status {
        render(controller, action);
    }
    
    public void render(String controller, String action) throws Status {
        // TODO: if not AJAX
        try {
            request.getRequestDispatcher("/views/" + controller + "/" + action + ".jsp").forward(request, response);
        } catch (Exception exception) {
            throw new Status("Unable to render!", 500, exception);
        }
    }
    
    public void redirect(String path) throws Status {
        // TODO: if path.startsWith("/") add context path
        try {
            response.sendRedirect(path);
        } catch (Exception exception) {
            throw new Status("Unable to redirect!", 500, exception);
        }
    }
    
    @Override
    public String toString() {
        return "{ uri => " + uri + ", method => " + method + ", attributes => " + attributes + "}";
    }
    
}
