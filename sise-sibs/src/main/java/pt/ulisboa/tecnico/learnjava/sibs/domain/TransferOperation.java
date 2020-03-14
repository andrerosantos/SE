package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private OperationState state;
	private final Services services; // can I make this static? - no need to have multiple services occupying memory
	
	//private State state; ??

	public enum OperationState {
		REGISTERED("RG"), WITHDRAWN("WD"), DEPOSITED("DP"), COMPLETED("CP"),
		CANCELED("CL");

		private final String prefix;

		OperationState(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return this.prefix;
		}
	}

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.state = OperationState.REGISTERED;
		this.services = new Services();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public void process() throws AccountException, OperationException {
		if (this.state == OperationState.REGISTERED) {

			this.services.withdraw(sourceIban, getValue());
			this.state = OperationState.WITHDRAWN;
			
		} else if (this.state == OperationState.WITHDRAWN && sourceIban.substring(0, 3).contentEquals(targetIban.substring(0, 3))){
			// same bank: deposit and complete
			this.services.deposit(targetIban, getValue());
			this.state = OperationState.COMPLETED;
			
		} else if (this.state == OperationState.WITHDRAWN && !sourceIban.substring(0, 3).contentEquals(targetIban.substring(0, 3))) {
			//deposit money on target account
			this.services.deposit(targetIban, getValue());
			this.state = OperationState.DEPOSITED;
			
		} else if (this.state == OperationState.DEPOSITED) {
			//charge fee
			this.services.withdraw(sourceIban, this.commission());
			this.state = OperationState.COMPLETED;
			
		} else {
			throw new OperationException("Cannot process a canceled operration.");
		}
	}

	public OperationState getState() {
		return this.state;
	}

	public void cancel() throws OperationException, AccountException {
		if (this.state == OperationState.WITHDRAWN) {
			this.services.deposit(sourceIban, getValue());
			this.state = OperationState.CANCELED;
			
		} else if (this.state == OperationState.DEPOSITED) {
			this.services.withdraw(targetIban, getValue());
			this.services.deposit(sourceIban, getValue());
			this.state = OperationState.CANCELED;
			
		} else if (this.state == OperationState.REGISTERED) {
			this.state = OperationState.CANCELED;
			
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
