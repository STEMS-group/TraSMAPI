package tlTest;

import java.util.ArrayList;

import start.DriverAgent;
import trasmapi.genAPI.TrafficLight;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.SumoTrafficLight;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class TLManager extends Agent{


	private static final long serialVersionUID = 1L;
	private ContainerController mainContainer;
	TLManagerGUI managerGUI;

	public TLManager(ContainerController mainContainer) {
		this.mainContainer = mainContainer;
	}


	protected void setup() {

		DFAgentDescription ad = new DFAgentDescription();
		ad.setName(getAID()); //agentID
		System.out.println("AID: "+ad.getName());

		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName()); //nome do agente    
		System.out.println("Nome: "+sd.getName());

		sd.setType("Manager");
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

	public void loadTls() throws InterruptedException {

		//	try {

		ArrayList<String> tlsIds = new ArrayList<String>();

		tlsIds = SumoTrafficLight.getIdList();

		managerGUI = new TLManagerGUI(tlsIds, this);

		managerGUI.setVisible(true);
	}


	public void focus(String selectedItem) {



	}


	public void createNewManager(String tlID) {

		try {

			mainContainer.acceptNewAgent("TL#"+tlID, new TlAgent(tlID)).start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}


}
