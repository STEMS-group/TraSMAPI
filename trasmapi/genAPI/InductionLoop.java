package trasmapi.genAPI;

import trasmapi.genAPI.exceptions.UnimplementedMethod;

public class InductionLoop {
	protected String id;
	
	/**
	 * method used to retrieve the String id of the induction loop
	 * @return the induction loop id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * standard comparison method, lexicographical by id
	 * @param a the other induction loop
	 * @return wheter the 2 induction loops are equal
	 */
	public boolean equals(InductionLoop a) {
		return id.equals(a.getId());
	}
	
	/**
	 * method used to retrieve all the induction loops in the loaded network
	 * @return InductionLoop[] - an array with all the induction loops
	 */
	public static InductionLoop[] getInductionLoopList() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to save the vehicle list so that "getNumNewVehicles" is able to see the difference.
	 */
	public void saveVehicle() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to retrieve the number of vehicles that "touched" the induction loop in the last induction loop read cycle
	 * @return number of vehicles
	 */
	public int getNumVehicles() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	* method used to retrieve the number of vehicles that have entered the detector in the last induction loop read cycle
	* There needs to have been a call to "saveVehicleIds" before the first call to getNumNewVehicles.
	* @return number of vehicles
	*/
	public int getNumNewVehicles() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to retrieve all the ids from all the vehicles that were on the induction loop in the last simulation step 
	 * @return String[] - a String array with all the ids (String)
	 */
	public Vehicle[] getVehiclesList() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to retrieve the occupancy of the induction loop
	 * @return last step occupancy (%) of the induction loop
	 */
	public double getOccupancy() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to retrieve the time since last detection
	 * @return time since last detection
	 */
	public double getTimeSinceLastDetection() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
	
	/**
	 * method used to return the mean velocity of (all) the vehicles passing over this induction loop in the last simulation step
	 * @return mean velocity of (all) the vehicles passing over this induction loop in the last simulation step
	 */
	public double getMeanVelocity() throws UnimplementedMethod {
		throw new UnimplementedMethod();
	}
}
