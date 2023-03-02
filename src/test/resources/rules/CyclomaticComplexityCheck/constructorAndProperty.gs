package checks.CyclomaticComplexityCheck

class constructorAndProperty {

  property get SomeProperty() : boolean{ // +2 to issues
    var condition1 = true
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    if(condition1)
      return false
    return true
  }

  construct(){ // +2 to issues
    var condition1 = true
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    if(condition1)
      return
    return
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