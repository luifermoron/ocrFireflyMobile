package com.opensource.autofill.model.ocr

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