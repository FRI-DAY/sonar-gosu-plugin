package checks.SuppressWarningsListener

@SuppressWarnings("gosu:TODOsRule")
class CheckWarnings {

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
// todo This one should not be suppressed
