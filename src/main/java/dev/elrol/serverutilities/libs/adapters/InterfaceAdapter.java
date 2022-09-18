package dev.elrol.serverutilities.libs.adapters;

import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.JsonUtils;

import java.lang.reflect.Type;

public final class InterfaceAdapter implements JsonSerializer<CompoundTag>, JsonDeserializer<CompoundTag> {
    public JsonElement serialize(CompoundTag object, Type interfaceType, JsonSerializationContext context) {
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("nbt", object.getAsString());
        return wrapper;
    }

    public CompoundTag deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        CompoundTag tag = JsonUtils.readNBT(elem.getAsJsonObject(), "nbt");
        return tag;
    }
}

