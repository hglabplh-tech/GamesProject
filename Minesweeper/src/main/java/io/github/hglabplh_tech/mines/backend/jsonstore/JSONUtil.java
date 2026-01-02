package io.github.hglabplh_tech.mines.backend.jsonstore;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import io.github.hglabplh_tech.mines.backend.ButtonDescription;
import io.github.hglabplh_tech.mines.backend.ButtonPoint;
import io.github.hglabplh_tech.mines.backend.Labyrinth;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;
import io.github.hglabplh_tech.mines.backend.util.Point;

import java.io.Reader;
import java.io.Writer;

public class JSONUtil {

    // TODO: write special customised type adapters for marshall / unmarshall look how this has to be done !!!!!!!
    public static void storeSweepLogic(SweeperLogic logic, Writer writer) {
        JsonWriter jsonWriter = new JsonWriter(writer);
        storeObject(logic, SweeperLogic.class, jsonWriter);
    }

    public static void storeLabyrinth(Labyrinth labyrinth, Writer writer) {
        JsonWriter jsonWriter = new JsonWriter(writer);
        storeObject(labyrinth, Labyrinth.class, jsonWriter);
    }

    public static <T> void storeObject(T source, Class<?> srcType, JsonWriter jsonWriter) {
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
        gson.toJson(source, srcType, jsonWriter);
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
