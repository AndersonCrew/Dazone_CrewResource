package com.kunpark.resource.model

import com.google.gson.annotations.SerializedName

/**
 * Created by BM Anderson on 21/07/2022.
 */
class ResourceNotification(
    @SerializedName("NotificationType")
    var notificationType: String?= null,

    @SerializedName("NotificationNo")
    var notificationNo: Int = 0,

    @SerializedName("ScheduleNo")
    var scheduleNo: Int = 0,

    @SerializedName("AlarmTime")
    var alarmTime: Int?= null
)