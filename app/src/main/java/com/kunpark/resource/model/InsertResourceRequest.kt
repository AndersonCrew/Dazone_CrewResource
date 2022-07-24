package com.kunpark.resource.model

/**
 * Created by BM Anderson on 21/07/2022.
 */
class InsertResourceRequest {
    var Title: String?= null
    var IsLunar: String?= null
    var languageCode: String?= null
    var sessionId: String?= null
    var timeZoneOffset: String?= null
    var EndDate: String?= null
    var StartDate: String?= null
    var StartTime: String?= null
    var ListNotification: String?= null
    var EndTime: String?= null
    var ParticipantNo: String?= null
    var Content: String?= null
    var ResourceNo: Int?= null
    var RepeatDOWs: Int = 2
    var NotiTimeType: Int?= null
    var ReservationNo: Int?= null
    var RepeatType: Int?= null
    var RepeatMonth: Int?= null
    var RepeatCount: Int?= null
    var RepeatDay: Int?= null
    var IsNotiPopup: Boolean?= null
    var IsNotiMail: Boolean?= null
    var IsNotiSMS: Boolean?= null
    var IsNotiNote: Boolean?= null
    var IsAllDay: Boolean = false
    var IsLastDay: Int = 0
    var IsFiveWeek: Int = 0
}