package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.PaymentOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class GetTotalValueOfOperationsMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";

	private Sibs sibs;
	private Services mockServices;

	@Before
	public void setUp() {
		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(3, this.mockServices);
	}

	@Test
	public void successTwo() throws SibsException, OperationException, AccountException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		this.sibs.addOperation(new PaymentOperation(TARGET_IBAN, 100));
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, 100);
		
		assertEquals(200, this.sibs.getTotalValueOfOperations());
	}

	@After
	public void tearDown() {
		this.sibs = null;
		this.mockServices = null;
	}

}
