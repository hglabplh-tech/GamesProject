package io.github.hglabplh_tech.mines.backend.util;

import java.util.Objects;

public record Point(Integer x, Integer y) {

    public boolean checkPointIsNeighbor(Point other) {
        boolean success = false;
        if ((this.y() + 1) == other.y()) {
            success = checkXCoord(this.x(), other.x());
        } else if ((this.y() - 1) == other.y()) {
            success = checkXCoord(this.x(), other.x());
        } else if (this.y().equals(other.y())) {
            success = checkXCoord(this.x(), other.x());
        }
        return success;
    }

    public boolean checkXCoord(double xOne, double xTwo) {
        boolean success = false;
        if ((xOne + 1) == xTwo) {
            success = true;
        } else if ((xOne - 1) == xTwo) {
            success = true;
        } else if (xOne == xTwo) {
            success = true;
        }
        return success;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(x(), point.x()) && Objects.equals(y(), point.y());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x(), y());
    }


}
