package checks.CognitiveComplexityCheck

class testBelowThreshold {
  function someFunc(obj : Object) : boolean {
    var condition1 = true
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return false
    return true
  }

  function someFunc1(obj : Object) : boolean {
    var condition1 = true
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return false
    return true
  }

  function someFunc2(obj : Object) : boolean {
    var condition1 = true
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return false
    return true
  }

  function someFunc3(obj : Object) : boolean {
    var condition1 = true
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return false
    return true
  }

  function someFunc4(obj : Object) : boolean {
    var condition1 = true
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return false
    return true
  }

  function someFunc5(obj : Object) : boolean {
    var condition1 = true
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return false
    return true
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