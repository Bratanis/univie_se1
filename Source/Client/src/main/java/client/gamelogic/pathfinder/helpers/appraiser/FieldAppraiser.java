package client.gamelogic.pathfinder.helpers.appraiser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.gamelogic.pathfinder.helpers.AppraisedMapField;
import client.mvc.model.gamemap.mapelements.Coordinates;
import client.mvc.model.gamemap.mapelements.MapNode;

public abstract class FieldAppraiser {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Collection<Coordinates> visitedCoordinates;
	
	public FieldAppraiser (Collection<Coordinates> visitedCoordinates) {
		this.visitedCoordinates = visitedCoordinates;
	}

	public AppraisedMapField getBestMapField (Map<Coordinates, MapNode> unappraisedFields) {
		if (unappraisedFields.isEmpty())
			logger.warn("getBestMapField() method of FieldAppraiser got fed an empty map of fields!");
		List<AppraisedMapField> allAppraisedFields = new ArrayList<>();
		
		unappraisedFields.forEach((coordinates, mapNode) -> {
			AppraisedMapField appraisedField = assignFieldPriority(coordinates, mapNode);
			assert (appraisedField != null);
			allAppraisedFields.add(appraisedField);
		});
		
	    return getHighestPriorityField(allAppraisedFields);
	}
	
	private AppraisedMapField getHighestPriorityField(List<AppraisedMapField> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Cannot get highest priority field; fields = " + fields);
        }

        Collections.shuffle(fields); // Ensures equal priority fields are not always given in the same order (prevents infinite loops)
        
        // Use streams to find the field with the highest priority
        return fields.stream()
                     .max(Comparator.naturalOrder())
                     .orElseThrow(() -> new IllegalArgumentException("Unexpectedly, getHightesPriorityField() collection is empty!."));
    }
	
	protected abstract AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode);

}







