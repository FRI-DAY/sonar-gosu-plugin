package checks.InvertedBooleanExpressionsRule

class ok {

  function someFunc() {
    var a = 0
    if (a != 2) {
      //DoSomething
    }
    var b = a >= 10;
    var c = !b
    var d = not (b)
    var e = !(getBoolean())
    var f = !getBoolean()
  }

  function getBoolean() : boolean {
    return true
  }
}
