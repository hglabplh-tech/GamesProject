/*
 * Copyright (c) Harald Glab-Plhak 2025
 */

package io.github.hglabplh_tech.mines.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test the backend of the Minesweeper
 */
class SweeperUtilTest {

    private SweeperUtil util;

    /**
     * Before each tesst we createe a new instance of the util
     */
    @BeforeEach
    public void before() {
        util= new SweeperUtil(20,20,15);
    }

    /**
     * Here the random calculation of the mines placement is tested
     */
    @Test
    void calculateMines() {
        List<List<SweeperUtil.ButtDescr>> thisList = util.calculateMines();
        Boolean[] shadow = util.getShadowArray();
        SweeperUtil util2 = new SweeperUtil(20,20,15);
        List<List<SweeperUtil.ButtDescr>> thisList2 = util2.calculateMines();
        Boolean[] shadow2 = util2.getShadowArray();
        Boolean equal = (Arrays.compare(shadow2, shadow) == 0);
        assertThat("Arrays are equal allthough random filled", equal, is(false));
    }

    /**
     * Test the generation of the button name out of position and true / false is a mine
     */
    @Test
    void makeButtonName() {
        String name = this.util.makeButtonName(5,7, true);
        String[] values = name.split("#");
        Integer x = Integer.valueOf(values[0]);
        Integer y = Integer.valueOf(values[1]);
        Boolean isMine = Boolean.valueOf(values[2]);
        assertThat("value x not ok", x, is(5));
        assertThat("value y not ok", y, is(7));
        assertThat("value isMine not ok", isMine, is(true));
    }

    /**
     * test the hit function which is chwcking if a button is a mine by his name
     */
    @Test
    void isMineHit() {
        List<List<SweeperUtil.ButtDescr>> thisList = util.calculateMines();
        String name = this.util.makeButtonName(5,7, true);
        assertThat("there should be a hit", util.isMineHit(name), is(true));
        name = this.util.makeButtonName(5,7, false);
        assertThat("there should be NO hit", util.isMineHit(name), is(false));
    }

    /**
     * test if the check for a positive end works. This is done by simulating the right count of positive
     * hits without hitting a mine field
     */
    @Test
    void isPositiveEnd() {
        List<List<SweeperUtil.ButtDescr>> thisList = util.calculateMines();

        int x = 0;
        int y = 0;
        for (int index = 0; index < (util.getNumFields() - this.util.getNumMines() ); index++) {
            String name = this.util.makeButtonName(x,y , false);
            util.isMineHit(name);
            if (x >= (this.util.getCx() - 1)) {
                if (y < this.util.getCy() -1) {
                    y++;
                }
                x = 0;
            } else {
                x++;
            }
        }
        assertThat("waited for positive end", util.isPositiveEnd(), is(true));

    }
}