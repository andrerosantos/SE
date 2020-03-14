package pt.ulisboa.tecnico.learnjava.sibs.states;

public class Registered extends State{
	private static Registered instance = new Registered();
	
	public Registered() { }

	public static State instance() {
		return instance;
	}
}
