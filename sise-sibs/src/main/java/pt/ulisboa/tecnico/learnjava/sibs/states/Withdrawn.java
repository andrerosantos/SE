package pt.ulisboa.tecnico.learnjava.sibs.states;

public class Withdrawn extends State{
	private static Withdrawn instance = new Withdrawn();
	
	public Withdrawn() {
		super();
	}

	public static State instance() {
		return instance;
	}
}
