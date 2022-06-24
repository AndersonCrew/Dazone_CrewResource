package com.kunpark.resource.utils

/**
 * Created by BM Anderson on 20/06/2022.
 */
fun checkNamNhuan(year: Int): Boolean {
    var isLeap = false
    isLeap = if (year % 4 == 0) //chia hết cho 4 là năm nhuận
    {
        if (year % 100 == 0) //nếu vừa chia hết cho 4 mà vừa chia hết cho 100 thì không phải năm nhuận
        {
            year % 400 == 0 //không chia hết cho 400 thì không phải năm nhuận
        } else  //chia hết cho 4 nhưng không chia hết cho 100 là năm nhuận
            true
    } else {
        false
    }
    return isLeap
}