package client.model.pathfinder.helpers.appraiser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import client.model.pathfinder.helpers.AppraisedMapField;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;

public class EnemyDirFieldAppraiser extends FieldAppraiser {

	EMove enemyDirection;

	private static final Map<EMove, EMove> oppositeDirections = new HashMap<>();

	static {
		oppositeDirections.put(EMove.Left, EMove.Right);
		oppositeDirections.put(EMove.Right, EMove.Left);
		oppositeDirections.put(EMove.Down, EMove.Up);
		oppositeDirections.put(EMove.Up, EMove.Down);
	}

	public EnemyDirFieldAppraiser(List<Coordinates> visitedCoordinates, EMove enemyDirection) {
		super(visitedCoordinates);
		this.enemyDirection = enemyDirection;
	}

//	@Override
//	protected AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode) {
//
//		ENodePriority priority = ENodePriority.Forbidden;
//
//		EMove moveToReachNode = determineDirFromMeToField(coordinates);
//
//		// Water is forbidden
//		if (mapNode.getTerrain() != ETerrain.Water) {
//
//			// Always prefer unvisited Coordinates to avoid looping around a dead end
//			if (!visitedCoordinates.contains(coordinates)) {
//
//				// We want to generally go in the dir of the enemy territory
//				if (moveToReachNode.equals(enemyDirection))
//					priority = ENodePriority.High;
//
//				// If the dir is neutral in regards to the enemy dir, its still ok
//				else if (!isOppositeToEnemyDir(moveToReachNode)) {
//					priority = ENodePriority.Medium;
//
//					// We dislike going away from the enemy dir, but it is still better than
//					// backtracking
//				} else {
//					priority = ENodePriority.Low;
//				}
//
//				// Visited nodes are not nice but better than water
//			} else {
//				priority = ENodePriority.VeryLow;
//			}
//		}
//
//		return new AppraisedMapField(coordinates, mapNode, priority);
//	}

	@Override
	protected AppraisedMapField assignFieldPriority(Coordinates coordinates, MapNode mapNode) {

		Integer desirabilityIndex = Integer.MIN_VALUE;

		EMove moveToReachNode = determineDirFromMeToField(coordinates);

		// Water is forbidden
		if (mapNode.getTerrain() != ETerrain.Water) {

			// Always prefer unvisited Coordinates to avoid looping around a dead end
			if (!visitedCoordinates.contains(coordinates)) {

				// We want to generally go in the dir of the enemy territory
				if (moveToReachNode.equals(enemyDirection))
					desirabilityIndex = Integer.MAX_VALUE;

				// If the dir is neutral in regards to the enemy dir, its still ok
				else if (!isOppositeToEnemyDir(moveToReachNode)) {
					desirabilityIndex = Integer.MAX_VALUE - 1;

					// We dislike going away from the enemy dir, but it is still better than
					// backtracking
				} else {
					desirabilityIndex = Integer.MAX_VALUE - 2;
				}

				// Visited nodes are not nice but better than water
			} else {
				int recencyIndex = visitedCoordinates.indexOf(coordinates);
				// More recently visited fields get lower priority
				desirabilityIndex = -recencyIndex;
			}
		}

		return new AppraisedMapField(coordinates, mapNode, desirabilityIndex);
	}

	private EMove determineDirFromMeToField(Coordinates otherCoordinates) {
		Coordinates myCurrentCoordinates = ((List<Coordinates>) visitedCoordinates).getLast();
		return myCurrentCoordinates.directionTo(otherCoordinates);
	}

	private boolean isOppositeToEnemyDir(EMove direction) {
		EMove oppositeToEnemyDir = oppositeDirections.get(enemyDirection);
		return direction.equals(oppositeToEnemyDir);
	}
}
