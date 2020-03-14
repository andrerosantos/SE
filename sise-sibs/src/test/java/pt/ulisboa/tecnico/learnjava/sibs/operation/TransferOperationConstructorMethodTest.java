package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.states.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.states.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.states.Withdrawn;

public class TransferOperationConstructorMethodTest {
	private static final String firstName = "John";
	private static final String lastName = "Doe";
	private static final String nif = "123456789";
	private static final String phoneNumber = "987654321";
	private static final String address = "Saint Street";
	private static final int age = 25;
	private static final int VALUE = 100;
	
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
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(sourceBank, firstName, lastName, nif, phoneNumber, address, age);
		this.targetClient = new Client(targetBank, firstName, lastName, nif, phoneNumber, address, age);
		this.sourceIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sourceClient, 1000, 0);
		this.targetIban = targetBank.createAccount(Bank.AccountType.CHECKING, targetClient, 1000, 0);
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
		assertEquals(Registered.instance(), op.getState());
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void testWithdrawn() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();

		assertEquals(Withdrawn.instance(), op.getState());
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void testCompletedNoFee() throws OperationException, AccountException, ClientException, BankException {
		Client newClient = new Client(sourceBank, firstName, lastName, "111111111", phoneNumber, address, age);
		String targetWithSourceBank = sourceBank.createAccount(Bank.AccountType.CHECKING, newClient, 1000, 0);
		
		TransferOperation op = new TransferOperation(this.sourceIban, targetWithSourceBank, 100);

		op.process();
		op.process();

		assertEquals(Completed.instance(), op.getState());
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetWithSourceBank).getBalance());
	}

	@Test
	public void testDeposit() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();
		op.process();

		assertEquals(Deposited.instance(), op.getState());
		assertEquals(900, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void testCompletedWithFee() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();
		op.process();
		op.process();

		assertEquals(Completed.instance(), op.getState());
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void testCancelRegisteredOperation() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.cancel();

		assertEquals(Canceled.instance(), op.getState());
	}
	
	@Test
	public void testCancelWithdrawnOperation() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();

		op.cancel();

		assertEquals(Canceled.instance(), op.getState());
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}
	
	
	@Test
	public void testCancelDepositedOperation() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban, this.targetIban, 100);

		op.process();
		op.process();

		op.cancel();

		assertEquals(Canceled.instance(), op.getState());
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}

	@Test(expected=OperationException.class)
	public void processCanceledOperation() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban,this.targetIban, 100);
		
		op.cancel();
		op.process();
	}
	
	@Test
	public void testCancelFail() throws OperationException, AccountException {
		TransferOperation op = new TransferOperation(this.sourceIban,this.targetIban, 100);
		
		op.process();
		op.process();
		op.process();
		
		try {
			op.cancel();
			fail();
		} catch (OperationException e) {
			assertEquals(Completed.instance(), op.getState());
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
		this.services = null;
	}

}
