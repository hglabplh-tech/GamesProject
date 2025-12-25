package io.github.hglabplh_tech.mines.backend.jsonstore;

import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class JSONUtilTest {

    private SweeperLogic logic = null;
    private Labyrinth labyrinth = null;

    @BeforeEach
    public void before() {
            this.logic = new SweeperLogic(PlayModes.LABYRINTH, 30,30, 25);
            this.logic.calculateMines();
            this.labyrinth = this.logic.getLabyrinth();
            assertNotNull(this.labyrinth);
    }

    @Test
    void storeSweepLogic() {
        String jsonString = JSONUtil.storeSweepLogic(this.logic);
        StringReader reader = new StringReader(jsonString);
        SweeperLogic loaded = JSONUtil.loadSweepLogic(reader);
        assertEquals(loaded.getCy(), this.logic.getCy());
        assertEquals(loaded.getLabyrinth().getStart(), this.logic.getLabyrinth().getStart());
    }

    @Test
    void storeLabyrinth() {
    }

    @Test
    void loadSweepLogic() {
    }

    @Test
    void loadLabyrinth() {
    }
}