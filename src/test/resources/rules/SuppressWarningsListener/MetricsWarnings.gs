package checks.SuppressWarningsListener

class MetricsWarnings {

  @SuppressWarnings("gosu:metrics")
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
    if (condition1)
      return false
    else if (condition1)
      return true
    return true
  }
  //this one is not suppressed
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
    if (condition1)
      return false
    if (condition1)
      return false
    if (condition1)
      return false
    else if (condition1)
      return true
    return true
  }
}
