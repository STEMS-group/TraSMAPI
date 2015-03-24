package trasmapi.genAPI.exceptions;

public class WrongCommand extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg; 
	
	public WrongCommand(String s) {
		msg = s;
	}
	
	public String toString() {
		String s = "ERROR: "+msg+"\n";
		return s;
	}
}
