package io.github.hglabplh_tech.sinking.backend;

public enum ShipDefines {
    TWO_POINT_SHIP(2, false),
    THREE_POINT_SHIP(3, true),
    FOUR_POINT_SHIP(4, false),
    FIVE_POINT_SHIP(5, true),
    SIX_POINT_SHIP(6, false),
    SEVEN_POINT_SHIP(7, true),
    ;
    private final Integer length;
    private final Boolean shootBack;

    ShipDefines(Integer length, Boolean shootBack) {
        this.length = length;
        this.shootBack = shootBack;
    }

    public Integer length() {
        return length;
    }

    public Boolean shootBack() {
        return shootBack;
    }
}
