package com.kunpark.resource.utils

object Config {
    const val LOGIN_V2 = "/UI/WebService/WebServiceCenter.asmx/Login_v2"
    const val LOGIN_V5 = "/UI/WebService/WebServiceCenter.asmx/Login_v5"
    const val CHECK_SSL = "/WebServiceMobile.asmx/SSL_Check"
    const val INSERT_ANDROID_DEVICE = "ResourceMobileDataService.asmx"
    const val GET_CONDITION_SEARCH = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/GetConditionSearch"
    const val GET_ALL_RESOURCE = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/GetCalendars"
    const val GET_DEVICE_NOTIFICATION_SETTINGS = "/UI/MobileSchedule/MobileDataService.asmx/NotificationTypes"
    const val GET_TIME_NOTIFICATION_SETTINGS = "/UI/MobileSchedule/MobileDataService.asmx/AlarmTimes"
    const val GET_ORGANIZATION = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/GetResourceTree"
    const val GET_DELETE_RESOURCE = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/DeleteReservation"
    const val GET_UPDATE_RESOURCE = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/UpdateResourceReservation"
    const val GET_INSERT_RESOURCE = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/InsertResourceReservation"
    const val GET_DETAIL_RESOURCE = "UI/MobileResourceSchedule/ResourceMobileDataService.asmx/GetMyReservationDetail"
    const val GET_DEPARTMENTS = "UI/WebService/WebServiceCenter.asmx/GetDepartments"
    const val GET_USER_BY_DEPARTMENT = "UI/WebService/WebServiceCenter.asmx/GetUsersByDepartment"
    const val URL_GET_DEPARTMENT_MOD = "/UI/WebService/WebServiceCenter.asmx/GetDepartments_Mod"
    const val URL_GET_USER_MOD = "/UI/WebService/WebServiceCenter.asmx/GetAllUsersWithBelongs_Mod"
    const val VERSION_DB = 1
    const val COUNT_YEAR = 200
    const val COLUMN_AGENDA = 7
    const val ROW = 5
    const val COUNT_DAY_OF_AGENDA = 42
}