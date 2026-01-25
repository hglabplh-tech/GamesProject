package io.github.hglabplh_tech.sinking.backend;

public enum ShipPointType {
    FOREIGN("foreign"),
    HOME("home"),
    ;
    private final String typeName;

    ShipPointType(String typeName) {
        this.typeName = typeName;
    }
}
