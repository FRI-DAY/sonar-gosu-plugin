package checks.UnusedParameterCheck

class ok {

  /**
   * @param iq
   */
  static property set IQ(iq : int) {
    _id = "sth"
  }

  static property set ID(id : String) {
    _id = id
  }

  /**
   * @param something is wrong
   */
  private function functionWithJavadoc(something : String) : void {
    return;
  }

  /**
   * @param sthElse
   */
  private function functionWithWrongParamInJavadoc(something : int) {

  }

  private function manyParams(param1 : int, param2 : int, param3 : int) {
    var i = param1 + param2 + param3
  }

  private function paramInStringInterpolation(param1 : Integer) {
    print("this is interpolation ${param1.toString()}")
  }

  public function overridableFunction(param1 : int) {

  }

  @Override
  function overridenFunction(param1 : int) {

  }

  private function emptyFunction(param1 : int) {
    //With Comment
  }

  protected function protectedFunction(param1 : int) {
    doSomething()
  }

  private function onlyThrowStatementInFunction(param1 : int) {
    throw new NotImplementedException("TODO");
  }

  private static function doSomething(){

  }
/*
  Case for enabled annotations checking
  @ImportantAnnotation
  private function manyParamsWithAnnotation(param1 : int, param2 : int, param3 : int) : int {
    doSomething()
    return param1
  }

*/
}
