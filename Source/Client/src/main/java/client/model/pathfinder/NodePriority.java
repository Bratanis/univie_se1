package client.model.pathfinder;

import java.util.Collection;
import java.util.Comparator;

public enum NodePriority {
	
	/** 
	 * The node has 1) a treasure, 2) a castle , or 3) is a walkable field in the direction of the enemy territory
	 */
	VeryHigh(4), 
	
	/**
	 * The node is 1) near a treasure, 2) near a castle, or 3) is a grass field, neutral in regards to the enemy direction
	 */
	High(3),
	
	/**
	 * The node is 1)&2) a mountain field, or 3) a mountain field, neutral in regards to the enemy direction
	 */
	Medium(2),
	
	/**
	 * The node is 1)&2) a grass field, or 3) a walkable field (not Water) opposite to the enemy direction
	 */
	Low(1);
	

	private final int priorityValue;
		
	private NodePriority(int priorityValue) {
		this.priorityValue = priorityValue;
	}
	
	public int getPriorityValue() {
        return priorityValue;
    }
	
//	public static NodePriority findHighestPriority(Collection<NodePriority> priorities) {
//        return priorities.stream()
//                .min(Comparator.comparingInt(NodePriority::getPriorityValue))
//                .orElse(null);
//	}


}
