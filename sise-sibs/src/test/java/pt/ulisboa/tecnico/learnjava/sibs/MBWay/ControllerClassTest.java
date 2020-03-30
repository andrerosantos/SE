package pt.ulisboa.tecnico.learnjava.sibs.MBWay;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.Controller;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MBWayAccount;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MVC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.After;

public class ControllerClassTest {
	private final int PHONE_NUMBER_1 = 912345678;
	private final int PHONE_NUMBER_2 = 987654321;
	private final int PHONE_NUMBER_3 = 978456123;
	private final int PHONE_NUMBER_4 = 932165487;
	
	private Services mockServices;
	private Controller controller;
	private Bank bpi;
	private Client client1;
	private Client client2;
	private Client client3;
	private Client client4;
	private String iban1;
	private String iban2;
	private String iban3;
	private String iban4;
	
	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		this.mockServices = mock(Services.class);
		
		this.controller = new Controller(this.mockServices);
		this.bpi = new Bank("BPI");
		
		this.client1 = new Client(bpi, "John", "Doe", "123456789", "987654321", "Street", 25);
		this.client2 = new Client(bpi, "Jane", "Doe", "987654321", "123456789", "Street", 25);
		this.client3 = new Client(bpi, "John", "Smith", "789456123", "321654987", "Street", 25);
		this.client4 = new Client(bpi, "Jane", "Smith", "321654987", "789456123", "Street", 25);
		
		this.iban1 = bpi.createAccount(Bank.AccountType.CHECKING, client1, 1000, 0);
		this.iban2 = bpi.createAccount(Bank.AccountType.CHECKING, client2, 1000, 0);
		this.iban3 = bpi.createAccount(Bank.AccountType.CHECKING, client3, 1000, 0);
		this.iban4 = bpi.createAccount(Bank.AccountType.CHECKING, client4, 1000, 0);
	}
	
	@Test
	public void createMBWayAccountTest() throws MBWayException {
		controller.createMBWay(iban1, PHONE_NUMBER_1);
		MBWayAccount account = MBWayAccount.getMBWayAccount(PHONE_NUMBER_1);
		assertEquals(iban1, account.getIban());
	}
	
	@Test
	public void accountExistsTest() throws MBWayException {
		MBWayAccount account = new MBWayAccount(iban1, PHONE_NUMBER_1);
		assertTrue(controller.accountExists(PHONE_NUMBER_1));
	}
	
	@Test
	public void confirmMBWayAccountTest() throws MBWayException {
		MBWayAccount account = new MBWayAccount(iban1, PHONE_NUMBER_1);
		
		controller.confirmAccount(PHONE_NUMBER_1, account.getConfirmationCode());
		
		assertTrue(account.isConfirmed());
	}
	
	@Test
	public void transferMoneyTest() throws MBWayException, SibsException, AccountException, OperationException {
		MBWayAccount account = new MBWayAccount(iban1, PHONE_NUMBER_1);
		MBWayAccount account2 = new MBWayAccount(iban2, PHONE_NUMBER_2);
		
		controller.transfer(PHONE_NUMBER_1, 999999999, 100);
		
		// this test is not verifying anything!!
		fail();
		 
	}
	
	@Test
	public void splitBillSuccess() throws MBWayException, SibsException, AccountException, OperationException {
		MBWayAccount account = new MBWayAccount(iban1, PHONE_NUMBER_1);
		MBWayAccount account2 = new MBWayAccount(iban2, PHONE_NUMBER_2);
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER_1, 500);
		friends.put(PHONE_NUMBER_2, 500);
		
		controller.splitBill(2, 1000, friends, PHONE_NUMBER_1);
		
		
		assertEquals(1500, this.services.getAccountByIban(iban1).getBalance());
		assertEquals(500, this.services.getAccountByIban(iban2).getBalance());
	}
	
	@Test
	public void splitBillNotEnoughMoney() throws MBWayException, SibsException, AccountException, OperationException {
		MBWayAccount account = new MBWayAccount(iban1, PHONE_NUMBER_1);
		MBWayAccount account2 = new MBWayAccount(iban2, PHONE_NUMBER_2);
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		friends.put(PHONE_NUMBER_1, 0);
		friends.put(PHONE_NUMBER_2, 1001);
		
		controller.splitBill(2, 1001, friends, PHONE_NUMBER_1);
		
		assertEquals(1000, this.services.getAccountByIban(iban1).getBalance());
		assertEquals(1000, this.services.getAccountByIban(iban2).getBalance());
	}
	
	
	@After
	public void tearDown() {
		controller = null;
		bpi = null;
		Bank.clearBanks();
		MBWayAccount.clearMBWayAccounts();
	}
}
