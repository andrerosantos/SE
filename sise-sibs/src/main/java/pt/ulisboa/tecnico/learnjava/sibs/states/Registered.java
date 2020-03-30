package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Registered implements State{
	private static Registered instance = new Registered();
	
	private Registered() { }

	public static State instance() {
		return instance;
	}

	@Override
	public void process(Operation operation, Services services) {
		if (operation instanceof TransferOperation) {
			try {
				services.withdraw(((TransferOperation) operation).getSourceIban(), operation.getValue());
				operation.setState(Withdrawn.instance());
				
			} catch (AccountException e) {
				if (!(operation.getState() instanceof Retry)) {
					operation.setState(new Retry(this));
				}
			}
		}
	}
	
	@Override
	public void cancel(Operation operation, Services services) {
		operation.setState(Canceled.instance());
	}
	
}
