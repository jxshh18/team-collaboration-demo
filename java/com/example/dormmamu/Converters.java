package com.example.dormmamu;

import androidx.room.TypeConverter;
import java.util.Arrays;

public class Converters {

    @TypeConverter
    public static String fromIntArray(int[] arr) {
        if (arr == null) return "";
        return Arrays.toString(arr);
    }

    @TypeConverter
    public static int[] toIntArray(String value) {
        if (value == null || value.isEmpty()) return new int[]{};
        value = value.replace("[", "").replace("]", "").replace(" ", "");
        String[] parts = value.split(",");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
            arr[i] = Integer.parseInt(parts[i]);
        return arr;
    }
}
