package start;

import java.util.Random;

import trasmapi.sumo.SumoCom;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ODManager extends Agent{

	private static final long serialVersionUID = 2724654562866411374L;
	private ContainerController mainContainer;

	private static String accidentEdge = null;

	private static boolean sendAccident = false;
	private static boolean sendNoAccident = false;

	private boolean doneBehaviour = false;

	private final int numDrivers = 500;

	protected boolean sentAccidentMessage = false;

	protected boolean sentNoAccidentMessage;

	public ODManager(ContainerController mainContainer) {
		this.mainContainer = mainContainer;
	}

	public void addDrivers(){

		SumoCom.createAllRoutes();

		DriverAgent.rand = new Random(423423);

		try {

			for(int i=0; i<numDrivers; i++)
				mainContainer.acceptNewAgent("DRIVER#"+i, new DriverAgent(i)).start();


			AccidentGUI accident = new AccidentGUI(numDrivers);
			accident.setVisible(true);

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}


	}

	public void sendMessage(String message) {
		//	SumoVehicle.vehicleCommands.clear();

		ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
		msg.setContent(message);

		addReceivers(msg);
		//addReceiversByType(msg, "Driver");

		send(msg);
	}

	private void addReceivers(ACLMessage msg) {
		for(int i=0; i< numDrivers;i++)
			msg.addReceiver(new AID("DRIVER#"+i,AID.ISLOCALNAME));
	}

	public static void setAccident(String EdgeId) {

		sendAccident  = true;
		accidentEdge = EdgeId;

	}

	public static void setNoAccident(String EdgeId) {

		sendNoAccident = true;
		accidentEdge = EdgeId;

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

		addBehaviour(new Behaviour() {

			private static final long serialVersionUID = -683154111370952801L;

			@Override
			public boolean done() {
				return doneBehaviour;
			}

			@Override
			public void action() {
				//	System.out.println("ODM action");
				if(sendAccident){
					sendMessage("ACCIDENT-"+accidentEdge);
					sendAccident = false;
				}

				if(sendNoAccident){
					sendMessage("NOACCIDENT-"+accidentEdge);
					sendNoAccident = false;
				}
			}
		});

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

	public static void trackVehicle(int selectedIndex) {


	}

	public void addPolygon() {

		try {

			mainContainer.acceptNewAgent("INFORMER#1", new InfoAgent(1306.84,132.25)).start();
			mainContainer.acceptNewAgent("INFORMER#2", new InfoAgent(1886.41,124.31)).start();
			mainContainer.acceptNewAgent("INFORMER#3", new InfoAgent(2743.54,121.58)).start();
			mainContainer.acceptNewAgent("INFORMER#4", new InfoAgent(2268.27,318.49)).start();
			mainContainer.acceptNewAgent("INFORMER#5", new InfoAgent(2862.28,294.92)).start();
			mainContainer.acceptNewAgent("INFORMER#6", new InfoAgent(3569.28,116.12)).start();
			mainContainer.acceptNewAgent("INFORMER#7", new InfoAgent(3610.23,303.11)).start();
			mainContainer.acceptNewAgent("INFORMER#8", new InfoAgent(438.46,119.64)).start();
			mainContainer.acceptNewAgent("INFORMER#9", new InfoAgent(823.46,271.95)).start();

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}


	}

}
