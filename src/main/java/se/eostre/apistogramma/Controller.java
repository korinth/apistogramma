package se.eostre.apistogramma;

public abstract class Controller {
	
	protected String name() {
		return getClass().getName().toLowerCase();
	}

	protected void control(Environment environment) {
		reflect(environment);
	}
	
	protected final void reflect(Environment environment) {
		// TODO: execute action method
	}

}
