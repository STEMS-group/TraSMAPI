package trasmapi.genAPI.exceptions;


@SuppressWarnings("serial")
public class UnimplementedMethod extends Exception {
	
	public UnimplementedMethod() {
		
	}
	
	public String toString() {
		return "Unimplemented Method";
	}
}
