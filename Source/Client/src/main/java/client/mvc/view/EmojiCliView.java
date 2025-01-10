package client.mvc.view;

import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public class EmojiCliView extends CLIView{

	@Override
	protected String getFieldAsCliRender(MapNode mapNode, boolean treasureCollected) {
		if (mapNode == null)
			throw new IllegalArgumentException("getFieldAsCliRender of EmojiCliView Cannot render field because given mapNode is null!!!");
		
	    if (mapNode.hasCastle()) {
	        return "\uD83C\uDFF0 "; // Castle 🏰
	    } else if (mapNode.hasTreasure()) {
	        return "\uD83D\uDCB0 "; // Money bag 💰
	    } else if (mapNode.hasEnemy() && mapNode.hasMe()) {
	        return "\uD83D\uDCA5 "; // Collision 💥
	    } else if (mapNode.hasMe()) {
	        return treasureCollected ? "\uD83E\uDD11 " : "\uD83D\uDE03 "; // 😃 (smiley) or 🤑 (money face)
	    } else if (mapNode.hasEnemy()) {
	        return "\uD83D\uDC79 "; // Ogre 👹
	    } else {
    		String errorNode = "❌"; // Cross mark ❌
    		ETerrain nodeTerrain = mapNode.getTerrain();
    		if (nodeTerrain == null)
    			return errorNode;
	        return switch (nodeTerrain) {
	            case ETerrain.Grass -> "\uD83D\uDFE9 "; // Green square 🟩
	            case ETerrain.Water -> "\uD83D\uDFE6 "; // Blue square 🟦
	            case ETerrain.Mountain -> "\uD83D\uDD32 ";    // Shold be Mountain ⛰️ but is a button, since the different sizes mess up the map
	            default -> errorNode; // Should never be reached!
	        };
	    }
	} 
	
	/*
	 * For testing only
	 */
	public String testEmojiFieldRender (MapNode mapNode, boolean treasureCollected) {
		return getFieldAsCliRender(mapNode, treasureCollected);
	}

}
