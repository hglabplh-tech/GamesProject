package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.JsonAdapter;
import io.github.hglabplh_tech.mines.backend.util.Point;

import java.lang.reflect.Type;

import static java.lang.String.valueOf;

public class TypeAdaptPoint implements JsonSerializer<Point> {

    public TypeAdaptPoint() {
        super();
    }
    @Override
    public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        jsonObj.add("x", context.serialize(src.x()));
        jsonObj.add("y", context.serialize(src.y()));

        return jsonObj;
    }
}
