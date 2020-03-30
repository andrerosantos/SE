package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Withdrawn implements State{
	private static Withdrawn instance = new Withdrawn();
	
	private Withdrawn() {
		super();
	}

	public static State instance() {
		return instance;
	}

	@Override
	public void process(Operation operation, Services services) {
		if (operation instanceof TransferOperation) {
			try {
				String sourceIban = ((TransferOperation) operation).getSourceIban();
				String targetIban = ((TransferOperation) operation).getTargetIban();
	
				services.deposit(targetIban, operation.getValue());
				
				if (sourceIban.contentEquals(targetIban)) {
					operation.setState(Completed.instance());
					
				} else {
					operation.setState(Deposited.instance());
				}
				
			} catch (AccountException e) {
				if (!(operation.getState() instanceof Retry)) {
					operation.setState(new Retry(this));
				}
			}
		}
	}
	
	@Override
	public void cancel(Operation operation, Services services) throws AccountException {
		if (operation instanceof TransferOperation) {
			services.deposit(((TransferOperation) operation).getSourceIban(), operation.getValue());
			operation.setState(Canceled.instance());
		}
	}
}