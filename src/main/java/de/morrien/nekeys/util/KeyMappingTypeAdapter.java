package de.morrien.nekeys.util;

import com.google.gson.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Type;

/**
 * Created by Timor Morrien
 */
public class KeyMappingTypeAdapter implements JsonDeserializer<KeyMapping>, JsonSerializer<KeyMapping> {
    private static final String KEY_MAPPING_NAME = "key_mapping_name";

    @Override
    public KeyMapping deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonPrimitive prim = (JsonPrimitive) jsonObject.get(KEY_MAPPING_NAME);
        final String name = prim.getAsString();
        for (KeyMapping keyBinding : Minecraft.getInstance().options.keyMappings) {
            if (keyBinding.getName().equalsIgnoreCase(name)) {
                return keyBinding;
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(KeyMapping src, Type typeOfSrc, JsonSerializationContext context) {
        var json = new JsonObject();
        json.addProperty(KEY_MAPPING_NAME, src.getName());
        return json;
    }
}
