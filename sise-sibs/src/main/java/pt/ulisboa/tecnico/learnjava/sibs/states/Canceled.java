package pt.ulisboa.tecnico.learnjava.sibs.states;

public class Canceled extends State{
	private static Canceled instance = new Canceled();
	
	public Canceled() {
		super();
	}
	
	public static State instance() {
		return instance;
	}
}
