package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.util.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LabyrinthTest {
    private Point startPoint;
    private Point firstBasePoint;
    private Point secondBasePoint;
    private Point endPoint;
    Labyrinth labyrinthObj ;

    @BeforeEach
    public void before() {
        this.startPoint = new Point(3,8);
        this.firstBasePoint = new Point(10,35);
        this.secondBasePoint = new Point(43, 27);
        this.endPoint = new Point(60,13);
        this.labyrinthObj = new Labyrinth(this.startPoint, this.firstBasePoint,
                this.secondBasePoint, this.endPoint);
    }

    @Test
    public void testAddXYToPath() {
        this.labyrinthObj.addXYToPath(this.startPoint.x(), this.startPoint.y());
        this.labyrinthObj.addXYToPath(4,9);
        boolean pathOk = this.labyrinthObj.checkCorrectPath();
        assertTrue(pathOk, "Path should be correct Ups");
    }

    @Test
    public void testCalculateNextPoint() {
        this.labyrinthObj.calculateNextPoint();
        Point p = labyrinthObj.getNextPoint();
        assertThat("next point not correct", p , is(this.secondBasePoint));

        this.labyrinthObj.calculateNextPoint();
        p = labyrinthObj.getNextPoint();
        assertThat("next point not correct", p , is(this.endPoint));
    }

    @Test
    public void testCheckCorrectPath() {
        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx <= 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8);
        }
        for (int indexy = 8; indexy <= 35; indexy++) {
            this.labyrinthObj.addXYToPath(10, indexy);
        }

        // to second base
        for (int indexx = 11; indexx <= 43; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 35);
        }
        for (int indexy = 36; indexy >= 27; indexy--) {
            this.labyrinthObj.addXYToPath(43, indexy);
        }
        assertTrue(this.labyrinthObj.checkCorrectPath(), "path not correct");

        this.labyrinthObj.getPathToNext().clear();

        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx <= 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8);
        }
        for (int indexy = 8; indexy <= 35; indexy++) {
            this.labyrinthObj.addXYToPath(10, indexy);
        }

        // to the end
        for (int indexx = 44; indexx <= 60; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 27);
        }
        for (int indexy = 27; indexy >= 13; indexy--) {
            this.labyrinthObj.addXYToPath(60, indexy);
        }

        assertFalse(this.labyrinthObj.checkCorrectPath(), "path seen as correct though it is incorrect");
    }

    @Test
    public void testIsPositiveEnd() {
        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx <= 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8);
        }
        for (int indexy = 8; indexy <= 35; indexy++) {
            this.labyrinthObj.addXYToPath(10, indexy);
        }

        // to second base
        for (int indexx = 11; indexx <= 43; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 35);
        }
        for (int indexy = 36; indexy >= 27; indexy--) {
            this.labyrinthObj.addXYToPath(43, indexy);
        }

        // to the end
        for (int indexx = 44; indexx <= 60; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 27);
        }
        for (int indexy = 27; indexy >= 13; indexy--) {
            this.labyrinthObj.addXYToPath(60, indexy);
        }
       // this.labyrinthObj.getPathToNext().forEach(e -> System.out.println(e.toString()));
        assertTrue(this.labyrinthObj.checkCorrectPath(), "path is not correct calculated");
        assertTrue(this.labyrinthObj.isPositiveEnd(), "positive end awaited ???");

    }

    @Test
    public void testCountBasesReached() {
        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx <= 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8);
        }
        for (int indexy = 8; indexy <= 35; indexy++) {
            this.labyrinthObj.addXYToPath(10, indexy);
        }

        // to second base
        for (int indexx = 11; indexx <= 43; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 35);
        }
        for (int indexy = 36; indexy >= 27; indexy--) {
            this.labyrinthObj.addXYToPath(43, indexy);
        }
        assertThat( "count bases not correct", this.labyrinthObj.countBasesReached(), is(2));
        this.labyrinthObj.getPathToNext().clear();

        this.labyrinthObj.addToPath(this.startPoint);

        // to first base
        for (int indexx = 4; indexx <= 10; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 8);
        }
        for (int indexy = 8; indexy <= 35; indexy++) {
            this.labyrinthObj.addXYToPath(10, indexy);
        }

        // to the end
        for (int indexx = 44; indexx <= 60; indexx++) {
            this.labyrinthObj.addXYToPath(indexx, 27);
        }
        for (int indexy = 27; indexy >= 13; indexy--) {
            this.labyrinthObj.addXYToPath(60, indexy);
        }
        assertThat( "count bases not correct", this.labyrinthObj.countBasesReached(), is(0));
    }

}