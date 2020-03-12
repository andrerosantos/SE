package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMethodMockTest {
	private Services mockServices;
	private Sibs sibs;
	private String sourceIban;
	private String targetIban;

	@Before
	public void setUp() throws BankException, ClientException {
		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(100, this.mockServices);

		this.sourceIban = "CGDCK1";
		this.targetIban = "BPICK1";
	}

	@Test
	public void transferSuccess()
			throws BankException, AccountException, ClientException, SibsException, OperationException {

		when(this.mockServices.accountExists(this.sourceIban)).thenReturn(true);
		when(this.mockServices.accountExists(this.sourceIban)).thenReturn(true);

		when(this.mockServices.accountExists(this.sourceIban)).thenReturn(true);
		when(this.mockServices.accountExists(this.targetIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);

		verify(this.mockServices).withdraw(this.sourceIban, 106);
		verify(this.mockServices).deposit(this.targetIban, 100);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperations());
		assertEquals(100, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));

	}

	@Test
	public void transferWithoutFee()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		// same bank - no fee
		this.targetIban = "CGDCK2";

		when(this.mockServices.accountExists(this.sourceIban)).thenReturn(true);
		when(this.mockServices.accountExists(this.targetIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);

		verify(this.mockServices).withdraw(this.sourceIban, 100);
		verify(this.mockServices).deposit(this.targetIban, 100);

	}

	@Test(expected = SibsException.class)
	public void noAccountsInBanks()
			throws BankException, AccountException, ClientException, SibsException, OperationException {

		when(this.mockServices.accountExists(this.sourceIban)).thenReturn(true);

		this.sibs.transfer(this.sourceIban, this.targetIban, 100);

	}

	@After
	public void tearDown() {
		this.mockServices = null;
		this.sibs = null;

		Bank.clearBanks();
	}
}
