package checks.CognitiveComplexityRule

class NestedIfStatements {
  function sampleFunc() {
    var condition1 = 21 > 32
    var condition2 = 1 + 2 > 3

    if(condition1){
      //DoSomething
    } else if(condition2){
      if(condition1){
        //DoSomethingElse
      } else if(condition1){ //Noncompliant
        //DoSomethingElseElse
      }
    }

    if(condition1){
      //DoSomething
    } else if(condition2){
      if(condition1){
        //DoSomethingElse
      } else if(condition1){ //Noncompliant
        //DoSomethingElseElse
      }
    }
  }
}
