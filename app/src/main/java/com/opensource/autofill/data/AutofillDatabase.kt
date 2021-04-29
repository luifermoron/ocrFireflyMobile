package com.opensource.autofill.data
/*
  ~ Copyright (c)  2021 Luis Morón
  ~
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
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