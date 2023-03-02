package checks.StringBuilderInstantiationRule

class nok {

  function someFunc() {
    var builder = new StringBuilder('a')
    var buffer = new StringBuffer('b')
  }
}
