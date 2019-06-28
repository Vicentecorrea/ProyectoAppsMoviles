package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.util.*

@Entity(
    tableName = "sales", foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoryId"),
            onDelete = CASCADE
        ),

        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("email"),
            childColumns = arrayOf("userEmail"),
            onDelete = CASCADE
        )
    ]
)

data class Sale(
    @NonNull @ColumnInfo(name = "name") val name: String,
    @NonNull @ColumnInfo(name = "categoryId") val categoryId: Int,
    @NonNull @ColumnInfo(name = "description") val description: String,
    @NonNull @ColumnInfo(name = "originalPrice") val originalPrice: Int,
    @NonNull @ColumnInfo(name = "salePrice") val salePrice: Int,
    @NonNull @ColumnInfo(name = "isOnline") val isOnline: Boolean,
    @NonNull @ColumnInfo(name = "createdAt") val createdAt: String,
    @NonNull @ColumnInfo(name = "userEmail") val userEmail: String,
    @ColumnInfo(name = "link") val link: String?,
    @ColumnInfo(name = "photoUri") val photoUri: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
