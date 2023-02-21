package checks.AutomaticDowncastCheck

uses java.util.Date

class simpleSwitch {

  function switchFunc() {
    var x : Object = "neat"
    switch ( typeof x ){
      case String :
        print( (x as String).charAt( 0 ) ) // x is automatically downcast to type String
        break
      case Date :
        print( (x as Date).Time ) // x is automatically downcast to type Date
        break
    }

    var okCast = (x as String).length()
  }

  function emptySwitch(i : int){
    var x : Object = "neat"
    switch ( i ) {
      case 1 :
      case 2:
    }
  }

  //function noBreakSwitch(){
  //  var x : Object = "neat"
  //  switch ( typeof x ) {
  //    case String :
  //      break;
  //    case Date:
  //  }
  //}
}
