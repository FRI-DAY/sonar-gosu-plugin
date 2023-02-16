package checks

uses java.time.LocalDate

class nok {

  function sampleFunc(s: String): LocalDate {
    var otherDate = new Date()
    return LocalDate.now()
  }

}