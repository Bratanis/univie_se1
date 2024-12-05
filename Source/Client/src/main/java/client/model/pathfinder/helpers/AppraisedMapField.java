package client.model.pathfinder.helpers;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public class AppraisedMapField implements Comparable<AppraisedMapField> {

	private final Coordinates fieldCoordinates;
	private final MapNode fieldMapNode;
//	private final ENodePriority fieldPriority;
	private final Integer desirablilityIndex;

	public AppraisedMapField(Coordinates fieldCoordinates, MapNode fieldMapNode, int desirabilityIndex) {
		super();
		this.fieldCoordinates = fieldCoordinates;
		this.fieldMapNode = fieldMapNode;
		this.desirablilityIndex = desirabilityIndex;
	}

//	public ENodePriority getFieldPriority() {
//		return fieldPriority;
//	}
//	

	@Override
	public int compareTo(AppraisedMapField otherField) {
		return desirablilityIndex.compareTo(otherField.desirablilityIndex);
	}
//	 public static WeightedMapField findHighestPriority(Collection<WeightedMapField> fields) {
//	     return fields.stream()
//	    		 
//	             .min(Enum::compareTo) // Use declaration order of the enum
//	             .orElse(null);
//	 }

	public ETerrain getTerrain() {
		return fieldMapNode.getTerrain();
	}

	public Coordinates getCoordinates() {
		return fieldCoordinates;
	}

}
