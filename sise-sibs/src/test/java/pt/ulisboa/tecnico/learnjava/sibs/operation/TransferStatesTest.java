package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.states.Canceled;
import pt.ulisboa.tecnico.learnjava.sibs.states.Completed;
import pt.ulisboa.tecnico.learnjava.sibs.states.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.states.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.states.Withdrawn;

public class TransferStatesTest {
	private final String SOURCE_IBAN = "sourceIban";
	private final String TARGET_IBAN = "targetIban";
	private final int AMOUNT = 100;
	
	
	private Services mockServices;
	private Sibs sibs;

	@Before
	public void setUp() throws BankException, ClientException {
		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(100, this.mockServices);
	}
	
	@Test
	public void successfulRegister() throws SibsException, AccountException, OperationException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		
		assertEquals(Registered.instance(), this.sibs.getOperation(operationID).getState());
	}
	
	@Test
	public void successfulWithdrawn() throws SibsException, AccountException, OperationException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		
		this.sibs.getOperation(operationID).process();
		
		assertEquals(Withdrawn.instance(), this.sibs.getOperation(operationID).getState());
	}
	
	@Test
	public void successfulDeposit() throws SibsException, AccountException, OperationException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).process();
		
		assertEquals(Deposited.instance(), this.sibs.getOperation(operationID).getState());
	}
	
	@Test
	public void successfulOperationDifferentBanks() throws SibsException, AccountException, OperationException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).process();
		
		assertEquals(Completed.instance(), this.sibs.getOperation(operationID).getState());
	}
	
	@Test
	public void successfulOperationSameBank() throws SibsException, AccountException, OperationException {
		String ibanSameBank = "sourceBankIban";
		
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(ibanSameBank)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, ibanSameBank, AMOUNT);
		
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).process();
		
		assertEquals(Completed.instance(), this.sibs.getOperation(operationID).getState());
	}
	
	@Test
	public void cancelOperation() throws SibsException, AccountException, OperationException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).cancel();
		
		assertEquals(Canceled.instance(), this.sibs.getOperation(operationID).getState());
	}
	
	@Test
	public void tryToCancelCompletedOperation() throws SibsException, AccountException, OperationException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int operationID = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, AMOUNT);
		
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).process();
		this.sibs.getOperation(operationID).process();
		
		try {
			this.sibs.getOperation(operationID).cancel();
		}catch (OperationException e) {
			assertEquals(Completed.instance(), this.sibs.getOperation(operationID).getState());
		}
	}
	
	@After
	public void tearDown() {
		this.sibs = null;
		this.mockServices = null;
	}
}
