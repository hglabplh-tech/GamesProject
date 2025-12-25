package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hglabplh_tech.mines.backend.ButtonDescription;
import io.github.hglabplh_tech.mines.backend.ButtonPoint;
import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.util.Point;

import java.io.Reader;

public class JSONUtil {

    // TODO: write special customised type adapters for marshall / unmarshall look how this has to be done !!!!!!!
    public static String storeSweepLogic(SweeperLogic logic) {
         return storeObject(logic);
    }

    public static String storeLabyrinth(Labyrinth labyrinth) {
        return storeObject(labyrinth);
    }

    public static String storeObject(Object source) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.util.Optional.class,
                        new TypeAdaptOptional())
                .registerTypeAdapter(Point.class,
                        new TypeAdaptPoint())
                .registerTypeAdapter(ButtonPoint.class,
                        new TypeAdaptButtPoint())
                .registerTypeAdapter(ButtonDescription.class,
                        new TypeAdaptButtDescr())
                .registerTypeAdapter(SweeperLogic.class,
                        new TypeAdaptSweepLogic())
                .registerTypeAdapter(Labyrinth.class,
                        new TypeAdaptLab()).create();
        return gson.toJson(source);
    }

    public static SweeperLogic loadSweepLogic(Reader source) {
        return loadObject(source, SweeperLogic.class);
    }

    public static Labyrinth loadLabyrinth(Reader source) {
        return loadObject(source, Labyrinth.class);
    }


    public static <T> T loadObject(Reader source, Class<?> theClass) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.util.Optional.class,
                        new TypeAdaptOptional())
                .registerTypeAdapter(Point.class,
                        new TypeAdaptPoint())
                .registerTypeAdapter(ButtonPoint.class,
                        new TypeAdaptButtPoint())
                .registerTypeAdapter(ButtonDescription.class,
                        new TypeAdaptButtDescr())
                .registerTypeAdapter(SweeperLogic.class,
                        new TypeAdaptSweepLogic())
                .registerTypeAdapter(Labyrinth.class,
                        new TypeAdaptLab()).create();
        //noinspection unchecked
        return (T) gson.fromJson(source, theClass);
    }
}
