package trasmapi.genAPI.exceptions;

public class WrongVariableTypeInMessage extends Exception {
	private static final long serialVersionUID = 1L;
	private String msg; 
	
	public WrongVariableTypeInMessage(String s) {
		msg = s;
	}
	
	public String toString() {
		String s = "ERROR: "+msg+"\n";
		return s;
	}
}
