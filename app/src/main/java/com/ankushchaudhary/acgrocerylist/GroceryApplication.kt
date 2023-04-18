package com.ankushchaudhary.acgrocerylist

import android.app.Application
import com.ankushchaudhary.acgrocerylist.roomdb.ItemDatabase

class GroceryApplication: Application() {

    /**
     * Using 'by lazy' so the database is only created when needed
     * rather than when the application starts.
     */

    val database: ItemDatabase by lazy { ItemDatabase.getDatabase(this) }
}