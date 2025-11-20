package com.example.dormmamu.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.dormmamu.Converters;
import com.example.dormmamu.model.Dorm;
import com.example.dormmamu.model.User;

@Database(entities = {User.class, Dorm.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract DormDao dormDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "dormmamu_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()  // Not recommended for production
                    .build();
        }
        return instance;
    }
}
