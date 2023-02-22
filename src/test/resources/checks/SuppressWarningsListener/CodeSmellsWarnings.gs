package checks.SuppressWarningsListener

@SuppressWarnings("gosu:code_smells")
class CodeSmellsWarnings {

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
//todo This one should not be suppressed
