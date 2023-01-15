package com.example.uberapp_tim6.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class DateTimeSerializer implements JsonSerializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        array.add(src.getYear());
        array.add(src.getMonthValue());
        array.add(src.getDayOfMonth());
        array.add(src.getHour());
        array.add(src.getMinute());
        array.add(src.getSecond());
        array.add(src.getNano());
        return array;
    }
}
