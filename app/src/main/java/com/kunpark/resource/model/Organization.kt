package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "ORGANIZATION")
class Organization(

    @SerializedName("Name")
    @ColumnInfo(name = "name")
    var name: String? = null,

    @SerializedName("DepartNo")
    @ColumnInfo(name = "departNo")
    var departNo: Int? = null,

    @SerializedName("ParentNo")
    @ColumnInfo(name = "ParentNo")
    var parentNo: Int? = null,

    @SerializedName("ChildDepartments")
    @ColumnInfo(name = "childDepartments")
    var childDepartments: ArrayList<Organization> = arrayListOf(),

    @ColumnInfo(name = "childMembers")
    var childMembers: ArrayList<User> = arrayListOf(),

    var isChosen: Boolean = false,
    var isChanging: Boolean = false


): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}