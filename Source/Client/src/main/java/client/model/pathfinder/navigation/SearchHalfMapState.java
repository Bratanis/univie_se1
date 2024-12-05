package client.model.pathfinder.navigation;

import java.io.*;
import java.util.*;

import client.model.GameProgress;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.ETerritory;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

/**
 * 
 */
public abstract class SearchHalfMapState extends NavigationState {

	protected ETerritory targetTerritory = ETerritory.None;

	public SearchHalfMapState(GameProgress gameProgress, ETerritory targetTerritory) {
		super(gameProgress);
		this.targetTerritory = targetTerritory;
	}

	@Override
	protected void removeOutOfBoundsFields(Map<Coordinates, MapNode> fields) {
		Iterator<Map.Entry<Coordinates, MapNode>> iterator = fields.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Coordinates, MapNode> field = iterator.next();
			if (!targetTerritory.contains(field.getKey())) { // If target teritory doesnt contain the coordinates of the
																// field, remove it
				logger.debug("PathFinder remoing field " + field
						+ " from considered fields: out of bounds for territory: " + targetTerritory);
				iterator.remove();
			}
		}
	}

}