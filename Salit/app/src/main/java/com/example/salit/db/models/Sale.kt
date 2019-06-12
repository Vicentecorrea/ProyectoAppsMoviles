package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.util.*

@Entity(
    tableName = "sales", foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = CASCADE
    )]
)

data class Sale(
    @NonNull @ColumnInfo(name = "name") val name: String,
    @NonNull @ColumnInfo(name = "category_id") val category_id: Int,
    @NonNull @ColumnInfo(name = "description") val description: String,
    @NonNull @ColumnInfo(name = "originalPrice") val originalPrice: Int,
    @NonNull @ColumnInfo(name = "salePrice") val salePrice: Int,
    @NonNull @ColumnInfo(name = "is_online") val is_online: Boolean,
    @NonNull @ColumnInfo(name = "created_at") val created_at: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
