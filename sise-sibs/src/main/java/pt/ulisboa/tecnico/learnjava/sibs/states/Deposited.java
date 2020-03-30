package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Deposited implements State{
	private static Deposited instance = new Deposited();
	
	private Deposited() {
		super();
	}
	
	public static State instance() {
		return instance;
	}

	@Override
	public void process(Operation operation, Services services) {
		if(operation instanceof TransferOperation) {
			try {
				services.withdraw(((TransferOperation) operation).getSourceIban(), operation.commission());
				operation.setState(Completed.instance());
			
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
			String targetIban = ((TransferOperation) operation).getTargetIban();
			String sourceIban = ((TransferOperation) operation).getSourceIban();
			
			services.withdraw(targetIban, operation.getValue());
			services.deposit(sourceIban, operation.getValue());
			
		}
	}
}
