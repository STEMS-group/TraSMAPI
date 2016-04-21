package trasmapi.genAPI.mas;

public abstract class Message {
	protected int from, to;

	/**
	 * Returns the author of the message
	 * @return the agent id
	 */
	public final int getFrom() {
		return from;
	}
	
	/**
	 * Returns to destination of the message
	 * @return the agent id
	 */
	public final int getTo() {
		return to;
	}
}
