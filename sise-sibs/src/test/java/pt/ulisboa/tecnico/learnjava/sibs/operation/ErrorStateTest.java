package pt.ulisboa.tecnico.learnjava.sibs.operation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.junit.After;

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
import pt.ulisboa.tecnico.learnjava.sibs.states.Error;

public class ErrorStateTest {
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
	private Client sameBankClient;
	private Client targetClient;
	private String sourceIban;
	private String targetIban;
	private String sameBankIban;
	
	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.services = new Services();
		this.sibs = new Sibs(10, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(sourceBank, firstName, lastName, nif, phoneNumber, address, age);
		this.sameBankClient = new Client(sourceBank, firstName, lastName, "987654321", phoneNumber, address, age);
		this.targetClient = new Client(targetBank, firstName, lastName, nif, phoneNumber, address, age);
		this.sourceIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sourceClient, 1000, 0);
		this.targetIban = targetBank.createAccount(Bank.AccountType.CHECKING, targetClient, 1000, 0);
		this.sameBankIban = sourceBank.createAccount(Bank.AccountType.CHECKING, sameBankClient, 1000, 0);
	}
	
	@Test
	public void getToErrorStateTest() throws SibsException, AccountException, OperationException {
		this.sibs.transfer(this.sourceIban, this.sameBankIban, 999);
		int errorOperationID = this.sibs.transfer(sourceIban, sameBankIban, 999);
		
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		this.sibs.processOperations();
		
		TransferOperation transfer = (TransferOperation) this.sibs.getOperation(errorOperationID);
		
		assertEquals(Error.instance(), transfer.getState());
	}
	
	@After
	public void tearDown() {
		this.sibs = null;
		this.services = null;
		Bank.clearBanks();
	}
}
