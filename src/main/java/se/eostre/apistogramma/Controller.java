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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller {
	
	private Map<String, Method> actions = new HashMap<String, Method>();
	
	protected String name() {
		return getClass().getSimpleName().toLowerCase();
	}

	protected void control(Environment environment) throws Status {
		reflect(environment);
	}
	
	protected final void reflect(Environment environment) throws Status {
		try {
			String action = environment.action();
			Method method = cache(action);
			method.invoke(this, environment);
		} catch (NoSuchMethodException exception) {
			throw new Status("No such action!", 404, exception);
		} catch (InvocationTargetException exception) {
			if (exception.getCause() instanceof Status) {
				throw (Status) exception.getCause();
			} else {
				throw new Status("Unhandled exception!", 500, exception.getCause());
			}			
		} catch (Exception exception) {
			throw new Status("Internal server error!", 500, exception);
		}
	}
	
	private Method cache(String action) throws SecurityException, NoSuchMethodException {
		Method method = actions.get(action);
		if (method == null) {
			System.out.println(action);
			method = getClass().getMethod(action, Environment.class);
			actions.put(action, method);
		}
		return method;
	}

}
