package com.adisa.diningplus.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adisa.diningplus.db.entities.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Insert
    void insert(Course course);

    @Delete
    void delete(Course course);

    @Update
    void update(Course course);
}