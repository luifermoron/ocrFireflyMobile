package com.opensource.autofill.data.dao
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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.opensource.autofill.model.ocr.OCRTag

@Dao
abstract class OCRTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend abstract fun insert(obj: OCRTag)

    @Query( "SELECT * FROM OCRTag")
    abstract fun getAllOCRTags(): LiveData<MutableList<OCRTag>>

    @Query("DELETE FROM OCRTag WHERE value_tag = :valueTag")
    abstract fun deleteTagByValue(valueTag: String): Int
}