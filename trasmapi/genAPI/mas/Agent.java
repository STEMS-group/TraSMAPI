package trasmapi.genAPI.mas;

import java.util.LinkedList;

public abstract class Agent implements Runnable {
	protected Mas mas;
	private int myId;
	private boolean readyStatus;
	private LinkedList<Message> messages;
	
	protected Agent() {
		readyStatus = true;
		messages = new LinkedList<Message>();
	}
	
	synchronized final boolean isReady() {
		return readyStatus;
	}
	
	synchronized final void setReady(boolean k) {
		readyStatus = k;
	}
	
	public abstract void action();
	
	public abstract void newMessage();
	
	public abstract void init();
	
	public final void run() {
		init();
		while (true) {
			if (messages.isEmpty())
				action();
			if (Thread.interrupted())
				newMessage();
			setReady(true);
			try {
				while (true) 
					Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				if (!messages.isEmpty()) {
					newMessage();
				}
			}			
		}
	}
	
	final void reportTo(Mas mas) {
		this.mas = mas;
	}
	
	final void setId(int k) {
		myId = k;
	}
	
	/**
	 * Returns the agent id.
	 * @return the agent id
	 */
	public final int getId() {
		return myId;
	}
	
	/**
	 * Returns the oldest message in the message queue
	 * @return the oldest message
	 */
	public final synchronized Message getMessage() {
		return messages.poll();
	}
	
	/**
	 * Sends a message to a given agent
	 * @param message the message to be sent
	 * @param agentId the agent id
	 */
	public final synchronized void sendMessage(Message message, int agentId) {
		message.from = myId;
		message.to = agentId;
		mas.send(message);
	}
	
	/**
	 * Broadcasts a message to every agent registered with the Multi-Agent System.
	 * @param message the broadcasted message
	 */
	public final synchronized void broadcast(Message message) {
		message.from = myId;
		mas.broadcast(message, myId);
	}
	
	final synchronized void deliverMessage(Message m) {
		messages.add(m);
	}
	
	/**
	 * Returns the number of unread messages.
	 * @return the number of unread messages
	 */
	public final synchronized int getNumMessages() {
		return messages.size();
	}
	
	/**
	 * Returns the current time step.
	 * @return the time step
	 */
	public final int getTimestep() {
		return mas.getTimeStep();
	}
}
