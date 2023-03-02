package checks.InvertedBooleanExpressionsCheck

class nok {

  function someFunc() {
    var a = 0
    if ( !(a == 2)) {
      //DoSomething
    }
    var b = !(a < 10);

    var c = not (a < 10)
  }
}
