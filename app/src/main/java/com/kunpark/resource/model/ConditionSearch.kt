package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "CONDITION_SEARCH")
class ConditionSearch {
    @ColumnInfo(name = "ConditionNo")
    @SerializedName("ConditionNo")
    var conditionNo: Int?= null

    @ColumnInfo(name = "Key")
    @SerializedName("Key")
    var key: String?= null

    @ColumnInfo(name = "Title")
    @SerializedName("Title")
    var title: String?= null

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

    @ColumnInfo(name = "isCheck")
    var isCheck: Boolean? = false
}