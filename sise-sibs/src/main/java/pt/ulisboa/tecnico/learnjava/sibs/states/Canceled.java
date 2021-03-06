package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;

public class Canceled implements State{
	private static Canceled instance = new Canceled();
	
	private Canceled() {
		super();
	}
	
	public static State instance() {
		return instance;
	}

	@Override
	public void process(Operation operation, Services services) {
		// Do nothing	
	}
	
	@Override
	public void cancel(Operation operation, Services services) throws AccountException {
		// Do nothing	
	}
}
