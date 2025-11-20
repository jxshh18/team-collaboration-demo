package com.example.dormmamu.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.dormmamu.model.User;

import java.util.List;

@Dao
public interface UserDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(User user);


    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);


    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users")
    LiveData<List<User>> observeUsers();

    @Query("DELETE FROM users WHERE id = :id")
    void deleteUser(int id);
}
