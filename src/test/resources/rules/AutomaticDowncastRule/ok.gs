package checks.AutomaticDowncastRule

class ok {

  function sampleFunc() {
    var x : Object = "nice"
    var strlen = 0
    if ( x typeis String ) {
      strlen = x.length // x automatically downcast to a String which has a length property
    }
  }

  static function format(obj : Object) : String {
    if (obj typeis BigDecimal or obj typeis Double or obj typeis Float) {
      return formatBigDecimal(obj as BigDecimal) //Cast to BigDecimal is necessary
    }
    if (obj typeis java.lang.Number) {
      return NumberUtil.render(obj)
    }
    return obj as String
  }

  function switchFunc() {
    var x : Object = "neat"
    var y : Object = "sth"
    switch ( typeof x ){
      case String :
        print( x.charAt( 0 ) ) // x is automatically downcast to type String
        break
      case Date :
        print( x.Time ) // x is automatically downcast to type Date
        break
      case int :
    }

    switch ( typeof y){
      case String :
        print( (x as String).charAt( 0 ) ) // x is automatically downcast to type String
        break
      case Date :
        print( (x as Date).Time ) // x is automatically downcast to type Date
        break
      default:
        print(x as String)
    }

    var i = 0
    switch (i){
      case 0:
        break
      case 1:
        print( (x as String).charAt( 0 ) )
        break
    }

    switch (typeof this.functionReturningObject()){
      case String :
        print( (this.functionReturningObject() as String).charAt( 0 ) )
        break
      case Date :
        print( (this.functionReturningObject() as Date).Time )
        break
    }

    //typeof without switch
    print(typeof x)
    var typeofVar = typeof y
  }

  function functionReturningObject() : Object{
    return new Object()
  }
}