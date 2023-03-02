package checks.PublicVariablesRule

class nok {
  public var FirstName : String // Do not declare a public variable.

  @FieldWithAnnotation
  public var FieldWithAnnotation : int

  @FirstAnnotation
  @SecondAnnotation
  @ComplexAnnotation(:key = 13)
  public var FieldWithComplexAnnotations : String

  function sampleFunc() {
    var argOne = 1
    var something = 3
    return;
  }

}
