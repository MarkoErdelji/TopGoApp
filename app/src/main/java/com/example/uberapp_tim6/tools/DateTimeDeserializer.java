package com.example.uberapp_tim6.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class DateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        int year = array.get(0).getAsInt();
        int month = array.get(1).getAsInt();
        int day = array.get(2).getAsInt();
        int hour = array.get(3).getAsInt();
        int minute = array.get(4).getAsInt();

        return LocalDateTime.of(year, month, day, hour, minute);
    }
}
