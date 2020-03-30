package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Retry implements State {
	private State previousState;
	private int tries;
	
	public Retry(State previousState) {
		super();
		this.previousState = previousState;
		this.tries = 0;
	}
	

	@Override
	public void process(Operation operation, Services services) {
		this.previousState.process(operation, services);
		
		// if operation failed for the third time, go to error state
		if (operation.getState() == this && ++tries == 3) {
			operation.setState(Error.instance());
		}
	}
	
	@Override
	public void cancel(Operation operation, Services services) throws AccountException, OperationException {
		this.previousState.cancel(operation, services);
	}
}
