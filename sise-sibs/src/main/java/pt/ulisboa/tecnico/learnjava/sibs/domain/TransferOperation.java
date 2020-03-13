package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperation extends Operation {
	private final String sourceIban;
	private final String targetIban;
	private String state;
	private final Services services; //can I make this static? - no need to have multiple services occupying memory
	
	public enum states {
		REGISTERED("registered"), WITHDRAWN("withdrawn"), DEPOSITED("deposited"), COMPLETED("completed"), CANCELED("canceled");
		
		private final String prefix;

		states(String prefix) {
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
		this.state = states.REGISTERED.getPrefix();
		this.services = new Services();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}
	
	public void process() {
		if (this.state.contentEquals(states.REGISTERED.getPrefix())) {
			
			// ToDo - see what else to do
			
			this.state = states.WITHDRAWN.getPrefix();
		} else if (this.state.equals(states.WITHDRAWN.getPrefix())){
			
			
			
		}
	}
	
	public String getState() {
		return this.state;
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
