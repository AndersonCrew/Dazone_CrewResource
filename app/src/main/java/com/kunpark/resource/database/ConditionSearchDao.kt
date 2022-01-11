package com.kunpark.resource.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kunpark.resource.model.ConditionSearch
import com.kunpark.resource.model.User

@Dao
interface ConditionSearchDao {
    @Query("SELECT * FROM CONDITION_SEARCH ORDER BY id DESC")
    fun getData(): LiveData<List<ConditionSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMoreData(conditionSearch: List<ConditionSearch>)
}