package pt.ulisboa.tecnico.learnjava.sibs.states;

public class Deposited extends State{
	private static Deposited instance = new Deposited();
	
	public Deposited() {
		super();
	}
	
	public static State instance() {
		return instance;
	}
}
