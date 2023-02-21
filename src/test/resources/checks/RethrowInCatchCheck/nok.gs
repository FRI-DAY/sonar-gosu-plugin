package checks.RethrowInCatchCheck

class nok {
  var logger : Logger

  function someFunc() {
    try{
      //DoSomething
    } catch (ex : IndexOutOfBoundsException){
      throw ex
    } catch (ex : Exception) {
      //Some Action
    }
  }
}