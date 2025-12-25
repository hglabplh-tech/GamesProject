package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.hglabplh_tech.mines.backend.Labyrinth;

import java.lang.reflect.Type;

public class TypeAdaptLab implements JsonSerializer<Labyrinth> {
    public TypeAdaptLab() {
        super();
    }
    @Override
    public JsonElement serialize(Labyrinth src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        jsonObj.add("nextIndex", context.serialize(src.getNextIndex()));
        jsonObj.add("nextPoint", context.serialize(src.getNextPoint()));

        jsonObj.add("startPoint", context.serialize(src.getStart()));
        jsonObj.add("firstBase", context.serialize(src.getFirstBase()));
        jsonObj.add("secondBase", context.serialize(src.getSecondBase()));
        jsonObj.add("endPoint", context.serialize(src.getEnd()));


        jsonObj.add("path", context.serialize(src.getPath()));
        jsonObj.add("pathToNext", context.serialize(src.getPathToNext()));

        jsonObj.add("pointsOrder", context.serialize(src.getPointsOrder()));
        jsonObj.add("actPoint", context.serialize(src.getActPoint()));
        return jsonObj;
    }
}
