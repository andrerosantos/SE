package pt.ulisboa.tecnico.learnjava.sibs.states;

public class Completed extends State{
	private static Completed instance = new Completed();
	
	public Completed() {
		super();
	}
	
	public static State instance() {
		return instance;
	}

}
