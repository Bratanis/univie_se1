package client.customexceptions;

/**
 * Used for handling the cases when the server does not send what the client expects
 */
public class ServerCommunicationException extends RuntimeException {
	public ServerCommunicationException (String message){
		super("Server communication error: " + message);
	}
}

