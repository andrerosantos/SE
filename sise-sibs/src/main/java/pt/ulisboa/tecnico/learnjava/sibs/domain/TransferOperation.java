package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.states.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.states.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.states.Error;
import pt.ulisboa.tecnico.learnjava.sibs.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.states.State;
import pt.ulisboa.tecnico.learnjava.sibs.states.Withdrawn;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private State state;
	private final Services services;
	private int fails;

	public TransferOperation(String sourceIban, String targetIban, int value, Services services) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.state = Registered.instance();
		this.services = services;
		this.fails = 0;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public void process() {
		this.state.process(this, this.services);
	}

	@Override
	public State getState() {
		return this.state;
	}
	
	@Override
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void cancel() throws OperationException, AccountException {
		this.state.cancel(this, this.services);
	}

	@Override
	public int commission() {
		if (this.sourceIban.substring(0, 3).equals(this.targetIban.substring(0, 3))) {
			return 0;
		}
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

	
}
