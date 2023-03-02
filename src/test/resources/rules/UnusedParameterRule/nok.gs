package checks.UnusedParameterRule

class nok {

  static property set ID(id : String) {
    _id = "sth"
  }

  private function manyParams(param1 : int, param2 : int, param3 : int) : int {
    doSomething()
    return param1
  }

  public final function finalFunction(param1 : int) {
    doSomething()
  }

  private function functionWithThrow(param1 : int) {
    doSomething()
    throw new NotImplementedException("TODO");
  }

  public static function staticFunction(param1 : int) {
    doSomething()
  }

  private static function withBlockParam(myCallBack( String ) : void) {
    doSomething()
  }

  @SuppressWarning("unchecked")
  @SuppressWarning("rawtypes")
  private function manyParamsWithAnnotation(param1 : int, param2 : int, param3 : int) : int {
    doSomething()
    return param1
  }

  private static function doSomething(){

  }
}
