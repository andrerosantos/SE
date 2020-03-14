package pt.ulisboa.tecnico.learnjava.sibs.states;

public class State {
	private static State instance = new State();
	
	public State() {
		
	}

	public static State instance() {
		return instance;
	}
}
