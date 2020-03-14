package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.states.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.states.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.states.State;
import pt.ulisboa.tecnico.learnjava.sibs.states.Withdrawn;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private State state;
	private final Services services; // can I make this static? - no need to have multiple services occupying memory

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.state = Registered.instance();
		this.services = new Services();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public void process() throws AccountException, OperationException {
		if (this.state == Registered.instance()) {

			this.services.withdraw(sourceIban, getValue());
			this.state = Withdrawn.instance();
			
		} else if (this.state == Withdrawn.instance() && sourceIban.substring(0, 3).contentEquals(targetIban.substring(0, 3))){
			// same bank: deposit and complete
			this.services.deposit(targetIban, getValue());
			this.state = Completed.instance();
			
		} else if (this.state == Withdrawn.instance() && !sourceIban.substring(0, 3).contentEquals(targetIban.substring(0, 3))) {
			//deposit money on target account
			this.services.deposit(targetIban, getValue());
			this.state = Deposited.instance();
			
		} else if (this.state == Deposited.instance()) {
			//charge fee
			this.services.withdraw(sourceIban, this.commission());
			this.state = Completed.instance();
			
		} else {
			throw new OperationException("Cannot process a canceled operration.");
		}
	}

	public State getState() {
		return this.state;
	}

	public void cancel() throws OperationException, AccountException {
		if (this.state == Withdrawn.instance()) {
			this.services.deposit(sourceIban, getValue());
			this.state = Canceled.instance();
			
		} else if (this.state == Deposited.instance()) {
			this.services.withdraw(targetIban, getValue());
			this.services.deposit(sourceIban, getValue());
			this.state = Canceled.instance();
			
		} else if (this.state == Registered.instance()) {
			this.state = Canceled.instance();
			
		} else {
			throw new OperationException("Cannot cancel a completed operation.");
		}
		
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
