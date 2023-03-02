package checks.AutomaticDowncastRule

class simpleCast {

  function sampleFunc() {
    var x : Object = "nice"
    var strlen = 0
    if ( x typeis String ) {
      strlen = (x as String).length // Do not need to explicitly cast x as a String
    }

    var y : Object = "str"
    if ( x typeis String ) {
      strlen = (y as String).length
    }

    var xyz = (x as String).length
  }

  function changedVariableInsideIf() {
    var x : Object = "nice"
    var strlen = 0
    if ( x typeis String ) {
      x = "cde"
      strlen = (x as String).length // Need to explicitly cast x as a String
    }
    var xyz = (x as String).length
  }

  function castOfExpression(){
    var strlen = 0
    if(this.functionReturningObject() typeis String){
      strlen = (this.functionReturningObject() as String).length
    }
  }


  function functionReturningObject() : Object {
    return new Object()
  }
}