package com.app.service;

import com.app.domain.vo.CardGroupInfoVo;
import com.common.core.utils.CardHandler;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameServiceTest {
    @Test
    void movingFromAreaToFrameUpdatesTheCorrectPlayerAndMirrorsOpponentPosition() {
        var self = new CardGroupInfoVo();
        var opponent = new CardGroupInfoVo();
        self.getFoldAreaList().add(11);
        opponent.getFoldAreaList().add(22);

        var cards = new CardHandler();
        cards.bind(self, opponent);
        var service = new GameService();
        service.init();

        service.invokeMethod("MoveFromAreaToFrame", Map.of("cardId", 11, "areaId", 1, "frameId", 5), cards);
        service.invokeMethod("MoveFromAreaToFrame", Map.of("cardId", 22, "areaId", 4, "frameId", 7), cards);

        assertFalse(self.getFoldAreaList().contains(11));
        assertFalse(opponent.getFoldAreaList().contains(22));
        assertEquals(5, self.frameMap.get(11).getPos());
        assertEquals(22, opponent.frameMap.get(22).getPos());
    }

    @Test
    void invalidFrameDoesNotRemoveCardFromArea() {
        var self = new CardGroupInfoVo();
        self.getCardAreaList().add(11);
        var cards = new CardHandler();
        cards.bind(self, new CardGroupInfoVo());

        assertThrows(IllegalArgumentException.class,
                () -> new GameService().MoveFromAreaToFrame(11, 0, 30, cards));
        assertEquals(11, self.getCardAreaList().get(0));
    }
}
