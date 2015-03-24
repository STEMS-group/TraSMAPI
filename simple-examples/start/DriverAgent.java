package start;

import java.util.Random;

import trasmapi.genAPI.Edge;
import trasmapi.genAPI.Vehicle;
import trasmapi.genAPI.exceptions.UnimplementedMethod;
import trasmapi.sumo.SumoCom;
import trasmapi.sumo.SumoEdge;
import trasmapi.sumo.SumoRoute;
import trasmapi.sumo.SumoVehicle;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class DriverAgent extends Agent{

	private static final long serialVersionUID = 6095960260125307076L;

	private int id;

	private String origin;
	private String destination;

	Vehicle vehicle;

	private boolean goingTripDone = false;
	private boolean returnTripDone = false;

	public static Random rand;

	MessageTemplate inform  = MessageTemplate.MatchPerformative( ACLMessage.INFORM );

	public DriverAgent(int idDriver) {

		super();

		try {

			this.id= idDriver;

			origin = SumoCom.edgesIDs.get(rand.nextInt(SumoCom.edgesIDs.size()));

			do{
				destination = SumoCom.edgesIDs.get(rand.nextInt(SumoCom.edgesIDs.size())); 
			}while(origin.equals(destination));


			String vehicleType = SumoCom.vehicleTypesIDs.get(rand.nextInt(SumoCom.vehicleTypesIDs.size()));
			String routeId = SumoCom.getRouteId(origin, null);
			int departureTime = 0;
			double departPosition = 0;
			double departSpeed = 0;
			byte departLane = 0;


			vehicle = new SumoVehicle(id, vehicleType, routeId, departureTime, departPosition, departSpeed, departLane);

			SumoCom.addVehicle((SumoVehicle)vehicle);

			SumoCom.addVehicleToSimulation((SumoVehicle)vehicle);

			vehicle.changeTarget(destination);

		} catch (UnimplementedMethod e) {
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

		sd.setType("Driver");
		System.out.println("Tipo: "+sd.getType()+"\n\n\n");

		ad.addServices(sd); 

		try {
			DFService.register(this, ad);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		addBehaviour(new SimpleBehaviour() {

			private static final long serialVersionUID = 7099828445735670474L;

			public boolean done() {

				if(goingTripDone && returnTripDone){
					System.out.println("END TRIP - "+getLocalName());
					return true;
				}
				return false;
			}

			@Override
			public void action() {

				if(vehicle.arrived)
				{
					if(!goingTripDone){
						try {

							System.out.println("CHEGUEI!! - "+getLocalName());
							goingTripDone = true;

							String temp = destination;
							destination = origin;
							origin = temp;

							vehicle.routeId = SumoCom.getRouteId(origin, null);
							vehicle.departTime = SumoCom.getCurrentSimStep();

							SumoCom.addVehicleToSimulation((SumoVehicle)vehicle);

							vehicle.arrived = false;

							vehicle.changeTarget(destination);


						} catch (UnimplementedMethod e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}


					}
					else
						returnTripDone = true;
				}
				block();

			}
		});

		addBehaviour(new CyclicBehaviour() {

			private static final long serialVersionUID = 1L;

			@Override
			public void action() {

				try {
					ACLMessage msg = receive(inform);

					if (msg!=null){
						if(msg.getContent().startsWith("ACCIDENT")){

						//	System.out.println("ACCIDENT Received "+ getLocalName());
							String[] tokens = msg.getContent().trim().split("-");

							String edgeId = tokens[1].trim();

							vehicle.setEdgeTravelTime(edgeId, 12000);
							vehicle.rerouteByTravelTime();
						}
						else
							if(msg.getContent().startsWith("NOACCIDENT")){

								String[] tokens = msg.getContent().trim().split("-");

								String edgeId = tokens[1].trim();

								vehicle.setEdgeTravelTime(edgeId);
								vehicle.rerouteByTravelTime();
							}
					}

				} catch (UnimplementedMethod e) {
					e.printStackTrace();
				}

				block();
			}
		});

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

	//
	//	System.out.println("############################################################"    +  SumoCom.getAllRoutesIds());
	//	/*switch(state ){
	//	case 1:
	//		//change target
	//		vehicle.changeTarget("1");
	//		System.out.println("Target Changed");
	//		state = 2;
	//		break;
	//	case 2:*/
	//	//get edges travelTimes
	//	Edge e32 = new SumoEdge("32");
	//	Edge e25 = new SumoEdge("25");
	//	/*Edge e22 = new SumoEdge("22");
	//		Edge e20 = new SumoEdge("20");
	//
	//		double tte25 = e25.getGlobalTravelTime();
	//		double tte22 = e22.getGlobalTravelTime();
	//		double tte20 = e20.getGlobalTravelTime();*/
	//	double tte32 = e32.getGlobalTravelTime(SumoCom.currentSimStep/1000);
	//	double cte32 = e32.getCurrentTime();
	//	System.out.println("\ncurrentSimStep   : "+ SumoCom.currentSimStep);
	//	System.out.println("\nGlobalTravelTime 32 : "+ tte32);
	//	System.out.println("CurrentTime 32     : "+ cte32);
	//
	//	double globalTime25 = e25.getGlobalTravelTime(SumoCom.currentSimStep/1000);
	//	double currentTime25 = e25.getCurrentTime();
	//	System.out.println("\nGlobalTravelTime 25 : "+ globalTime25);
	//	System.out.println("CurrentTime 25     : "+ currentTime25);
	//
	//	vehicle.changeTarget("20");
	//	System.out.println("Target Changed");
	//
	//	System.out.println("############################################################"    +  SumoCom.getAllRoutesIds());
	//
	//	vehicle.rerouteByTravelTime();
	//
	//	vehicle.setEdgeTravelTime(0,10000,"25",3600.0);
	//	//	e25.setGlobalTravelTime(0,10000,3600.0);
	//
	//	vehicle.rerouteByTravelTime();
	//	System.out.println("############################################################"    +  SumoCom.getAllRoutesIds());
	//
	//	/*case 3:
	//		//reroute
	//		vehicle.rerouteByTravelTime();
	//		System.out.println("ReRoute Done");
	//		state = 3;
	//		break;
	//	}*/
	//


}
