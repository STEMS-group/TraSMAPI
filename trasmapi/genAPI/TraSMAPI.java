package trasmapi.genAPI;


import java.io.IOException;
import java.net.UnknownHostException;
import trasmapi.genAPI.exceptions.TimeoutException;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.*;

public class TraSMAPI {
	
	static Simulator sim;
	

	/**
	 * Initializes TraSMAPI for the given simulator
	 */
	public TraSMAPI() {
	}

	/**
	 * Initializes TraSMAPI for the given simulator
	 * @param simulator - simulator identification tag
	 * @throws IOException
	 */
	public TraSMAPI(String simulator) {
		if (simulator=="sumo" || simulator=="guisim") {
			sim = new Sumo(simulator);
		}
	}
	/**
	 * launches the Simulator Process
	 * @throws IOException
	 * @throws UnimplementedMethod 
	 */
	public void launch() throws IOException, UnimplementedMethod {
		sim.launch();
	}
	
	/**
	 * method used to connect to the simulator (using sockets)
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws TimeoutException
	 * @throws UnimplementedMethod 
	 */
	public void connect() throws UnknownHostException, IOException, TimeoutException, UnimplementedMethod {
		sim.connect();
	}
	
	/**
	 * method used to close the simulator
	 * @throws UnimplementedMethod 
	 * @throws IOException 
	 */
	public void close() throws UnimplementedMethod, IOException {
		sim.close();
		sim = null;
	}

	// SIMULATION MANAGEMENT \\
	/**
	 * method used to advance k steps in the simulation
	 * @param k - number of simulation steps to be simulated
	 * @throws UnimplementedMethod 
	 */
	public boolean simulationStep(int k) throws UnimplementedMethod {
		return sim.simulationStep(k);
	}
	
	public void addSimulator(Simulator simP) {
		TraSMAPI.sim = simP;
	}

	public void start() throws UnimplementedMethod {
		sim.start();
	}
	
	
}
