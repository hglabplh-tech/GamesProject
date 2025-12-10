/*
 * Copyright (c)
 */

package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.config.PlayModes;

import io.github.hglabplh_tech.mines.backend.util.Point;

import java.util.*;
import java.util.List;

public class SweeperLogic {

    private final Integer numFields;
    private final Integer cx;
    private final Integer cy;
    private final Integer numMines;
    private final List<List<ButtDescr>> fieldsList = new ArrayList<>();
    private final Boolean[] shadowArray;
    private final Boolean[] labArray;

    private Integer negativeHits;
    private Optional<Labyrinth> labyrinth;
    private PlayModes playMode;

    public SweeperLogic(PlayModes playMode, Integer cx, Integer cy, Integer numMines) {
        this.numFields = cx * cy;
        this.numMines = numMines;
        this.cx = cx;
        this.cy = cy;

        this.playMode = playMode;
        this.negativeHits = 0;
        this.shadowArray = new Boolean[this.numFields];
        this.labArray = new Boolean[this.numFields];

    }

    public List<List<ButtDescr>> calculateMines() {
        Random rand = new Random();
        int arrIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            this.fieldsList.add(new ArrayList<>());
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                this.fieldsList.get(cyInd).add(cxInd, new ButtDescr(Boolean.FALSE,
                        SweepPointType.NORMALPOINT));
                this.shadowArray[arrIndex] = Boolean.FALSE;
                this.labArray[arrIndex] = Boolean.FALSE;
                arrIndex++;
            }
        }
        for (int index = 0; index < this.numMines; index++) {
            Integer mineIndex = rand.nextInt(this.numFields - 1);
            this.shadowArray[mineIndex] = Boolean.TRUE;
        }

        for (int index = 0; index < 4; index++) {
            Integer nextIndex = rand.nextInt(this.numFields - 1);
            while (shadowArray[nextIndex]) {
                nextIndex = rand.nextInt(this.numFields - 1);
            }
            this.labArray[nextIndex] = Boolean.TRUE;
        }

        this.labyrinth = Optional.empty();
        arrIndex = 0;
        Point[] labPoints = new Point[4];
        int labIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                SweepPointType pointType = SweepPointType.NORMALPOINT;
                Boolean temp = this.shadowArray[arrIndex];
                Boolean lab = this.labArray[arrIndex];
                if (lab && this.playMode.equals(PlayModes.LABYRINTH)) {
                    labPoints[labIndex] = new Point(cxInd, cyInd);
                    pointType = switch (labIndex) {
                        case 0 -> SweepPointType.STARTPOINT;
                        case 1 -> SweepPointType.FIRST_BASE;
                        case 2 -> SweepPointType.SECOND_BASE;
                        case 3 -> SweepPointType.ENDPOINT;
                        default -> SweepPointType.NORMALPOINT;
                    };
                    labIndex++;
                }
                pointType = temp ? SweepPointType.MINEPOINT : pointType;
                this.fieldsList.get(cyInd).add(cxInd, new ButtDescr(Boolean.FALSE,
                        pointType));
                arrIndex++;

            }
        }
        if (this.playMode.equals(PlayModes.LABYRINTH)) {
            this.labyrinth = Optional.of(new Labyrinth(labPoints[0], labPoints[1], labPoints[2], labPoints[3]));
        }
        return getFieldsList();
    }

    public String makeButtonName(Integer x, Integer y, Boolean isMine) {
        return new StringBuilder()
                .append(x)
                .append('#')
                .append(y)
                .append('#')
                .append(isMine).toString();
    }

    public Boolean isMineHit(String buttonName) {
        String[] buttonValues = buttonName.split("#");
        Integer x = Integer.valueOf(buttonValues[0]);
        Integer y = Integer.valueOf(buttonValues[1]);
        ButtDescr temp = this.fieldsList.get(y).get(x);
        Boolean mineHit = Boolean.valueOf(buttonValues[2]);
        if (!temp.isProcessed() && !mineHit) {
            this.negativeHits++;
        }
        this.fieldsList.get(y).add(x, new ButtDescr(Boolean.TRUE, temp.getPointType()));
        return Boolean.valueOf(mineHit);
    }

    public Boolean compNamesXY(String buttonName, String compName) {
        String[] buttonValues = buttonName.split("#");
        Integer x = Integer.valueOf(buttonValues[0]);
        Integer y = Integer.valueOf(buttonValues[1]);
        String[] compValues = compName.split("#");
        Integer cox = Integer.valueOf(compValues[0]);
        Integer coy = Integer.valueOf(compValues[1]);
        if (cox.equals(x) && coy.equals(y)) {
            return true;
        }

        return false;
    }

    public boolean isPositiveEnd() {
        return (boolean) (this.negativeHits >= (this.numFields - this.numMines));
    }

    public Integer getNumFields() {
        return this.numFields;
    }

    public Integer getNumMines() {
        return this.numMines;
    }

    public Integer getCx() {
        return cx;
    }

    public Integer getCy() {
        return cy;
    }

    public Boolean[] getShadowArray() {
        return shadowArray;
    }

    public List<List<ButtDescr>> getFieldsList() {
        return Collections.unmodifiableList(this.fieldsList);
    }

    public Boolean[] getLabArray() {
        return labArray;
    }

    public Integer getNegativeHits() {
        return negativeHits;
    }

    public Optional<Labyrinth> getLabyrinth() {
        return labyrinth;
    }

    public PlayModes getPlayMode() {
        return playMode;
    }

    public static class ButtDescr {

        private final boolean isProcessed;
        private final SweepPointType pointType;

        public ButtDescr(boolean isProcessed, SweepPointType type) {
            this.isProcessed = isProcessed;
            this.pointType = type;
        }

        public static class ButtonPoint {
            private final Point myPoint;

            private final ButtDescr buttonDescr;

            public ButtonPoint(Point myPoint, ButtDescr buttonDescr) {
                this.myPoint = myPoint;
                this.buttonDescr = buttonDescr;
            }

            public Point getMyPoint() {
                return myPoint;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                ButtonPoint that = (ButtonPoint) o;
                return Objects.equals(getMyPoint(), that.getMyPoint()) && Objects.equals(buttonDescr, that.buttonDescr);
            }

            @Override
            public int hashCode() {
                return Objects.hash(getMyPoint(), buttonDescr);
            }
        }

        public boolean isProcessed() {
            return isProcessed;
        }

        public SweepPointType getPointType() {
            return this.pointType;
        }

        public boolean isMine() {
            return getPointType().equals(SweepPointType.MINEPOINT);
        }

    }

}
