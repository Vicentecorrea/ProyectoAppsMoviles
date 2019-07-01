package com.example.salit.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.salit.db.models.Category

@Dao
interface CategoryDao {
    @Insert
    fun insertAll(vararg category: Category)

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("SELECT id FROM category WHERE name LIKE :categoryName")
    fun getCategoryId(categoryName: String): Int

    @Query("SELECT name FROM category WHERE id LIKE :id")
    fun getCategoryName(id: Int): String

}