/*
 * Copyright (c)
 */

package io.github.hglabplh_tech.mines.backend;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SweeperUtil {

    private final Integer numFields;
    private final Integer cx;
    private final Integer cy;
    private final Integer numMines;
    private final List<List<ButtDescr>> fieldsList = new ArrayList<>();
    private final Boolean[] shadowArray;

    private Integer negativeHits;
    private Labyrinth labyrinth;

    public SweeperUtil(Integer cx, Integer cy, Integer numMines) {
        this.numFields = cx * cy;
        this.numMines = numMines;
        this.cx = cx;
        this.cy = cy;

        this.negativeHits = 0;
        this.shadowArray = new Boolean[this.numFields];

    }

    public List<List<ButtDescr>> calculateMines() {
        Random rand = new Random();
        int arrIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            this.fieldsList.add(new ArrayList<>());
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                this.fieldsList.get(cyInd).add(cxInd, new ButtDescr(Boolean.FALSE, Boolean.FALSE,
                        SweepPointType.NORMALPOINT));
                this.shadowArray[arrIndex] = Boolean.FALSE;
                arrIndex++;
            }
        }
        for (int index = 0; index < this.numMines; index++) {
            Integer mineIndex = rand.nextInt(this.numFields - 1);
            this.shadowArray[mineIndex] = Boolean.TRUE;
        }

        arrIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                Boolean temp = this.shadowArray[arrIndex];
                this.fieldsList.get(cyInd).add(cxInd, new ButtDescr(Boolean.FALSE, temp,
                        SweepPointType.NORMALPOINT));
                arrIndex++;
            }
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
        this.fieldsList.get(y).add(x, new ButtDescr(Boolean.TRUE, temp.isMine, SweepPointType.NORMALPOINT));
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

    public static class ButtDescr  {

        private final boolean isProcessed;
        private final boolean isMine;
        private final SweepPointType pointType;
        public ButtDescr(boolean isProcessed, boolean isMine, SweepPointType type) {
            this.isProcessed = isProcessed;
            this.isMine = isMine;
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

        public boolean isMine() {
            return isMine;
        }

    }

}
