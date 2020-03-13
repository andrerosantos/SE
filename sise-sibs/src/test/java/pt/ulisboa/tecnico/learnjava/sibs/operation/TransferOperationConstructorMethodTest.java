package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class TransferOperationConstructorMethodTest {
	private static final String SOURCE_IBAN = "BPICK1";
	private static final String TARGET_IBAN = "CGDCK1";
	private static final int VALUE = 100;

	@Before
	public void setUp() {

	}

	@Test
	public void success() throws OperationException {
		TransferOperation operation = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);

		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
	}

	@Test(expected = OperationException.class)
	public void nonPositiveValue() throws OperationException {
		new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 0);
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new TransferOperation(null, TARGET_IBAN, 100);
	}

	@Test(expected = OperationException.class)
	public void emptySourceIban() throws OperationException {
		new TransferOperation("", TARGET_IBAN, 100);
	}

	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new TransferOperation(SOURCE_IBAN, null, 100);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new TransferOperation(SOURCE_IBAN, "", 100);
	}

	@Test
	public void testRegistered() throws OperationException {
		TransferOperation op = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 100);
		assertEquals(TransferOperation.states.REGISTERED, op.getState());
	}

	@Test
	public void testWithdrawn() throws OperationException {
		TransferOperation op = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 100);

		op.process();

		assertEquals(TransferOperation.states.WITHDRAWN, op.getState());
	}

	@Test
	public void testCompletedNoFee() throws OperationException {
		String targetWithSourceBank = "BPICK2";
		TransferOperation op = new TransferOperation(SOURCE_IBAN, targetWithSourceBank, 100);

		op.process();
		op.process();

		assertEquals(TransferOperation.states.COMPLETED, op.getState());
	}

	@Test
	public void testDeposit() throws OperationException {
		TransferOperation op = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 100);

		op.process();
		op.process();

		assertEquals(TransferOperation.states.DEPOSITED, op.getState());
	}

	@Test
	public void testCompletedWithFee() throws OperationException {
		TransferOperation op = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 100);

		op.process();
		op.process();
		op.process();

		assertEquals(TransferOperation.states.COMPLETED, op.getState());
	}

	@Test
	public void testCancellOperation() throws OperationException {
		TransferOperation op = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 100);

		op.process();

		op.cancel();

		assertEquals(TransferOperation.states.CANCELED, op.getState());
	}

	@After
	public void tearDown() {

	}

}
