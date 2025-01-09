package client.mvc.view;

import client.mvc.model.gamemap.mapelements.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public class AsciiCliView extends CLIView{

	@Override
	protected String mapNodeStringRender(MapNode mapNode, boolean treasureCollected) {
        if (mapNode.hasCastle()) {
            return " _IHI_";
        } else if (mapNode.hasTreasure()) {
            return " _[$]_";
        } else if (mapNode.hasEnemy() && mapNode.hasMe()) {
            return "!%#@✴&";
        } else if (mapNode.hasMe()) {
            return treasureCollected ? " ($‿$)" : " (°‿°)";
        } else if (mapNode.hasEnemy()) {
            return " (`ʖ̯´)";
        } else {
            return switch (mapNode.getTerrain()) {
                case ETerrain.Grass -> " _____";
                case ETerrain.Water -> " ~~~~~";
                case ETerrain.Mountain -> " A^A^A";
                default -> " !!!!!"; // Should never be reached!
            };
        }
	}

}
