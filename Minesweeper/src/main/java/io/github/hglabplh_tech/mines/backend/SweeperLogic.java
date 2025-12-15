/*
Copyright (c) 2025 Harald Glab-Plhak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
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
    private final List<List<ButtonDescription>> fieldsList = new ArrayList<>();
    private final Boolean[] shadowArray;
    private final Boolean[] labArray;

    private Integer successHits;
    private Optional<Labyrinth> labyrinthOpt;
    private PlayModes playMode;

    public SweeperLogic(PlayModes playMode, Integer cx, Integer cy, Integer numMines) {
        this.numFields = cx * cy;
        this.numMines = numMines;
        this.cx = cx;
        this.cy = cy;

        this.playMode = playMode;
        this.successHits = 0;
        this.shadowArray = new Boolean[this.numFields];
        this.labArray = new Boolean[this.numFields];

    }

    public List<List<ButtonDescription>> calculateMines() {
        Random rand = new Random();
        int arrIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            this.fieldsList.add(new ArrayList<>());
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                this.fieldsList.get(cyInd).add(cxInd, new ButtonDescription(Boolean.FALSE,
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

        this.labyrinthOpt = Optional.empty();
        arrIndex = 0;
        ButtonPoint[] labPoints = new ButtonPoint[4];
        int labIndex = 0;
        for (int cyInd = 0; cyInd < this.cy; cyInd++) {
            for (int cxInd = 0; cxInd < this.cx; cxInd++) {
                SweepPointType pointType = SweepPointType.NORMALPOINT;
                Boolean temp = this.shadowArray[arrIndex];
                Boolean lab = this.labArray[arrIndex];
                if (lab && this.playMode.equals(PlayModes.LABYRINTH)) {

                    pointType = switch (labIndex) {
                        case 0 -> SweepPointType.STARTPOINT;
                        case 1 -> SweepPointType.FIRST_BASE;
                        case 2 -> SweepPointType.SECOND_BASE;
                        case 3 -> SweepPointType.ENDPOINT;
                        default -> SweepPointType.NORMALPOINT;
                    };
                    pointType = temp ? SweepPointType.MINEPOINT : pointType;
                    labPoints[labIndex] = new ButtonPoint(new Point(cxInd, cyInd),
                            new ButtonDescription(Boolean.FALSE, pointType));
                    labIndex++;
                }
                pointType = temp ? SweepPointType.MINEPOINT : pointType;
                this.fieldsList.get(cyInd).add(cxInd, new ButtonDescription(Boolean.FALSE,
                        pointType));
                arrIndex++;

            }
        }
        if (this.playMode.equals(PlayModes.LABYRINTH)) {
            this.labyrinthOpt = Optional.of(new Labyrinth(labPoints[0], labPoints[1], labPoints[2], labPoints[3]));
        }
        return getFieldsList();
    }

    public void addToXYLabPath(Integer x, Integer y, ButtonDescription buttDescr) {
        labyrinthOpt.ifPresent(labyrinth -> labyrinth.addXYToPath(x, y, buttDescr));
    }

    public void addToLabPath(ButtonPoint thePoint) {
        labyrinthOpt.ifPresent(labyrinth -> labyrinth.addToPath(thePoint));
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
        ButtonDescription temp = this.fieldsList.get(y).get(x);
        Boolean mineHit = Boolean.valueOf(buttonValues[2]);
        if (!temp.isProcessed() && !mineHit) {
            this.successHits++;
        }
        this.fieldsList.get(y).add(x, new ButtonDescription(Boolean.TRUE, temp.pointType()));
        return mineHit;
    }

    public ButtonPoint extractPointFromName(String buttonName) {
        String[] buttonValues = buttonName.split("#");
        Integer x = Integer.valueOf(buttonValues[0]);
        Integer y = Integer.valueOf(buttonValues[1]);
        ButtonDescription descr = this.fieldsList.get(y).get(x);
        return new ButtonPoint(new Point(x, y), descr);
    }

    public SweepPointType extractPointType(String buttonName) {
        String[] buttonValues = buttonName.split("#");
        Integer x = Integer.valueOf(buttonValues[0]);
        Integer y = Integer.valueOf(buttonValues[1]);
        return this.fieldsList.get(y).get(x).pointType();
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
        return (this.successHits >= (this.numFields - this.numMines));
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

    public List<List<ButtonDescription>> getFieldsList() {
        return Collections.unmodifiableList(this.fieldsList);
    }

    public Boolean[] getLabArray() {
        return labArray;
    }

    public Integer getSuccessHits() {
        return successHits;
    }

    public Optional<Labyrinth> getLabyrinthOpt() {
        return labyrinthOpt;
    }

    public PlayModes getPlayMode() {
        return playMode;
    }

}
