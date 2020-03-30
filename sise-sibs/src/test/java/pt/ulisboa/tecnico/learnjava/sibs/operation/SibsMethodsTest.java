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
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.states.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.states.Completed;

public class SibsMethodsTest {
	private Services services;
	private Sibs sibs;
	
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
	private String sourceIban;
	private String targetIban;
	
	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.sibs = new Sibs(10, services);
		
		this.services = new Services();
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(sourceBank, firstName, lastName, nif, phoneNumber, address, age);
		this.targetClient = new Client(targetBank, firstName, lastName, nif, phoneNumber, address, age);
		this.sourceIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sourceClient, 1000, 0);
		this.targetIban = targetBank.createAccount(Bank.AccountType.CHECKING, targetClient, 1000, 0);
	}
	
	@Test
	public void createTransferOperation() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(sourceIban, targetIban, 100);

		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
	}
	
	@Test
	public void completeTransfer() throws SibsException, AccountException, OperationException {
		int id = this.sibs.transfer(sourceIban, targetIban, 100);
		
		TransferOperation transfer = (TransferOperation) this.sibs.getOperation(id);
		
		transfer.process();
		transfer.process();
		transfer.process();
		
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(Completed.instance(), transfer.getState());
	}
	
	@Test
	public void processOperationsTest() throws SibsException, AccountException, OperationException {
		int id = this.sibs.transfer(sourceIban, targetIban, 100);
		
		TransferOperation transfer = (TransferOperation) this.sibs.getOperation(id);
		
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		
		assertEquals(894, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(Completed.instance(), transfer.getState());
	}
	
	@Test
	public void cancelOperationsTest() throws SibsException, AccountException, OperationException {
		int id = this.sibs.transfer(sourceIban, targetIban, 100);
		
		TransferOperation transfer = (TransferOperation) this.sibs.getOperation(id);
		
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.cancelOperation(id);
		
		assertEquals(1000, this.services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, this.services.getAccountByIban(targetIban).getBalance());
		assertEquals(Canceled.instance(), transfer.getState());
	}
	
	@After
	public void tearDown() {
		this.sibs = null;
		Bank.clearBanks();
	}
}
