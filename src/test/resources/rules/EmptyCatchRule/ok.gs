package checks.EmptyCatchRule

class ok {
  var logger : Logger

  function someFunc() {
    try{
     //DoSomething
    } catch (ex : IndexOutOfBoundsException){
      logger.LogError(ex)
      throw ex
    } catch (ex : Exception) {
      //Some Action
    }
  }
}
