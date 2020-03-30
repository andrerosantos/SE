package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;

public class Error implements State {
private static Error instance = new Error();
	
	private Error() {
		super();
	}
	
	public static State instance() {
		return instance;
	}

	@Override
	public void process(Operation operation, Services services) {
		// Do nothing - no operation recovers from error
	}

	@Override
	public void cancel(Operation operation, Services services) throws AccountException {
		// Do nothing - no operation can be canceled after entering this state
		
	}

}
