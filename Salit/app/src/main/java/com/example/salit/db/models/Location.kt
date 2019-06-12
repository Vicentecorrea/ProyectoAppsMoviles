package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(
    tableName = "Location", foreignKeys = [ForeignKey(
        entity = Sale::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("saleId"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class Location(
    @NonNull @ColumnInfo(name = "longitude") val longitude: Double,
    @NonNull @ColumnInfo(name = "latitude") val latitude: Double,
    @NonNull @ColumnInfo(name = "saleId") val saleId: Int
){
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}