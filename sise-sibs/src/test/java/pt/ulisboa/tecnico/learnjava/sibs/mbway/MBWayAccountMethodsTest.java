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

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayAccountMethodsTest {
	private final String IBAN = "iban";
	private final int PHONE_NUMBER = 987654321;
	
	private Services mockServices;
	
	@Before
	public void setUp() {
		this.mockServices = mock(Services.class);
	}
	
	@Test
	public void createMBWayAccount() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(1000);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		
		assertEquals(acc, MBWayAccount.getMBWayAccount(PHONE_NUMBER));
		assertEquals(PHONE_NUMBER, acc.getPhoneNumber());
		assertEquals(IBAN, acc.getIban());
		assertEquals(1000, acc.getBalance());
	}
	
	@Test (expected=MBWayException.class)
	public void createAccountWithAssignedPhoneNumberTest() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(1000);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);

		new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
	}
	
	@Test (expected=MBWayException.class)
	public void createAccountWithNonExistingIbanTest() throws MBWayException {
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
	}
	
	@Test
	public void confirmMBWayAccount() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		acc.validateAccount(acc.getConfirmationCode());
		
		assertTrue(acc.isConfirmed());
	}
	
	@Test
	public void wrongConfirmationCode() throws MBWayException {
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		
		acc.validateAccount(acc.getConfirmationCode() + 1);
		
		assertFalse(acc.isConfirmed());
	}
	
	@Test
	public void transferMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		String anotherIban = "iban2";
		
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(anotherIban)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(1000);
		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(anotherIban)).thenReturn(true);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		MBWayAccount acc2 = new MBWayAccount(anotherIban, 912345678, this.mockServices);
		
		acc.validateAccount(acc.getConfirmationCode());
		acc2.validateAccount(acc2.getConfirmationCode());
		
		acc.transferMoney(acc2, 1000);
	}
	
	@Test
	public void notEnoughMoneyForTransferTest() throws MBWayException, SibsException, AccountException, OperationException {
		String anotherIban = "iban2";
		
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(anotherIban)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(500);
		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(anotherIban)).thenReturn(true);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		MBWayAccount acc2 = new MBWayAccount(anotherIban, 912345678, this.mockServices);
		
		acc.validateAccount(acc.getConfirmationCode());
		acc2.validateAccount(acc2.getConfirmationCode());
		
		try {
			acc.transferMoney(acc2, 1000);
			fail();
		} catch (Exception e) {
			assertEquals("Not enough money in the source account!", e.getMessage());
		}
	}
	
	@Test
	public void transferWithUncofirmedAccountTest() throws MBWayException, SibsException, AccountException, OperationException {
		String anotherIban = "iban2";
		
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(anotherIban)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(IBAN).getBalance()).thenReturn(1000);
		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(anotherIban)).thenReturn(true);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		MBWayAccount acc2 = new MBWayAccount(anotherIban, 912345678, this.mockServices);
		
		acc2.validateAccount(acc2.getConfirmationCode());
		
		try {
			acc.transferMoney(acc2, 1000);
			fail();
		} catch (Exception e) {
			assertEquals("One or more MBWay Accounts are not confirmed!", e.getMessage());
		}
	}
	
	@Test
	public void mbwaySplitBillMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		String iban2 = "iban2";
		String iban3 = "iban3";
		int phoneNumber2 = 912345678;
		int phoneNumber3 = 987123456;
		
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(iban2)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(iban3)).thenReturn(mock(Account.class));

		when(this.mockServices.getAccountByIban(iban2).getBalance()).thenReturn(1000);
		when(this.mockServices.getAccountByIban(iban3).getBalance()).thenReturn(1000);
		
		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(iban2)).thenReturn(true);
		when(this.mockServices.accountExists(iban3)).thenReturn(true);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		MBWayAccount acc2 = new MBWayAccount(iban2, phoneNumber2, this.mockServices);
		MBWayAccount acc3 = new MBWayAccount(iban3, phoneNumber3, this.mockServices);
		
		acc.validateAccount(acc.getConfirmationCode());
		acc2.validateAccount(acc2.getConfirmationCode());
		acc3.validateAccount(acc3.getConfirmationCode());
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER, 50);
		friends.put(phoneNumber2, 50);
		friends.put(phoneNumber3, 50);
		
		acc.splitBill(friends, 150);
	}
	
	@Test (expected=MBWayException.class)
	public void wrongQuantityFriendsMbwaySplitBillMethodTest() throws MBWayException, SibsException, AccountException, OperationException {
		String iban2 = "iban2";
		String iban3 = "iban3";
		int phoneNumber2 = 912345678;
		int phoneNumber3 = 987123456;
		
		when(this.mockServices.getAccountByIban(IBAN)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(iban2)).thenReturn(mock(Account.class));
		when(this.mockServices.getAccountByIban(iban3)).thenReturn(mock(Account.class));

		when(this.mockServices.getAccountByIban(iban2).getBalance()).thenReturn(1000);
		when(this.mockServices.getAccountByIban(iban3).getBalance()).thenReturn(1000);
		
		when(this.mockServices.accountExists(IBAN)).thenReturn(true);
		when(this.mockServices.accountExists(iban2)).thenReturn(true);
		when(this.mockServices.accountExists(iban3)).thenReturn(true);
		
		MBWayAccount acc = new MBWayAccount(IBAN, PHONE_NUMBER, this.mockServices);
		MBWayAccount acc2 = new MBWayAccount(iban2, phoneNumber2, this.mockServices);
		MBWayAccount acc3 = new MBWayAccount(iban3, phoneNumber3, this.mockServices);
		
		acc.validateAccount(acc.getConfirmationCode());
		acc2.validateAccount(acc2.getConfirmationCode());
		acc3.validateAccount(acc3.getConfirmationCode());
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER, 50);
		friends.put(phoneNumber2, 50);
		friends.put(phoneNumber3, 50);
		
		acc.splitBill(friends, 200);
	}
		
	
	@After
	public void tearDown() {
		this.mockServices = null;
		MBWayAccount.clearMBWayAccounts();
	}
}
