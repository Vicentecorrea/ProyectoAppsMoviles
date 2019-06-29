package com.example.salit.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.salit.db.models.Sale

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY createdAt")
    fun getAll(): List<Sale>

    @Insert
    fun insertAll(vararg sale: Sale)

    @Query("SELECT * FROM sales ORDER BY salePrice DESC")
    fun getSalesOrderedByPriceDesc(): List<Sale>

    @Query("SELECT * FROM sales ORDER BY salePrice DESC")
    fun getSalesOrderedByPriceAsc(): List<Sale>

    @Query("SELECT * FROM sales WHERE categoryId LIKE :categoryId")
    fun getSalesByCategory(categoryId: Int): List<Sale>

    @Query("SELECT * FROM sales WHERE name LIKE :saleName")
    fun getSalesByName(saleName: String): List<Sale>

    @Query("SELECT * FROM sales WHERE name LIKE :saleName AND categoryId LIKE :categoryId")
    fun getSalesByNameAndCategory(saleName: String, categoryId: Int): List<Sale>

    @Query("SELECT * FROM sales WHERE salePrice < :targetPrice ORDER BY salePrice ASC")
    fun getSalesByPriceTarget(targetPrice: Int): List<Sale>
    
}