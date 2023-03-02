package checks.PublicStaticFieldRule

class ok {

  public static final var SomeVar : Integer = 3
  private static var privateField : String = ""
  protected static var protectedStaticField : int = 1
  protected final var protectedFinalField : int = 3
  protected var protectedField : int = 1
  final var finalField : String = "";
  static var staticField : int = 12
  public var publicField : String = "sth"

  function sampleFunc() {
  }

}
