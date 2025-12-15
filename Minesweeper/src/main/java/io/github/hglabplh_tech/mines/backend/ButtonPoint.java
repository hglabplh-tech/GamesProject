package io.github.hglabplh_tech.mines.backend;

import io.github.hglabplh_tech.mines.backend.util.Point;

import java.util.Objects;

public record ButtonPoint(Point myPoint, ButtonDescription buttonDescr) {

    @Override
    public ButtonDescription buttonDescr() {
        return buttonDescr;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ButtonPoint that = (ButtonPoint) o;
        return Objects.equals(myPoint(), that.myPoint()) && Objects.equals(buttonDescr(), that.buttonDescr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(myPoint(), buttonDescr());
    }

    @Override
    public String toString() {
        return "ButtonPoint{" +
                "myPoint=" + myPoint +
                ", buttonDescr=" + buttonDescr +
                '}';
    }
}
