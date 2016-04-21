package trasmapi.genAPI.mas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Mas {
	private int nextAgent = 0;
	private List<Agent> agents;
	private List<Thread> threads;
	
	private int timestep = 0;
	
	ArrayList<LinkedList<Message>> messages;
	
	public Mas() {
		agents = new ArrayList<Agent>();
		threads = new ArrayList<Thread>();
		messages = new ArrayList<LinkedList<Message>>();
	}
	
	/**
	 * Adds a new agent to the simulation
	 * @param newAgent the agent to be added
	 */
	public void addAgent(Agent newAgent) {
		newAgent.reportTo(this);
		newAgent.setId(nextAgent++);
		threads.add(new Thread(newAgent));
		agents.add(newAgent);
		messages.add(new LinkedList<Message>());
	}

	/**
	 * Starts the Multi-Agent system.
	 * Agents are run individually and its init() method is called
	 */
	public void start() {
		for (Thread a : threads)
			a.start();
	}
	
	/**
	 * Returns the number of active agents in the Multi-Agent System
	 * @return the number of active agents
	 */
	public int getNumAgents() {
		return agents.size();
	}
	
	/**
	 * Returns an array of ids identifying the active agents in the Multi-Agent System
	 * @return an array of ids identifying the active agents
	 */
	public int[] getAgents() {
		int[] v = new int[agents.size()];
		for (int i = 0; i<agents.size(); i++) {
			v[i] = agents.get(i).getId();
		}
		return v;
	}
	
	/**
	 * Closes the Multi-Agent System, killing every agent.
	 * Please note that agents must save important information before this method is called.
	 */
	@SuppressWarnings("deprecation")
	public void close() {
		for (Thread a : threads)
			a.stop(); // stop is deprecated, but the reason isn't real issue in your case  
	}
	
	/**
	 * Returns the current time step
	 * @return the current time step
	 */
	public int getTimeStep() {
		return timestep;
	}
	
	/**
	 * advances N time steps in the simulation
	 * @param k the number of timesteps
	 */
	public void step(int k) {
		for (int ss = 0; ss < k; ss++) {
			timestep++;
			for (int i = 0; i<threads.size(); i++) {
				agents.get(i).setReady(false);
				threads.get(i).interrupt();
			}
			
			/*
			 * could be made faster by checking the last agent that was not ready
			 * instead of starting from the beginning
			 */
			boolean someoneNotReady;
			do {
				someoneNotReady = false;
				sendMessages();
				for (int i = 0; i<agents.size(); i++)
					if (!agents.get(i).isReady() && threads.get(i).isAlive()) {
						someoneNotReady = true;
						break;
					}
				if (someoneNotReady)
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				
			} while (someoneNotReady && !areThereNewMessages());
		}
	}

	synchronized void broadcast(Message message, int id) {
		for (int i = 0; i<agents.size(); i++) {
			if (agents.get(i).getId() != id) {
				message.to = agents.get(i).getId();
				send(message);
			}
		}
	}	
	
	synchronized void send(Message m) {
		messages.get(m.to).add(m);
	}
	
	synchronized private void sendMessages() {
		for (int i = 0; i<agents.size(); i++) {
			if (!messages.get(i).isEmpty()) {
				for (Message m : messages.get(i)) {
					agents.get(i).deliverMessage(m);
				}
				messages.get(i).clear();
				threads.get(i).interrupt();
			}
		}
	}

	synchronized private boolean areThereNewMessages() {
		for (LinkedList<Message> v : messages) {
			if (!v.isEmpty())
				return true;
		}
		return false;
	}
}
