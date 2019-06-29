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

    @Query("SELECT * FROM sales WHERE categoryId LIKE :categoryName")
    fun getSalesByCategory(categoryName: String): List<Sale>

    @Query("SELECT * FROM sales WHERE name LIKE :saleName")
    fun getSalesByName(saleName: String): List<Sale>

    @Query("SELECT * FROM sales WHERE name LIKE :saleName AND categoryId LIKE :categoryName")
    fun getSalesByNameAndCategory(saleName: String, categoryName: String): List<Sale>

    @Query("SELECT * FROM sales WHERE salePrice < :targetPrice ORDER BY salePrice ASC")
    fun getSalesByPriceTarget(targetPrice: Int): List<Sale>

    @Query("SELECT * FROM sales WHERE id LIKE :id")
    fun getSaleById(id: Int): Sale?
    
}