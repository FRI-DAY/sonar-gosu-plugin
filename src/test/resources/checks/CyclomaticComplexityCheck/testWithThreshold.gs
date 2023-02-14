package checks.CyclomaticComplexityCheck

class testWithThreshold {
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