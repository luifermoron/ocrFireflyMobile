package com.opensource.autofill.model.ocr
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
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OCRTag")
data class OCRTag(val field_name: String,
                  val value_tag: String,
                  val max_word_quantity: Int) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        @JvmStatic val AMOUNT_FIELD = "amount"
        @JvmStatic val DESCRIPTION_FIELD = "description"
    }
}