package de.morrien.nekeys.voice;

import com.google.gson.*;
import de.morrien.nekeys.api.command.IVoiceCommand;

import java.lang.reflect.Type;

/**
 * Created by Timor Morrien
 */
public class VoiceCommandTypeAdapter<T extends IVoiceCommand> implements JsonDeserializer<T>, JsonSerializer<T> {
    private static final String TYPE_FIELD_NAME = "voice_command_type";

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonPrimitive prim = (JsonPrimitive) jsonObject.get(TYPE_FIELD_NAME);
        final String className = prim.getAsString();
        final Class<T> clazz = getClassInstance(className);
        return context.deserialize(jsonObject, clazz);
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        var json = context.serialize(src, src.getClass()).getAsJsonObject();
        json.addProperty(TYPE_FIELD_NAME, src.getClass().getName());
        return json;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getClassInstance(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
