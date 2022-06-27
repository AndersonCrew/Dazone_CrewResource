package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "ORGANIZATION")
class Organization(
    @SerializedName("ResourceNo")
    @ColumnInfo(name = "resourceNo")
    var resourceNo: Int? = null,

    @SerializedName("Title")
    @ColumnInfo(name = "title")
    var title: String? = null,

    @SerializedName("ParentID")
    @ColumnInfo(name = "parentID")
    var parentID: String? = null,

    @SerializedName("ResourceTrees")
    @ColumnInfo(name = "resourceTrees")
    var resourceTrees: ArrayList<Organization> = arrayListOf(),

    var isChosen: Boolean = false


): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}