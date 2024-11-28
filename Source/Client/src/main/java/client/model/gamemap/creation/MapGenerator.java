package client.model.gamemap.creation;

import client.model.gamemap.ClientHalfMap;
import client.model.gamemap.mapelements.Coordinates;
import client.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class MapGenerator {

	/**
	 * Attributes:
	 */
	
	// Minimum requirements for the map
	// private static final int MIN_GRASS_FIELDS = 24; // The generator will favor placing grass so this is unnecessary
	private final int MIN_MOUNTAIN_FIELDS = 5;
	private final int MIN_WATER_FIELDS = 7;

	private final int TOTAL_NUM_FIELDS = 50;

	private Logger logger = LoggerFactory.getLogger(MapGenerator.class);;
	/**
	 * Default constructor
	 */
	public MapGenerator() {
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * @return
	 */
	public ClientHalfMap offerHalfMap() {
		HashMap<Coordinates, MapNode> mapFields = initializeMapFields();

		// setting the treasure to one random position and the castle and Me to another
		// setTreasure(mapFields); // Treasure set by the server
		Coordinates startingCoordinates = setCastleAndMe(mapFields);

		return new ClientHalfMap(mapFields, startingCoordinates); // , myTreasureLocation
	}

	/**
	 * Helper function that will generate a random map that contains the minimum
	 * number of each field type
	 * 
	 * @return
	 */
	private HashMap<Coordinates, MapNode> initializeMapFields() {
		HashMap<Coordinates, MapNode> mapFields = new HashMap<>();
		
		// CREATE A TerrainCountTracker CLASS THAT WILL TAKE THE CONSTANTS AND GENERATE 3 PARAMETERS (ACCESSIBLE VIA GETTERS)

		// Number of fields of each type. Grass is going to be the default, once the
		// mountains and water has been set
		AtomicInteger mountainNum = new AtomicInteger(generateFieldNum(MIN_MOUNTAIN_FIELDS));
		logger.debug("MapGenerator will place " + mountainNum + " mountain tiles");
		AtomicInteger waterNum = new AtomicInteger(generateFieldNum(MIN_WATER_FIELDS));
		logger.debug("MapGenerator will place " + waterNum + " water tiles");
		AtomicInteger grassNum = new AtomicInteger(TOTAL_NUM_FIELDS - waterNum.get() - mountainNum.get());
		logger.debug("MapGenerator will place " + grassNum + " grass tiles");

		// Populate array, considering the minimum num of field for each field type
		Coordinates lastMapCoordinates = ClientHalfMap.LAST_COORDINATES;
		
		for (int X = 0; X <= lastMapCoordinates.getX(); ++X) {
			for (int Y = 0; Y <= lastMapCoordinates.getY(); ++Y) {

				ETerrain terrain = rollRandomType(mountainNum.get(), waterNum.get(), grassNum.get());
				adjustParameters(terrain, mountainNum, waterNum, grassNum);
				MapNode mapNode = new MapNode(terrain);
				Coordinates coordinates = new Coordinates(X, Y);

				mapFields.put(coordinates, mapNode);

			}
		}
		assert (mapFields.size() == 50);
		return mapFields;
	}
	

	
	/**
	 *  Used to change update the number of fields remaining to be placed for each type. The numbers aren't decremented in the rollRandomType() method
	 *  to avoid side effects.
	 * @param terrain
	 * @param mountainNum
	 * @param waterNum
	 * @param grassNum
	 */
	private  void adjustParameters(ETerrain terrain, AtomicInteger mountainNum, AtomicInteger waterNum, AtomicInteger grassNum) {
	    if (terrain == ETerrain.Mountain) {
	        mountainNum.decrementAndGet();
	        //logger.debug("Mountain placed. Mountain fields left:" + mountainNum.get());				// Logs removed because they spam too much
	    } else if (terrain ==ETerrain.Water) {
	        waterNum.decrementAndGet();
	        //logger.debug("Water placed. Water fields left:" + waterNum.get());
	    } else {
	        grassNum.decrementAndGet();
	    }
	}

	/**
	 * Used to randomly determine the number of fields of each type
	 * 
	 * @param minimum
	 * @return
	 */
	private int generateFieldNum(int minimum) {
		Random r = new Random();
		int extra = r.nextInt(5);
		return minimum + extra;
	}

	/**
	 * Will randomly generate one of the 3 types based on the input
	 * 
	 * @param mountainNum
	 * @param waterNum
	 * @param grassNum
	 * @return
	 */
	private ETerrain rollRandomType(int mountainNum, int waterNum, int grassNum) {
		// Will be used to determine the type of each generated field
		Random r = new Random();
		int roll = r.nextInt(mountainNum + waterNum + grassNum);

		if (roll < mountainNum) {
			return ETerrain.Mountain;
		} else if (roll < (mountainNum + waterNum)) {
			return ETerrain.Water;
		} else {
			return ETerrain.Grass;
		}
	}

	/**
	 * Sets the castle and Me on a random grass field
	 * 
	 * @param mapFields
	 * @return
	 */

	private Coordinates setCastleAndMe(HashMap<Coordinates, MapNode> mapFields) {
		if (mapFields.isEmpty()) {
			throw new IllegalArgumentException("gameMap has no mapFields!");
		}

		Coordinates selectedFieldLocation = new Coordinates(-1, -1);
		MapNode selectedField;
		boolean isValid = false;

		while (!isValid) {

			selectedFieldLocation = getRandomMapNodeLocation(mapFields);
			selectedField = mapFields.get(selectedFieldLocation);

			if (selectedField.getTerrain() == ETerrain.Grass) {
				selectedField.setHasCastle(true);
				selectedField.setHasMe(true);
				logger.info("Starting position:" + selectedFieldLocation);
				isValid = true;
			}
		}
		return selectedFieldLocation;
	}

	private Coordinates getRandomMapNodeLocation(HashMap<Coordinates, MapNode> mapFields) {
		Random r = new Random();
		// Loop every time treasure couldn't be set
		int position = r.nextInt(mapFields.size());

		Iterator<Map.Entry<Coordinates, MapNode>> iterator = mapFields.entrySet().iterator();
		for (int i = 0; i < position; i++) {
			iterator.next();
		}
		return (iterator.next()).getKey();
	}

	
}