package com.kunpark.resource.base

import android.app.Application
import com.kunpark.resource.database.DazoneDatabase
import com.kunpark.resource.services.Result
import com.kunpark.resource.utils.DazoneApplication
import retrofit2.Response
import java.lang.Exception

open class BaseRepository() {
    var db: DazoneDatabase? = null

    init {
        db = DazoneDatabase.getDatabase(DazoneApplication.getInstance())
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): Result<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                Result.Success(myResp.body()!!)
            } else {
                /*
              handle standard error codes
              if (myResp.code() == 403){
                  Log.i("responseCode","Authentication failed")
              }
              .
              .
              .
               */

                val error = if (myResp.errorBody()?.string().isNullOrEmpty()) errorMessage else myResp.errorBody()?.string()
                        ?: errorMessage
                Result.Error(error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val error = if (e.message.isNullOrEmpty()) errorMessage else e.message
                    ?: errorMessage
            Result.Error(error)
        }
    }


}