package io.github.hglabplh_tech.mines.backend.jsonstore;

import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.config.PlayModes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class JSONUtilTest {

    private SweeperLogic logic = null;
    private Labyrinth labyrinth = null;

    @BeforeEach
    public void before() {
        this.logic = new SweeperLogic(PlayModes.LABYRINTH, 10, 10, 20);
        assertNotNull(this.logic);
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
    void testLoadSweepLogic() throws IOException {
        String outFilePath = System.getProperty("user.home") +
                "/gamesTest/";
        String completePath = outFilePath + "sweeplogic2.tmp";
        File temp = new File(outFilePath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        FileWriter writer = new FileWriter(completePath);
        JSONUtil.storeSweepLogic(this.logic, writer);
        writer.close();
        Reader reader = new FileReader(completePath);
        SweeperLogic loaded = JSONUtil.loadSweepLogic(reader);
        reader.close();
        assertEquals(loaded.getCy(), this.logic.getCy());
        assertEquals(loaded.getLabyrinth().getStart(), this.logic.getLabyrinth().getStart());
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