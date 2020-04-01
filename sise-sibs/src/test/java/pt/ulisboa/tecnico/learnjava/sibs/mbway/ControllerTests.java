package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ControllerTests {
	private final String IBAN = "iban";
	private final String IBAN_2 = "iban2";
	private final int PHONE_NUMBER = 987654321;
	private final int PHONE_NUMBER_2 = 987654322;
	
	private Services mockServices;
	private View mockView;
	private Controller controller;
	
	@Before
	public void setUp() {
		this.mockServices = mock(Services.class);
		this.mockView = mock(View.class);
		this.controller = new Controller(this.mockServices, this.mockView);
	}
	
	@Test
	public void createMBwayMethodTest() {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		
		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		
		verify(this.mockView).printConfirmationCode(Mockito.anyInt());
		assertTrue(this.controller.accountExists(PHONE_NUMBER));
	}
	
	@Test
	public void failcreateMBwayMethodTest() {
		this.controller.createMBWay(IBAN, 99);
		
		verify(this.mockView).printException("This phone number is not valid.");
		assertFalse(this.controller.accountExists(99));
	}
	
	@Test
	public void confirmAccountMethodTest() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		
		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		verify(this.mockView).printConfirmationResult(true);
	}
	
	@Test
	public void wrongConfirmationAccountMethodTest() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		
		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode() + 1);
		
		verify(this.mockView).printConfirmationResult(false);
	}
	
	@Test
	public void transferMethodTest() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN_2)).thenReturn(mock(Account.class));

		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(IBAN_2)).thenReturn(true);
		
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(1000);

		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		this.controller.createMBWay(IBAN_2, PHONE_NUMBER_2);
		this.controller.confirmAccount(PHONE_NUMBER_2, MBWayAccount.getMBWayAccount(PHONE_NUMBER_2).getConfirmationCode());
		
		this.controller.transfer(PHONE_NUMBER, PHONE_NUMBER_2, 100);
		
		verify(this.mockView).printSuccessfulTransfer();
	}
	
	@Test
	public void splitBillMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN_2)).thenReturn(mock(Account.class));

		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(IBAN_2)).thenReturn(true);

		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(1000);
		when(this.mockServices.getAccountByIban(IBAN_2).getBalance()).thenReturn(1000);

		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		this.controller.createMBWay(IBAN_2, PHONE_NUMBER_2);
		this.controller.confirmAccount(PHONE_NUMBER_2, MBWayAccount.getMBWayAccount(PHONE_NUMBER_2).getConfirmationCode());
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER, 50);
		friends.put(PHONE_NUMBER_2, 50);
		
		this.controller.splitBill(2, 100, friends, PHONE_NUMBER);
		
		verify(this.mockView).printSuccessSplitBill();
	}
	
	@Test
	public void failTransferMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN_2)).thenReturn(mock(Account.class));

		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(IBAN_2)).thenReturn(true);
		
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(500);

		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		this.controller.createMBWay(IBAN_2, PHONE_NUMBER_2);
		this.controller.confirmAccount(PHONE_NUMBER_2, MBWayAccount.getMBWayAccount(PHONE_NUMBER_2).getConfirmationCode());
		
		this.controller.transfer(PHONE_NUMBER, PHONE_NUMBER_2, 1000);
		
		verify(this.mockView).printException(Mockito.anyString());
	}
	
	@Test
	public void notEnoughFriendsSplitBillMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN_2)).thenReturn(mock(Account.class));

		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(IBAN_2)).thenReturn(true);

		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		this.controller.createMBWay(IBAN_2, PHONE_NUMBER_2);
		this.controller.confirmAccount(PHONE_NUMBER_2, MBWayAccount.getMBWayAccount(PHONE_NUMBER_2).getConfirmationCode());
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER, 50);
		friends.put(PHONE_NUMBER_2, 50);
		
		this.controller.splitBill(3, 100, friends, PHONE_NUMBER);
		
		verify(this.mockView).printNotEnoughFriends();;
	}
	
	@Test
	public void tooManyFriendsSplitBillMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN_2)).thenReturn(mock(Account.class));

		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(IBAN_2)).thenReturn(true);

		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		this.controller.createMBWay(IBAN_2, PHONE_NUMBER_2);
		this.controller.confirmAccount(PHONE_NUMBER_2, MBWayAccount.getMBWayAccount(PHONE_NUMBER_2).getConfirmationCode());
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER, 50);
		friends.put(PHONE_NUMBER_2, 50);
		
		this.controller.splitBill(1, 100, friends, PHONE_NUMBER);
		
		verify(this.mockView).printTooManyFriends();
	}
	
	@Test
	public void notEnoughMoneySplitBillMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN_2)).thenReturn(mock(Account.class));

		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(IBAN_2)).thenReturn(true);

		this.controller.createMBWay(IBAN, PHONE_NUMBER);
		this.controller.confirmAccount(PHONE_NUMBER, MBWayAccount.getMBWayAccount(PHONE_NUMBER).getConfirmationCode());
		
		this.controller.createMBWay(IBAN_2, PHONE_NUMBER_2);
		this.controller.confirmAccount(PHONE_NUMBER_2, MBWayAccount.getMBWayAccount(PHONE_NUMBER_2).getConfirmationCode());
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER, 50);
		friends.put(PHONE_NUMBER_2, 50);
		
		this.controller.splitBill(2, 100, friends, PHONE_NUMBER);
		
		verify(this.mockView).printException(Mockito.anyString());
	}
	
	@After
	public void tearDown() {
		this.controller = null;
		MBWayAccount.clearMBWayAccounts();
	}
	
}
