package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferFailMockTest {
	private Services mockServices;
	private Sibs sibs;

	@Before
	public void setUp() throws BankException, ClientException {
		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(100, this.mockServices);
	}

	@Test
	public void failDeposit()
			throws BankException, AccountException, ClientException, SibsException, OperationException {
		String sourceIban = "CGDCK1";
		String targetIban = "BPICK1";

		doThrow(new AccountException()).when(this.mockServices).deposit(targetIban, 100);

		when(this.mockServices.accountExists(sourceIban)).thenReturn(true);
		when(this.mockServices.accountExists(targetIban)).thenReturn(true);

		try {
			this.sibs.transfer(sourceIban, targetIban, 100);
			fail();
		} catch (SibsException e) {
			verify(this.mockServices, Mockito.times(0)).withdraw(sourceIban, 106);
		}

	}
}
