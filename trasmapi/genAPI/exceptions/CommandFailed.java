package trasmapi.genAPI.exceptions;

public class CommandFailed extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public CommandFailed(String s) {	
		msg = s;
	}
	
	public String toString() {
		String s = "ERROR: "+msg+"\n";
		return s;
	}
}
