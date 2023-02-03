package com.example.uberapp_tim6.tools;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class ChartLocalDateFormatter extends ValueFormatter {

    private DateTimeFormatter formatter;

    public ChartLocalDateFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String getFormattedValue(float value) {
        return LocalDate.ofEpochDay((int) value).format(formatter);
    }
}
