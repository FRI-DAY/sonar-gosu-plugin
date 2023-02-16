package checks.EmptyCatchCheck

class nok {
  var logger : Logger

  function someFunc() {
    try{
      //DoSomething
    } catch (ex : IndexOutOfBoundsException){

    } catch (ex : Exception) {

    }
  }
}