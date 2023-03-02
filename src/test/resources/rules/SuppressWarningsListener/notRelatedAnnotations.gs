package checks.SuppressWarningsListener

class notRelatedAnnotations {
  @SuppressWarnings("all")
  public static var SomeVar : Integer = 3
  public static var someString : String = "asdf"

  @SuppressWarnings("cast")
  property get Something() : String {
    //todo
    return "abcde"
  }

  @SuppressWarnings({"abc", "cde", "efg"})
  property get SomethingElse() : String {
    //todo
    return "abcde"
  }
}
