package client.customexceptions;

public class IllegalConversionException extends RuntimeException {
	/**
	 * This unchecked exception gets thrown if the program attempts to convert
	 * between incompatible types in the serverCompatibility package
	 * 
	 * @param message
	 */
	public IllegalConversionException(String message) {
		super("Attempted conversion between incompatible types: " + message);
	}
}
