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

public class AddOperationMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	private Sibs sibs;
	private Services mockServices;

	@Before
	public void setUp() {
		this.mockServices = mock(Services.class);
		this.sibs = new Sibs(3, mockServices);
	}

	@Test
	public void success() throws OperationException, SibsException, AccountException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int position = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);

		Operation operation = this.sibs.getOperation(position);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(VALUE, operation.getValue());
	}

	@Test
	public void successWithDelete() throws OperationException, SibsException, AccountException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		int position = this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.removeOperation(position);
		
		position = this.sibs.addOperation(new PaymentOperation(TARGET_IBAN, 200));

		Operation operation = this.sibs.getOperation(position);

		assertEquals(3, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_PAYMENT, operation.getType());
		assertEquals(200, operation.getValue());
	}

	@Test(expected = SibsException.class)
	public void failIsFull() throws OperationException, SibsException, AccountException {
		when(this.mockServices.accountExists(SOURCE_IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(TARGET_IBAN)).thenReturn(true);
		
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.transfer(SOURCE_IBAN, TARGET_IBAN, VALUE);
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
