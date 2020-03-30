package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.states.State;

public abstract class Operation {
	public static final String OPERATION_TRANSFER = "transfer";
	public static final String OPERATION_PAYMENT = "payment";

	private final String type;
	private final int value;
	private State state;

	public Operation(String type, int value) throws OperationException {
		checkParameters(type, value);
		this.type = type;
		this.value = value;
	}

	private void checkParameters(String type, int value) throws OperationException {
		if (type == null || !type.equals(OPERATION_TRANSFER) && !type.equals(OPERATION_PAYMENT)) {
			throw new OperationException(type);
		}

		if (value <= 0) {
			throw new OperationException(value);
		}
	}

	public int commission() {
		return 1;
	}

	public String getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void process() { }
	
	public void cancel() throws OperationException, AccountException { }

}
