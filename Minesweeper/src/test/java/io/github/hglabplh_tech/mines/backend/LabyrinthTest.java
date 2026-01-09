package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.hglabplh_tech.mines.backend.SweepPointType.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LabyrinthTest {
    private ButtonPoint startPoint;
    private ButtonPoint firstBasePoint;
    private ButtonPoint secondBasePoint;
    private ButtonPoint endPoint;
    Labyrinth labyrinthObj ;

    @BeforeEach
    public void before() {
        this.startPoint = new ButtonPoint(new Point(3,8),
                new ButtonStatus(Boolean.FALSE, STARTPOINT));
        this.firstBasePoint = new ButtonPoint(new Point(10,35),
                new ButtonStatus(Boolean.FALSE, FIRST_BASE) );
        this.secondBasePoint = new ButtonPoint(new Point(43, 27),
                new ButtonStatus(Boolean.FALSE, SECOND_BASE));
        this.endPoint = new ButtonPoint(new Point(60,13),
                new ButtonStatus(Boolean.FALSE, ENDPOINT));
        this.labyrinthObj = new Labyrinth(this.startPoint, this.firstBasePoint,
                this.secondBasePoint, this.endPoint);
    }

    @Test
    public void testAddXYToPath() {
        this.labyrinthObj.addXYToPath(this.startPoint.myPoint().x(),
                this.startPoint.myPoint().y(), startPoint.buttonDescr());
        this.labyrinthObj.addXYToPath(4,9, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        boolean pathOk = this.labyrinthObj.checkCorrectPath();
        assertTrue(pathOk, "Path should be correct Ups");
    }

    @Test
    public void testCalculateNextPoint() {
        this.labyrinthObj.calculateNextPoint();
        ButtonPoint p = labyrinthObj.getNextPoint();
        assertThat("next point not correct", p , is(this.secondBasePoint));

        this.labyrinthObj.calculateNextPoint();
        p = labyrinthObj.getNextPoint();
        assertThat("next point not correct", p , is(this.endPoint));
    }

    @Test
    public void testCheckCorrectPath() {
        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx < 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 8; indexy < 35; indexy++) {
            this.labyrinthObj.addXYToPath(9, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.firstBasePoint);
        // to second base
        for (int indexx = 11; indexx < 43; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 35, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 36; indexy > 27; indexy--) {
            this.labyrinthObj.addXYToPath(42, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.secondBasePoint);
        assertTrue(this.labyrinthObj.checkCorrectPath(), "path not correct");

        this.labyrinthObj.getPathToNext().clear();

        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx < 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 8; indexy < 35; indexy++) {
            this.labyrinthObj.addXYToPath(9, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.firstBasePoint);
        // to the end
        for (int indexx = 44; indexx < 60; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 27, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 27; indexy > 13; indexy--) {
            this.labyrinthObj.addXYToPath(59, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.endPoint);
        assertFalse(this.labyrinthObj.checkCorrectPath(), "path seen as correct though it is incorrect");
    }

    @Test
    public void testIsPositiveEnd() {
        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx < 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 9; indexy < 35; indexy++) {
            this.labyrinthObj.addXYToPath(9, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.firstBasePoint);
        // to second base
        for (int indexx = 11; indexx < 43; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 35, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 36; indexy > 27; indexy--) {
            this.labyrinthObj.addXYToPath(42, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.secondBasePoint);

        // to the end
        for (int indexx = 44; indexx < 60; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 27, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 27; indexy > 13; indexy--) {
            this.labyrinthObj.addXYToPath(59, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.endPoint);

       // this.labyrinthObj.getPathToNext().forEach(e -> System.out.println(e.toString()));
        assertTrue(this.labyrinthObj.checkCorrectPath(), "path is not correct calculated");
        assertTrue(this.labyrinthObj.isPositiveEnd(), "positive end awaited ???");

    }

    @Test
    public void testCountBasesReached() {
        System.out.println(this.firstBasePoint);
        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx < 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 9; indexy < 35; indexy++) {
            this.labyrinthObj.addXYToPath(9, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.firstBasePoint);
        // to second base
        for (int indexx = 11; indexx < 43; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 35, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 36; indexy > 27; indexy--) {
            this.labyrinthObj.addXYToPath(43, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.secondBasePoint);
        assertThat( "count bases not correct", this.labyrinthObj.countBasesReached(), is(2));
        this.labyrinthObj.getPathToNext().clear();

        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx <= 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 9; indexy < 35; indexy++) {
            this.labyrinthObj.addXYToPath(9, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.firstBasePoint);
        // to the end
        for (int indexx = 44; indexx < 60; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 27, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        for (int indexy = 26; indexy > 13; indexy--) {
            this.labyrinthObj.addXYToPath(59, indexy, new ButtonStatus(Boolean.TRUE, NORMALPOINT));
        }
        this.labyrinthObj.addToPath(this.endPoint);
        assertThat( "count bases not correct", this.labyrinthObj.countBasesReached(), is(0));
    }

}