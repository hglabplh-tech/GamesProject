package io.github.hglabplh_tech.sinking.backend;

public enum ShipPlayerMode {
    MACHINE_MACHINE("Machine against Machine"),
    HUMAN_HUMAN("Human against Human"),
    HUMAN_MACHINE("Human against Machine"),;

    private final String description;

    ShipPlayerMode(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }
}
