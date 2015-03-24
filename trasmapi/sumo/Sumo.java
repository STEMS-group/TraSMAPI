package trasmapi.sumo;


import java.io.IOException;
import java.util.List;

import trasmapi.genAPI.Simulator;
import trasmapi.genAPI.exceptions.TimeoutException;

public class Sumo extends Simulator {
	
	String simulator;
	SumoCom comm;
	
	public Sumo(String sim){
		simulator = sim;	
		comm = new SumoCom();
	}
	
	public void launch(){
		comm.launch(simulator);
	}
	
	public void connect() throws TimeoutException{
		comm.connect();
	}

	public void close() throws IOException {
		comm.close();
	}

	public boolean simulationStep(int k) {
		return comm.simStep(k);
	}

	public void addParameters(List<String> paramsP) {
		comm.params = paramsP;
	}

	public void addConnections(String add, int portP){
		comm.port = portP;
	}
	
	public void start(){
		comm.start(0);
	}
}
