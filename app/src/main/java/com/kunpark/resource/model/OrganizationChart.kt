package com.kunpark.resource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by BM Anderson on 02/07/2022.
 */
@Entity(tableName = "ORGANIZATION_LIST")
class OrganizationChart(
    @PrimaryKey
    @ColumnInfo(name = "timeUpdated")
    var timeUpdated: String = "",

    @ColumnInfo(name = "listOrganization")
    var list: ArrayList<Organization>? = ArrayList()
    )