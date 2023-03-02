package checks.NestedStatementsCheck

class ok {

  function sampleFunc() {
    var sth : List<String>
    for(i in sth){
      for(j in i){
        if(i.length() > 0){
          //DoSTH
        }
      }
    }
  }
}
