package checks.SuppressWarningsListener

class VulnerabilitiesWarnings {
  @SuppressWarnings("gosu:vulnerabilities")
  function someFunc() {
    try{
      //DoSomething
    } catch (ex : IndexOutOfBoundsException){

    } catch (ex : Exception) {

    }
  }
}
