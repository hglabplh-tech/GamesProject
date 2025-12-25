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
