package checks.SuppressWarningsListener

@SuppressWarnings({"gosu:PublicStaticFieldRule","gosu:TODOsRule", "cdef"})
class arrayOfSuppressWarnings {

  public static var SomeVar : Integer = 3
  public static var someString : String = "asdf"

  property get Something() : String {
    //todo
    return "abcde"
  }

  property get SomethingElse() : String {
    //todo
    return "abcde"
  }
}
