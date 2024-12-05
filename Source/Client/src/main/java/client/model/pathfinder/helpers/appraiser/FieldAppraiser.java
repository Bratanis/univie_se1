package client.model.pathfinder.helpers.appraiser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.AppraisedMapField;

public abstract class FieldAppraiser {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected List<Coordinates> visitedCoordinates;

	public FieldAppraiser(List<Coordinates> visitedCoordinates) {
		this.visitedCoordinates = visitedCoordinates;
	}

	public AppraisedMapField getBestMapField(Map<Coordinates, MapNode> unappraisedFields) {
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

	private AppraisedMapField getHighestPriorityField(List<AppraisedMapField> reachableFields) {
		if (reachableFields == null || reachableFields.isEmpty()) {
			throw new IllegalArgumentException("Cannot get highest priority field; fields = " + reachableFields);
		}

		Collections.shuffle(reachableFields); // Ensures equal priority fields are not always given in the same order
		// (prevents infinite loops)

		// Use streams to find the field with the highest priority
		return reachableFields.stream().max(Comparator.naturalOrder()).orElseThrow(
				() -> new IllegalArgumentException("Unexpectedly, getHightesPriorityField() collection is empty!."));
	}

//	private void visitedFieldsPriorityTieBreaker(List<AppraisedMapField> reachableFields) {
//
////		List<AppraisedMapField> visitedReachableMapFields = new ArrayList<AppraisedMapField>();
//
//		for (AppraisedMapField reachableField : reachableFields) {
//			Coordinates reachableFieldCoordinates = reachableField.getCoordinates();
//
//		}
//	}

	protected abstract AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode);

}
