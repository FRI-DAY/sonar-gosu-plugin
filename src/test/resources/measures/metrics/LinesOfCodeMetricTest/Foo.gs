package de.friday.metrics.lines

public class Foo {

  function doSomething(arg : String) : String {
    return "Hello, got the argument '${arg}'"
  }


  // a comment




  // another comment

  /**
   * Yet, another comment.
   */
  internal function doSomethingElse() {
    print(" Hello there. This file has 11 lines of code!")
  }
}
