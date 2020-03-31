package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.states.Completed;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public int transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {

		if(!this.services.accountExists(sourceIban) || !this.services.accountExists(targetIban)) {
			throw new SibsException();
		}
		
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, amount, services);
		
		int id = addOperation(operation);
		
		return id;
	}

	public int addOperation(Operation operation)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
	
	public void processOperations() throws AccountException, OperationException {
		for (Operation operation : operations) {
			if (operation instanceof TransferOperation && operation.getState() != Completed.instance()) {
				operation.process();
			}
		}
	}
	
	public void cancelOperation(int transferID) throws AccountException, OperationException, SibsException {
		if (transferID >= operations.length || !(operations[transferID] instanceof TransferOperation)) {
			throw new SibsException();
		}
		((TransferOperation) operations[transferID]).cancel();
	}
	
	public Services getServices() {
		return this.services;
	}
	
}
