package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperationConstructorMethodTest {
	private static final String firstName = "John";
	private static final String lastName = "Doe";
	private static final String nif = "123456789";
	private static final String phoneNumber = "987654321";
	private static final String address = "Saint Street";
	private static final int age = 25;
	private static final int VALUE = 100;
	
	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;
	private String sourceIban;
	private String targetIban;
	

	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.services = new Services();
		this.sibs = new Sibs(10, this.services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(sourceBank, firstName, lastName, nif, phoneNumber, address, age);
		this.targetClient = new Client(targetBank, firstName, lastName, nif, phoneNumber, address, age);
		this.sourceIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sourceClient, 1000, 0);
		this.targetIban = sourceBank.createAccount(Bank.AccountType.CHECKING, targetClient, 1000, 0);
	}

	@Test
	public void success() throws OperationException {
		TransferOperation operation = new TransferOperation(this.sourceIban, this.targetIban, VALUE);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(this.sourceIban, operation.getSourceIban());
		assertEquals(this.targetIban, operation.getTargetIban());
	}

	@Test(expected = OperationException.class)
	public void nonPositiveValue() throws OperationException {
		new TransferOperation(this.sourceIban, this.targetIban, 0);
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new TransferOperation(null, this.targetIban, 100);
	}

	@Test(expected = OperationException.class)
	public void emptySourceIban() throws OperationException {
		new TransferOperation("", this.targetIban, 100);
	}

	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new TransferOperation(this.sourceIban, null, 100);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new TransferOperation(this.sourceIban, "", 100);
	}

	@Test
	public void testRegistered() throws OperationException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);
		assertEquals(TransferOperation.states.REGISTERED, op.getState());
	}

	@Test
	public void testWithdrawn() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();

		assertEquals(TransferOperation.states.WITHDRAWN, op.getState());
	}

	@Test
	public void testCompletedNoFee() throws OperationException, AccountException {
		String targetWithSourceBank = "BPICK2";
		TransferOperation op = new TransferOperation(this.sourceIban, targetWithSourceBank, 100);

		op.process();
		op.process();

		assertEquals(TransferOperation.states.COMPLETED, op.getState());
	}

	@Test
	public void testDeposit() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();
		op.process();

		assertEquals(TransferOperation.states.DEPOSITED, op.getState());
	}

	@Test
	public void testCompletedWithFee() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();
		op.process();
		op.process();

		assertEquals(TransferOperation.states.COMPLETED, op.getState());
	}

	@Test
	public void testCancelOperation() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();

		op.cancel();

		assertEquals(TransferOperation.states.CANCELED, op.getState());
	}

	@Test
	public void testCancelFail() {
		
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
