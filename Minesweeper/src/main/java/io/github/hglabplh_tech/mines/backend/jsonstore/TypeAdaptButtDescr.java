package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.hglabplh_tech.mines.backend.ButtonDescription;

import java.lang.reflect.Type;

public class TypeAdaptButtDescr implements JsonSerializer<ButtonDescription> {

    public TypeAdaptButtDescr() {
        super();
    }
    @Override
    public JsonElement serialize(ButtonDescription src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();

        jsonObj.add("isProcessed", context.serialize(src.isProcessed()));
        jsonObj.add("pointType", context.serialize(src.pointType()));

        return jsonObj;
    }
}
