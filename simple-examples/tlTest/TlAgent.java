package tlTest;

import java.util.ArrayList;

import trasmapi.genAPI.TrafficLight;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.SumoCom;
import trasmapi.sumo.SumoTrafficLight;
import trasmapi.sumo.SumoVehicle;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class TlAgent extends Agent{


	private static final long serialVersionUID = 6095960260125307076L;

	private String id;
	public TrafficLight tl;

	private TLGUI tlGUI;

	public ArrayList<String> controlledLanes;



	public TlAgent(String tlID) {

		super();

		try {
			
			System.out.println("TlAgent id: "+this.id);

			tl = new SumoTrafficLight(tlID);

			controlledLanes = tl.getControlledLanes();

			tlGUI = new TLGUI(this);

			tlGUI.setVisible(true);
			
		} catch (UnimplementedMethod e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {

		DFAgentDescription ad = new DFAgentDescription();
		ad.setName(getAID()); //agentID
		System.out.println("AID: "+ad.getName());

		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName()); //nome do agente    
		System.out.println("Nome: "+sd.getName());

		sd.setType("TrafficLightManager");
		System.out.println("Tipo: "+sd.getType()+"\n\n\n");

		ad.addServices(sd); 

		try {
			DFService.register(this, ad);
		} catch(FIPAException e) {
			e.printStackTrace();
		}


		super.setup();
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		super.takeDown();
	}

}
