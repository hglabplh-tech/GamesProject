package io.github.hglabplh_tech.mines.backend.jsonstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.hglabplh_tech.mines.backend.SweeperLogic;

import java.lang.reflect.Type;

public class TypeAdaptSweepLogic implements JsonSerializer<SweeperLogic> {

    public TypeAdaptSweepLogic() {
        super();
    }

    @Override
    public JsonElement serialize(SweeperLogic src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObj = new JsonObject();

        jsonObj.add("cx", context.serialize(src.getCx()));
        jsonObj.add("cy", context.serialize(src.getCy()));
        jsonObj.add("fieldsList", context.serialize(src.getFieldsList()));
        jsonObj.add("numFields", context.serialize(src.getNumFields()));
        jsonObj.add("numMines", context.serialize(src.getNumMines()));
        jsonObj.add("labyrinth", context.serialize(src.getLabyrinth()));
        jsonObj.add("shadowArray", context.serialize(src.getShadowArray()));
        jsonObj.add("labArray", context.serialize(src.getLabArray()));

        jsonObj.add("successHits", context.serialize(src.getSuccessHits()));
        jsonObj.add("labyrinthOpt", context.serialize("#lab-opt-placeholder#"));
        jsonObj.add("playMode", context.serialize(src.getPlayMode()));

        return jsonObj;
    }
}
