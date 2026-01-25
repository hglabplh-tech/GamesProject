package io.github.hglabplh_tech.sinking.backend;

import java.util.*;

public class ShipDeclares {

    private static final Integer FIRST_PLAYER = 1;

    private static final Integer SECOND_PLAYER = 2;

    private static final List<ShipDefines> shipsList = new ArrayList<>();

    private static final Map<Integer, List<ShipDefines>> shipsActive = new HashMap<>();

    static {
        for (int index = 0; index < 5;index++) {
            shipsList.add(ShipDefines.TWO_POINT_SHIP);
        }

        for (int index = 0; index < 5;index++) {
            shipsList.add(ShipDefines.THREE_POINT_SHIP);
        }

        for (int index = 0; index < 3;index++) {
            shipsList.add(ShipDefines.FOUR_POINT_SHIP);
        }

        for (int index = 0; index < 3;index++) {
            shipsList.add(ShipDefines.FIVE_POINT_SHIP);
        }
        for (int index = 0; index < 2;index++) {
            shipsList.add(ShipDefines.SIX_POINT_SHIP);
        }

        for (int index = 0; index < 2;index++) {
            shipsList.add(ShipDefines.SEVEN_POINT_SHIP);
        }

    }




}
