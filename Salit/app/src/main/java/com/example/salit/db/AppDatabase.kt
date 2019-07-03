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
import com.example.salit.db.dao.CategoryDao
import com.example.salit.db.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


@Database(
    entities = [Image::class, Location::class, Sale::class, Category::class, User::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun SaleDao(): SaleDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun UserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch(Dispatchers.IO) {
                            val database = getDatabase(context)
                            val categoryDao = database.CategoryDao()
                            val saleDao = database.SaleDao()
                            val userDao = database.UserDao()
                            categoryDao.insertAll(Category("All categories"))
                            categoryDao.insertAll(Category("Fashion"))
                            categoryDao.insertAll(Category("Technology"))
                            categoryDao.insertAll(Category("Sport"))
                            categoryDao.insertAll(Category("Others"))
                            userDao.insertAll(User("user1@gmail.com", "123123"))
                            userDao.insertAll(User("user2@gmail.com", "123123"))
                            userDao.insertAll(User("user3@gmail.com", "123123"))
                            saleDao.insertAll(
                                Sale(
                                    name = "sportPhysicalSale",
                                    categoryId = 4,
                                    description = "This is a sport sale",
                                    originalPrice = 15000,
                                    salePrice = 7000,
                                    isOnline = false,
                                    createdAt = Calendar.getInstance().time.toString(),
                                    userEmail = "user1@gmail.com",
                                    link = null,
                                    photoUri = null,
                                    latitude = null,
                                    longitude = null
                                )
                            )
                            saleDao.insertAll(
                                Sale(
                                    name = "fashionPhysicalSale",
                                    categoryId = 2,
                                    description = "This is a fashion sale",
                                    originalPrice = 30000,
                                    salePrice = 13000,
                                    isOnline = false,
                                    createdAt = Calendar.getInstance().time.toString(),
                                    userEmail = "user2@gmail.com",
                                    link = null,
                                    photoUri = null,
                                    latitude = null,
                                    longitude = null
                                )
                            )
                            saleDao.insertAll(
                                Sale(
                                    name = "technologyPhysicalSale",
                                    categoryId = 3,
                                    description = "This is a technology sale",
                                    originalPrice = 250000,
                                    salePrice = 13000,
                                    isOnline = false,
                                    createdAt = Calendar.getInstance().time.toString(),
                                    userEmail = "user2@gmail.com",
                                    link = null,
                                    photoUri = null,
                                    latitude = null,
                                    longitude = null
                                )
                            )
                            saleDao.insertAll(
                                Sale(
                                    name = "SAMSUNG GALAXY A70 NEGRO 6,7",
                                    categoryId = 3,
                                    description = "This is a technology sale",
                                    originalPrice = 329990,
                                    salePrice = 299990,
                                    isOnline = true,
                                    createdAt = Calendar.getInstance().time.toString(),
                                    userEmail = "user2@gmail.com",
                                    link = "https://simple.ripley.cl/samsung-galaxy-a70-negro-67-2000374031011p?utm_source=soicos",
                                    photoUri = null,
                                    latitude = null,
                                    longitude = null
                                )
                            )
                        }
                    }
                }).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

