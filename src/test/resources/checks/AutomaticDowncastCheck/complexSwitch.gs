package checks.AutomaticDowncastCheck
uses java.util.Date

class complexSwitch {

  function switchFunc(){
    var x : Object = "neat"
    switch( typeof x ) {
      case String:
        print(x.charAt(0)) // x is automatically downcast to type String
      case Date: // Code execution falls through to next case
        print(x.Time) // x reverts to original type of Object which does not have Time property
        break
    }
  }
}