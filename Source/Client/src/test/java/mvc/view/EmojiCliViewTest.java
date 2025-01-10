package mvc.view;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import client.mvc.model.gamemap.mapelements.MapNode;
import client.mvc.view.EmojiCliView;
import messagesbase.messagesfromclient.ETerrain;

class EmojiCliViewTest {

    private final EmojiCliView emojiCliView = new EmojiCliView();

    @Test
    void testRenderCastleNode() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.hasCastle()).thenReturn(true);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83C\uDFF0 ", result); // 🏰
    }

    @Test
    void testRenderTreasureNode() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.hasTreasure()).thenReturn(true);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDCB0 ", result); // 💰
    }

    @Test
    void testRenderCollisionNode() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.hasEnemy()).thenReturn(true);
        when(mockNode.hasMe()).thenReturn(true);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDCA5 ", result); // 💥
    }

    @Test
    void testRenderPlayerNodeWithTreasureCollected() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.hasMe()).thenReturn(true);

        String result = emojiCliView.testEmojiFieldRender(mockNode, true);
        assertEquals("\uD83E\uDD11 ", result); // 🤑
    }

    @Test
    void testRenderPlayerNodeWithoutTreasureCollected() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.hasMe()).thenReturn(true);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDE03 ", result); // 😃
    }

    @Test
    void testRenderEnemyNode() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.hasEnemy()).thenReturn(true);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDC79 ", result); // 👹
    }

    @Test
    void testRenderGrassTerrain() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Grass);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDFE9 ", result); // 🟩
    }

    @Test
    void testRenderWaterTerrain() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Water);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDFE6 ", result); // 🟦
    }

    @Test
    void testRenderMountainTerrain() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.getTerrain()).thenReturn(ETerrain.Mountain);

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        assertEquals("\uD83D\uDD32 ", result); // ⛰️
    }

    @Test
    void testRenderInvalidTerrain() {
        MapNode mockNode = mock(MapNode.class);
        when(mockNode.getTerrain()).thenReturn(null); // Simulating unexpected terrain

        String result = emojiCliView.testEmojiFieldRender(mockNode, false);
        
        assertEquals("❌", result);
    }
    
    @Test
    void testRenderNullMapNode() {
        MapNode mockNode = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        	emojiCliView.testEmojiFieldRender(mockNode, false);
        });
        
        assertTrue("testRenderInvalidTerrain() should have thrown an exception!", exception.getMessage().contains("mapNode is null"));
    }
}
