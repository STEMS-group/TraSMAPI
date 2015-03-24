package trasmapi.genAPI;

import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class Junction {

	/**
	 * method used to retrieve all the ids from all the traffic lights in the loaded network
	 * @return String[] - a String array with all the ids (String)
	 */
	public static Junction[] getJunctionsList() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}

}
