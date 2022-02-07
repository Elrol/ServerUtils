package com.github.elrol.elrolsutilities.libs.adapters;

import com.github.elrol.elrolsutilities.api.data.ITpRequest;
import com.github.elrol.elrolsutilities.data.TpRequest;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class ITpRequestAdapter implements JsonSerializer<ITpRequest>, JsonDeserializer<ITpRequest> {
    @Override
    public ITpRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        UUID requester = UUID.fromString(obj.get("requester").getAsString());
        UUID target = UUID.fromString(obj.get("target").getAsString());
        String isTpHere = obj.get("tphere").getAsString();
        return new TpRequest(requester, target, isTpHere.equals("true"));
    }

    @Override
    public JsonElement serialize(ITpRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("requester", src.getRequester().toString());
        obj.addProperty("target", src.getTarget().toString());
        obj.addProperty("tphere", (src.isTpHere() ? "true" : "false"));
        return obj;
    }
}
