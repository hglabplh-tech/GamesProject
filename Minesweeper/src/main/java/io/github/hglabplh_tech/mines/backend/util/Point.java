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
package io.github.hglabplh_tech.mines.backend.util;

import java.util.Objects;

public record Point(Integer x, Integer y) implements PointCompareIfc {

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

    public boolean checkXCoord(Integer xOne, Integer xTwo) {
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

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public CompareResult comparePoint(Point other) {
        return new CompareResult(StrictMath.abs(this.x() - other.x()), StrictMath.abs(this.y() - other.y()),
                this.x().compareTo(other.x()), this.y().compareTo(other.y()), this.equals(other));
    }

    @Override
    public NearerResult compareNearerToEnd(Point other, Point end) {
        int xDiffThisToEnd = StrictMath.abs(this.x() - end.x());
        int yDiffThisToEnd = StrictMath.abs(this.y() - end.y());
        int xDiffOtherToEnd = StrictMath.abs(other.x() - end.x());
        int yDiffOtherToEnd = StrictMath.abs(other.y() - end.y());
        boolean xOtherNearer = (xDiffThisToEnd <= xDiffOtherToEnd);
        boolean yOtherNearer = (yDiffThisToEnd <= yDiffOtherToEnd);
        boolean bothNearer = (xOtherNearer && yOtherNearer);
        return new NearerResult(xDiffThisToEnd, yDiffThisToEnd,
                xDiffOtherToEnd, yDiffOtherToEnd,
                xOtherNearer, yOtherNearer, bothNearer);
    }
}
