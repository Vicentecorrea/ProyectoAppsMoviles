package com.example.salit.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.salit.db.dao.SaleDao
import com.example.salit.db.models.*
import java.util.concurrent.Executors
import android.support.annotation.NonNull



@Database(
    entities = [Image::class, Link::class, Location::class, Sale::class],
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase : RoomDatabase(){
    abstract fun SaleDao(): SaleDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}

