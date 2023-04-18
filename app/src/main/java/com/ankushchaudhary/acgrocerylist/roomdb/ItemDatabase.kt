package com.ankushchaudhary.acgrocerylist.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Item::class], version = 1, exportSchema = false)

abstract class ItemDatabase: RoomDatabase() {
    abstract fun getDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        /*
            If the INSTANCE is not null, then, return it
            otherwise create the Database (buildDatabase).
         */

        fun getDatabase(context: Context): ItemDatabase {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE == buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): ItemDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ItemDatabase::class.java,
                "item_database"
            )
                    /*
                        Wipes and rebuilds instead of migrating if no Migration object.
                     */
                .fallbackToDestructiveMigration()
                .build()
        }

    }

}