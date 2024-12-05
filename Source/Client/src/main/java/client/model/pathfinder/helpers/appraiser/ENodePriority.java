package client.model.pathfinder.helpers.appraiser;

public enum ENodePriority {

	/**
	 * Field is water
	 */
	Forbidden,

	/**
	 * The node is walkable but visited
	 */
	VeryLow,

	/**
	 * The node is 1)&2) a grass field, or 3) a walkable field (not Water) opposite
	 * to the enemy direction
	 */
	Low,

	/**
	 * The node is 1)&2) a mountain field, or 3) a mountain field, neutral in
	 * regards to the enemy direction
	 */
	Medium,

	/**
	 * The node is 1) near a treasure, 2) near a castle, or 3) is a grass field,
	 * neutral in regards to the enemy direction
	 */
	High,

	/**
	 * The node has 1) a treasure, 2) a castle , or 3) is a walkable field in the
	 * direction of the enemy territory
	 */
	VeryHigh;

	// public static ENodePriority findHighestPriority(Collection<ENodePriority>
	// priorities) {
	// return priorities.stream()
	// .min(Enum::compareTo) // Use natural order
	// .orElse(null);
	// }
}
