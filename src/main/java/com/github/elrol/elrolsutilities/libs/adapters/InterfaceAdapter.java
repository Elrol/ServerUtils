package com.github.elrol.elrolsutilities.libs.adapters;

import com.google.gson.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.JsonUtils;

import java.lang.reflect.Type;

public final class InterfaceAdapter implements JsonSerializer<CompoundNBT>, JsonDeserializer<CompoundNBT> {
    public JsonElement serialize(CompoundNBT object, Type interfaceType, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("nbt", object.getAsString());
        return wrapper;
    }

    public CompoundNBT deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        CompoundNBT tag = JsonUtils.readNBT(elem.getAsJsonObject(), "nbt");
        return tag;
    }
}

