package io.github.hglabplh_tech.sinking.backend;
import io.github.hglabplh_tech.games.backend.logexcp.GameLogger;
import io.github.hglabplh_tech.games.backend.logexcp.LoggingID;

import java.util.*;
import java.util.List;

public class ShipSinkPlayground {

    private final GameLogger logger = GameLogger.logInstance();
    private final Integer numFields;
    private final Integer cx;
    private final Integer cy;
    private final Integer numMines;
    private final ShipPlayerMode shipPlayerMode;
    private final List<List<ButtonPoint>> fieldsList = new ArrayList<>();

    public ShipSinkPlayground(ShipPlayerMode shipPlayerMode, Integer cx, Integer cy, Integer numMines) {
        this.numFields = cx * cy;
        this.numMines = numMines;
        this.cx = cx;
        this.cy = cy;
        this.shipPlayerMode = shipPlayerMode;


        logger.logDebug(LoggingID.MINELOG_DEB_ID_00001, this.numMines);
        logger.logDebug(LoggingID.MINELOG_DEB_ID_00002, this.cx, this.cy);
    }

    public List<List<ButtonPoint>> calculateMines() {
        Random rand = new Random();
        int arrIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            this.fieldsList.add(new ArrayList<>());
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                arrIndex++;
            }
        }
        for (int index = 0; index < this.numMines; index++) {
            Integer mineIndex = rand.nextInt(this.numFields - 1);

        }
        return this.fieldsList;
    }

}
