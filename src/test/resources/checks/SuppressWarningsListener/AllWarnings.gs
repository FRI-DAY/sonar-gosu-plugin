package checks.SuppressWarningsListener

@SuppressWarnings("gosu:all")
class AllWarnings {

/*
ToDo structured comment
 */
  function sampleFunc() {
    var argOne = 1
   //todo this
    var something = 3
    return;
  }

  /**
   * TODO in javadoc
   */
}
//Todo This one should not be suppressed
