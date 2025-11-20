package com.example.dormmamu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dormmamu.model.Dorm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteManager {

    private static final String PREF_NAME = "favorite_dorms_pref";
    private static final String KEY_FAVORITES = "favorite_dorm_list";

    // Save or remove a dorm
    public static void saveFavorite(Context context, Dorm dorm, boolean isFavorite) {
        ArrayList<Dorm> favorites = getFavorites(context);

        if (isFavorite) {
            boolean exists = false;
            for (Dorm d : favorites) {
                if (d.getName().equals(dorm.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) favorites.add(dorm);

        } else {
            favorites.removeIf(d -> d.getName().equals(dorm.getName()));
        }

        saveFavorites(context, favorites);
    }

    // Check if dorm is favorited
    public static boolean isFavorite(Context context, String dormName) {
        ArrayList<Dorm> favorites = getFavorites(context);
        for (Dorm d : favorites) {
            if (d.getName().equals(dormName)) return true;
        }
        return false;
    }
    public static void removeFavorite(Context context, Dorm dorm) {
        ArrayList<Dorm> favorites = getFavorites(context);

        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getName().equals(dorm.getName())) {
                favorites.remove(i);
                break;
            }
        }

        saveFavorites(context, favorites);
    }


    // Get the list of favorites
    public static ArrayList<Dorm> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_FAVORITES, null);

        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<ArrayList<Dorm>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private static void saveFavorites(Context context, ArrayList<Dorm> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_FAVORITES, new Gson().toJson(list)).apply();
    }
}
