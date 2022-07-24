package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by BM Anderson on 13/07/2022.
 */

@Entity(tableName = "RESOURCE_TREE")
class ResourceTree: Serializable {
    @PrimaryKey
    @SerializedName("ResourceNo")
    @ColumnInfo(name = "resourceNo")
    var resourceNo: Int? = null

    @SerializedName("ParentID")
    @ColumnInfo(name = "parentID")
    var parentID: String? = null

    @SerializedName("ResourceTrees")
    @ColumnInfo(name = "resourceTrees")
    var resourceTrees: ArrayList<ResourceTree> = arrayListOf()

    @SerializedName("Title")
    @ColumnInfo(name = "title")
    var title: String? = null

    var isChosen : Boolean = false
}