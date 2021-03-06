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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Route {
    
    String route;
    String method;
    String controller;
    String action;
    
    private Pattern pattern;
    private String[] parts;
    
    public Route(String route) {
        this(route, "");
    }
    
    public Route(String route, String method) {
        this.route = route;
        this.method = method.toLowerCase();
        this.pattern = Pattern.compile(route.replaceAll(":[^/]*", "[^/]*"));
        parts = route.split("/");
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
    
    boolean isMatch(String controller, String action) {
        return (controller == null || controller.equals(this.controller)) && (action == null || action.equals(this.action));
    }

}
