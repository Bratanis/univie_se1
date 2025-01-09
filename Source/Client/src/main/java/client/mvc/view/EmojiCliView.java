package client.mvc.view;

import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public class EmojiCliView extends CLIView{

	@Override
	protected String mapNodeStringRender(MapNode mapNode, boolean treasureCollected) {
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
	        return switch (mapNode.getTerrain()) {
	            case ETerrain.Grass -> "\uD83D\uDFE9 "; // Green square 🟩
	            case ETerrain.Water -> "\uD83D\uDFE6 "; // Blue square 🟦
	            case ETerrain.Mountain -> "\uD83D\uDD32 ";    // Mountain ⛰️
	            default -> " !!!!!"; // Should never be reached!
	        };
	    }
	}

}
