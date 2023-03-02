package checks

uses java.time.LocalDate

class ok {
  function sampleFunc(s: String): LocalDate {
    var result1 = Date.now()
    var result2 = DateUtil.currentDate()
    var result3 = new Date(0)
    return result2.toLocalDateTruncated()
  }
}