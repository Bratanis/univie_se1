package client.customexceptions;

public class MapNavigationException extends RuntimeException {

	/**
	 * This unchecked exception gets thrown if the bot fails to find an appropriate
	 * route throughout the map
	 * 
	 * @param message
	 */
	public MapNavigationException(String message) {
		super("No viable route could be calculated: " + message);
	}
}
