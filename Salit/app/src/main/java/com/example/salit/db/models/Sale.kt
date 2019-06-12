package com.example.salit.db.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.support.annotation.NonNull

@Entity(tableName = "sales")

data class Sale (
    @NonNull @ColumnInfo(name = "name") val name: String;
    @NonNull
)
