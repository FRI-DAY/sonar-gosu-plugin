package checks.SuppressWarningsListener

class fieldsAndProperties {
  @SuppressWarnings("gosu:PublicStaticFieldCheck")
  public static var SomeVar : Integer = 3
  @SuppressWarnings("gosu:PublicStaticFieldCheck")
  public static var someString : String = "asdf"

  @SuppressWarnings("gosu:TODOsRule")
  property get Something() : String {
    //todo
    return "abcde"
  }

  @SuppressWarnings("gosu:TODOsRule")
  property get SomethingElse() : String {
    //todo
    return "abcde"
  }
}
