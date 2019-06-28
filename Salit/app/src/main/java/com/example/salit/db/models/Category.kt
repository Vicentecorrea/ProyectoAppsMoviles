package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull

@Entity(tableName = "category")

data class Category(
    @NonNull @ColumnInfo(name="name") val name: String?
){
    @PrimaryKey(autoGenerate = true) var id: Int=0
}