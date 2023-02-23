package highlighting

class GosuFile {

  /*
   * Some comment
   */

  //Another comment

  @Override
  function someClass(a : int) : String {
    var stringLiteral = "This is String Literal"
    var numberLiteral = 12

    var conj = true && false

    var disj = true || false

    return stringLiteral
  }

  override function toString(): String {
    var multilineString = "  \
    Something else \
    "
    var interpolation = "Some Str${this.IntrinsicType}!"

    var singleQuoteString = 'This is some String'
    return interpolation
  }

}

//CommentAtTheEnd
