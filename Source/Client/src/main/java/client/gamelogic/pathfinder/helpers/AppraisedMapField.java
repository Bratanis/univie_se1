package client.gamelogic.pathfinder.helpers;

import client.gamelogic.pathfinder.helpers.appraiser.ENodePriority;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public class AppraisedMapField implements Comparable<AppraisedMapField>{

	private final Coordinates fieldCoordinates;
	private final MapNode fieldMapNode;
	private final ENodePriority fieldPriority;
	
	public AppraisedMapField(Coordinates fieldCoordinates, MapNode fieldMapNode, ENodePriority fieldPriority) {
		super();
		this.fieldCoordinates = fieldCoordinates;
		this.fieldMapNode = fieldMapNode;
		this.fieldPriority = fieldPriority;
	}
	
//	public ENodePriority getFieldPriority() {
//		return fieldPriority;
//	}
//	

	@Override
	public int compareTo(AppraisedMapField otherField) {
		return fieldPriority.compareTo(otherField.fieldPriority);
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
