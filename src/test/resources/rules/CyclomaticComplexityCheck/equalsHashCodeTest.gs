package checks.CyclomaticComplexityCheck

class equalsHashCodeTest {
  override function equals(obj : Object) : boolean {
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

  override function hashCode() : int {
    var hashCode = 2
    var condition1 = true
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    if (condition1)
      hashCode++
    return hashCode
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