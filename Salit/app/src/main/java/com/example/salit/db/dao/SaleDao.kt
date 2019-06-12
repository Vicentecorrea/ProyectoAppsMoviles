package com.example.salit.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.salit.db.models.Sale

@Dao
interface SaleDao {
    @Query("SELECT * FROM sales ORDER BY name")
    fun getAll(): List<Sale>

    @Insert
    fun insertAll(vararg sale: Sale)

    @Query("SELECT * FROM sales ORDER BY sale_price DESC")
    fun getSalesOrderedByPriceDesc()

    @Query("SELECT * FROM sales ORDER BY sale_price DESC")
    fun getSalesOrderedByPriceAsc()

    @Query("SELECT * FROM sales WHERE category LIKE :category_name")
    fun getSalesByCategory(category_name: String): List<Sale>

    @Query("SELECT * FROM sales WHERE name LIKE :sale_name")
    fun getSalesByName(sale_name: String): List<Sale>

    @Query("SELECT * FROM sales WHERE sale_price < :target_price ORDER BY sale_price ASC")
    fun getSalesByPriceTarget(target_price: Int)
    
}