package client.customexceptions;

public class UserInputException extends Exception {
	/**
	 * Checked exception that gets thrown if the user inserts wrong data when
	 * initializing the client (invalid String args in main)
	 * 
	 * @param message
	 */
	public UserInputException(String message) {
		super("An error occured while attempting to communicate with server: " + message);
	}
}
