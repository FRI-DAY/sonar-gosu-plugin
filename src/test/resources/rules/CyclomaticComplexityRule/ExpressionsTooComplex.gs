package checks.CyclomaticComplexityRule

class ExpressionsTooComplex {
  function test() : void { //+2 -> underline function + secondary issue
    var condition1 = true
    var condition2 = false
    var condition3 = false
    var condition4 = true
    var condition5 = false
    var condition6 = true
    //Complexity -> 16
    if (((condition1 && condition2) || (condition3 and condition4)) && condition5) {
    }

    while (condition1 || condition2 && (condition3 && condition4) or condition5) {
    }

    do {
    } while ((condition1 or condition2) and (condition3 and condition4) or (condition5 and condition6))

    //Complexity -> 10
    var a = (condition1 or condition2) and (condition3 and condition4) and condition5 ? doSomething(1) : doSomethingElse(0)
    var b = (condition1 or condition2) ? doSomething(1)
        : (condition2 or condition3) ? null
        : doSomethingElse(1)
    var c = getInteger(2)?:"stj"


    //Complexity -> 6
    if (condition1) {
      if (condition2) {

        if (condition3) {
          return;
        } else if (condition1 && condition2) {
          return;
        } else {
          return;
        }
      } else if (condition1) {
        doSomethingElse(2)
      } else if (condition3) {
        doSomething(0)
      } else {
        return;
      }
    } else {
      doSomething(0)
    }

    //Complexity -> 5
    switch (c) {
      case 0:
        doSomething(1)
        break;
      case 1:
      case 2:
      case 3:
        doSomethingElse(12)
      default:
        doSomething(0)
    }

    //Complexity -> 4
    switch (c) {
      case 0:
        doSomething(1)
        break;
      case 1:
      case 2:
      case 3:
        doSomethingElse(12)
    }
    //Complexity -> 4
    for (i in 1..2) {
      if (i == 1)
        break
      else
        continue
    }

    foreach (i in 1..2) {
      if (i == 1)
        break
      else
        continue
    }

  }

  private function getInteger(param : Integer) : Integer {
    return param
  }

  private function doSomething(param : int) : int {
    return param
  }

  private function doSomethingElse(param : int) : int {
    return param
  }
}
