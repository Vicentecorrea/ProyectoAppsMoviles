package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(
    tableName = "Link", foreignKeys = [ForeignKey(
        entity = Sale::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("saleId"),
        onDelete = ForeignKey.CASCADE
    )]
)

data class Link(
    @NonNull @ColumnInfo(name = "link") val link: String?,
    @NonNull @ColumnInfo(name = "saleId") val saleId: Int
){
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}