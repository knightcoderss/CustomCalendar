package `in`.gowebs.customcalendar

data class AttendanceModel(
    var data: ArrayList<DayEvent>,
    var message: String = "",
    var status: Boolean = true,
    var monthName: String = "",
) {
    data class DayEvent(
        var day: String = "",
        var BreakTime: String = "",
        var DateString: String = "",
        var Empcode: String = "",
        var Erl_Out: String = "",
        var INTime: String = "",
        var Late_In: String = "",
        var Name: String = "",
        var OUTTime: String = "",
        var OverTime: String = "",
        var Remark: String = "",
        var Status: String = "",
        var WorkTime: String = "",
        var id: String = "",
    )
}
