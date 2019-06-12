package com.example.salit.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.salit.db.models.Sale

@Dao
interface SaleDao{
    @Query("SELECT * FROM sales")
    fun getAll(): List<Sale>

    @Insert
    fun insertAll(vararg sale: Sale)

}