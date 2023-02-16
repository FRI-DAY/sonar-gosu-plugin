package checks.SameConditionsInIfCheck

class ok {

  function sampleFunc() {
    var condition1 = 21 > 32
    var condition2 = 1 + 2 > 3

    if(condition1){
      //DoSomething
    } else if(condition2){
      //DoSomething
    }

    if(condition1){
      if(condition1){
        //DoSomething
      } else if(condition2){
        //DoSomething
      }
    } else if(condition2){
      if(condition1){
        //DoSomething
      } else if(condition2){
        //DoSomething
      }
      if(condition1){
        //DoSomething
      } else if(condition2){
        //DoSomething
      }
    }
  }
}
