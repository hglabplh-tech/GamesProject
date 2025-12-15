package io.github.hglabplh_tech.mines.backend;

import java.util.Objects;

public record ButtonDescription(boolean isProcessed, SweepPointType pointType) {

    public boolean isMine() {
        return pointType().equals(SweepPointType.MINEPOINT);
    }

    public boolean equalsComplete(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ButtonDescription that = (ButtonDescription) o;
        return isProcessed() == that.isProcessed() && pointType() == that.pointType();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ButtonDescription that = (ButtonDescription) o;
        return  pointType() == that.pointType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(isProcessed(), pointType());
    }

    @Override
    public String toString() {
        return "ButtonDescription{" +
                "isProcessed=" + isProcessed +
                ", pointType=" + pointType +
                '}';
    }
}
