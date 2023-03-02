package checks.EmptyCatchRule

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
