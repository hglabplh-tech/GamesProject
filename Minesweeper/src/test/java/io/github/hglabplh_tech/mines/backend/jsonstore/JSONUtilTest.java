package io.github.hglabplh_tech.mines.backend.jsonstore;

import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;

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

    @AfterEach
    void tearDown() {
    }

    @Test
    void storeSweepLogic() {
        StringWriter writer = new StringWriter();
        JSONUtil.storeSweepLogic(this.logic, writer);
        String jsonString = writer.getBuffer().toString();
        StringReader reader = new StringReader(jsonString);
        SweeperLogic loaded = JSONUtil.loadSweepLogic(reader);
        assertEquals(loaded.getCy(), this.logic.getCy());
        assertEquals(loaded.getLabyrinth().getStart(), this.logic.getLabyrinth().getStart());
    }

    @Test
    void testLoadSweepLogic() {

    }

    @Test
    void testLoadLabyrinth() {
    }



    @Test
    void testStoreLabyrinth() {
        StringWriter writer = new StringWriter();
        JSONUtil.storeLabyrinth(this.labyrinth, writer);
        String jsonString = writer.getBuffer().toString();
        StringReader reader = new StringReader(jsonString);
        Labyrinth loaded = JSONUtil.loadLabyrinth(reader);
        assertEquals(loaded.getStart(), this.labyrinth.getStart());
        assertEquals(loaded.getFirstBase(), this.labyrinth.getFirstBase());
        assertEquals(loaded.getSecondBase(), this.labyrinth.getSecondBase());
        assertEquals(loaded.getEnd(), this.labyrinth.getEnd());
    }
}