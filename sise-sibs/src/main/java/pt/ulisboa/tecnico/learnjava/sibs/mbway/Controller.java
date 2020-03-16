package pt.ulisboa.tecnico.learnjava.sibs.mbway;

public class Controller {
	private MBWayAccount model;
	private View view;
	
	public Controller(MBWayAccount model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public int getPhoneNumer() {
		return this.model.getPhoneNumber();
	}
	
	public String getIban() {
		return this.model.getIban();
	}
	
	public void setPhoneNumer(int phoneNumber) {
		this.model.setPhoneNumber(phoneNumber);
	}
	
	public void setIban(String iban) {
		this.model.setIban(iban);
	}
	
}
