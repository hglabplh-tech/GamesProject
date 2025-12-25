package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.hglabplh_tech.mines.backend.ButtonPoint;

import java.lang.reflect.Type;

public class TypeAdaptButtPoint implements JsonSerializer<ButtonPoint> {

    public TypeAdaptButtPoint() {
        super();
    }
    @Override
    public JsonElement serialize(ButtonPoint src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();
        jsonObj.add("myPoint", context.serialize(src.myPoint()));
        jsonObj.add("buttonDescr", context.serialize(src.buttonDescr()));
        return jsonObj;
    }
}
