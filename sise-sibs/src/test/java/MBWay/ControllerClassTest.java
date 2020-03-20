package MBWay;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.Controller;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MBWayAccount;
import pt.ulisboa.tecnico.learnjava.sibs.mbway.MVC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.stream.Stream;

import org.junit.After;

public class ControllerClassTest {
	private int PHONE_NUMBER = 912345678;
	
	private Controller controller;
	private String iban;
	
	@Before
	public void setUp() {
		controller = new Controller();
		iban = "ola";
	}
	
	@Test
	public void createMBWayAccountTest() {
		controller.createMBWay(iban, PHONE_NUMBER);
		MBWayAccount account = MBWayAccount.getMBWayAccount(PHONE_NUMBER);
		assertEquals(iban, account.getIban());
	}
	
	@Test
	public void accoutnExistsTest() {
		MBWayAccount account = MBWayAccount.getMBWayAccount(PHONE_NUMBER);
		assertTrue(controller.accountExists(PHONE_NUMBER));
	}
	
	@Test
	public void confirmMBWayAccountTest() throws MBWayException {
		MBWayAccount account = new MBWayAccount(iban, PHONE_NUMBER);
		
		controller.confirmAccount(PHONE_NUMBER, account.getConfirmationCode());
		
		assertTrue(account.isConfirmed());
	}
	
	@Test
	public void transferMoneyTest() throws MBWayException, SibsException, AccountException, OperationException {
		MBWayAccount account = new MBWayAccount(iban, PHONE_NUMBER);
		MBWayAccount account2 = new MBWayAccount("ola", 999999999);
		
		controller.transfer(PHONE_NUMBER, 999999999, 100);
	}
	
	
	@After
	public void tearDown() {
		controller = null;
	}
}
