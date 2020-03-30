package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Completed implements State{
	private static Completed instance = new Completed();
	
	private Completed() {
		super();
	}
	
	public static State instance() {
		return instance;
	}

	@Override
	public void process(Operation operation, Services services) {
		//Do nothing
	}
	
	@Override
	public void cancel(Operation operation, Services services) throws OperationException {
		throw new OperationException("Not possible to cancel a completed operation.");
	}


}
