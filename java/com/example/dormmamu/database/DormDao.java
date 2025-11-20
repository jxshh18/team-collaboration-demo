package com.example.dormmamu.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dormmamu.model.Dorm;

import java.util.List;

@Dao
public interface DormDao {

    @Insert
    void insertDorm(Dorm dorm);

    @Update
    void updateDorm(Dorm dorm);

    @Query("SELECT * FROM dorms")
    List<Dorm> getAllDorms();

    @Query("SELECT * FROM dorms WHERE id = :id LIMIT 1")
    Dorm getDormById(int id);

    @Query("DELETE FROM dorms")
    void deleteAllDorms();
}
