package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class TypeAdaptOptional implements JsonSerializer<java.util.Optional> {

    public TypeAdaptOptional() {
        super();
    }
    @Override
    public JsonElement serialize(java.util.Optional src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObj = new JsonObject();
        if (src.isPresent()) {
            jsonObj.add("value", context.serialize(src.get()));
        }

        return jsonObj;
    }
}
