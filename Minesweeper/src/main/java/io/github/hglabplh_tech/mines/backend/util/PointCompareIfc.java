package io.github.hglabplh_tech.mines.backend.util;

public interface PointCompareIfc {

    CompareResult comparePoint(Point other);

    NearerResult compareNearerToEnd(Point other, Point end);

    record CompareResult(int xDiffABS, int yDiffABS, int xComp, int yComp, boolean equal) {
    }

    record NearerResult(int xDiffThisToEnd, int yDiffThisToEnd,
                        int xDiffOtherToEnd, int yDiffOtherToEnd ,
                        boolean xOtherNearer, boolean yOtherNearer, boolean bothNearer) {
    }
}
