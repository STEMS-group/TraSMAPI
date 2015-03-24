
package trasmapi.sumo.protocol;

import java.io.IOException;

public class StatusResponse {

	private int length;
	private int identifier;
	private int result;
	private String description = "";

	public StatusResponse(Buffer in) throws IOException {

		length = in.readByte();
		
		identifier = in.readByte();
		
		result = in.readByte();
		
		int stringSize = in.readInt();
		
		if(stringSize != 0){

			for(int j=0; j<stringSize; j++)
				description += (char) in.readByte();

		}
	}

	public int getLength() {
		return length;
	}
	/**
	 * @return the identifier
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void print(String prefix) {

		System.out.println(prefix + "++++ Status ++++");
		System.out.println(prefix + "statusLength : " + length);
		System.out.println(prefix + "cmdResponse : " + Integer.toString( identifier & 0xFF, 16) + " ");
		System.out.println(prefix + "result : " + Integer.toString( result & 0xFF, 16) + " ");
		System.out.println(prefix + description);
		
	}

}
