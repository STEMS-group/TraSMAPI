
package trasmapi.sumo.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class RequestMessage {

	private ArrayList<Command> commands = new ArrayList<Command>();

	public void addCommand(Command c) {
		if (c != null)
			commands.add(c);
	}

	public void sendRequestMessage(DataOutputStream out) throws IOException {
		
		int totalLen = Integer.SIZE / 8; // the length header

		for (Command cmd : commands)
			totalLen += cmd.length();
		
		out.writeInt(totalLen);


		for (Command cmd : commands) 
			cmd.sendCommand(out);
		
	}
}
