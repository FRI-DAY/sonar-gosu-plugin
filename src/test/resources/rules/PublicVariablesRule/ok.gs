package checks.PublicVariablesRule

class ok {
  private var _firstName : String as FirstName // Delcare a public property as a private variable.
  public static var SomeVar : Integer = 3
  public static final var someString : String = "asdf"
  private final var someInt : int as SOMEINTEGER = 3
  public var _variable : String as BestVariable
  var _variableWithoutModifiers : int as ImportantVar = 4
  var _defaultPrivate : int
  var _defaultPrivate2 : String = "abcd"

  function sampleFunc() {
  }

}
