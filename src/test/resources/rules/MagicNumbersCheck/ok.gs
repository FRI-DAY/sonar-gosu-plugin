package checks.MagicNumbersCheck

class ok {
  public static final var ACTIVITY_DESC_FIELD_LENGTH: int = 1333
  public static final var FIELD : int = "sth".length()
  public static var NOT_INITIALIZED_FIELD : int

  property set someProp(xyz: String) {
    final var checksumDigitIndex = 17
  }

  function sampleFunc() {
    var someLocalVar = 23
    var someLocalVar2 = 0
    var unassignedStatement : String
    var sum = someLocalVar + someLocalVar2 + 1F

    var addition = 1 + 0
    var complexAddition = 1.0bd + 0.0
    var complexAddition2 = 0.bd + 0.
    var complexAddition3 = .0 + .0F

    if(0F > 1bd){
      return;
    }

    if(sum > -1){
      return
    }
  }

  override function hashCode() : int {
    var sth = 3
    var sum = sth * 21

    if(sth > 32){
      //DoSth
    }

    for(i in 3..53){
      print(i)
    }

    return 234
  }

  function switchFunc() {
    var i : int = 0

    switch (i){
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
        break
    }
  }

}