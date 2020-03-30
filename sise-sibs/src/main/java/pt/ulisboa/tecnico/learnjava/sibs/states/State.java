package pt.ulisboa.tecnico.learnjava.sibs.states;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public interface State {	
	public void process(Operation operation, Services services);
	
	public void cancel(Operation operation, Services services) throws AccountException, OperationException;
	
}
