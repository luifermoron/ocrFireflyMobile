package com.opensource.autofill.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.opensource.autofill.data.dao.OCRTagDao
import com.opensource.autofill.model.ocr.OCRTag

@Database( entities = [OCRTag::class], version = 1)
abstract class AutofillDatabase : RoomDatabase() {

    abstract fun ocrTagDataDao(): OCRTagDao

    companion object {
        @JvmStatic
        fun buildDatabase(context: Context): AutofillDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AutofillDatabase::class.java, "AutofillDatabase.db"
            ).build()
        }
    }
}