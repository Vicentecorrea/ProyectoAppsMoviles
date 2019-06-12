package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.util.*

@Entity(tableName = "sales")

data class Sale(
    @NonNull @ColumnInfo(name = "name") val name: String,
    @NonNull @ColumnInfo(name = "category") val category: String,
    @NonNull @ColumnInfo(name = "description") val description: String,
    @NonNull @ColumnInfo(name = "original_price") val originalPrice: Int,
    @NonNull @ColumnInfo(name = "sale_price") val salePrice: Int,
    @NonNull @ColumnInfo(name = "is_online") val is_online: Boolean,
    @NonNull @ColumnInfo(name = "created_at") val created_at: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
