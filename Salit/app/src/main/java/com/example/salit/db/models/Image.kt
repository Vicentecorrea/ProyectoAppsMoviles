package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(
    tableName = "Image", foreignKeys = [ForeignKey(
        entity = Sale::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("saleId"),
        onDelete = CASCADE
    )]
)

data class Image(
    @NonNull @ColumnInfo(name = "uri") val uri: String?,
    @NonNull @ColumnInfo(name = "saleId") val sale_id: Int
){
    @PrimaryKey(autoGenerate=true) var id: Int = 0
}