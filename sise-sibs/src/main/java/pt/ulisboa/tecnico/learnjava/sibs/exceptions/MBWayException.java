package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class MBWayException extends Exception{
	private String message;
	
	public MBWayException(String message) {
		this.message = message;
	}
	
	public MBWayException() {
	
	}
	
	public String getMessage() {
		return this.message;
	}

}
