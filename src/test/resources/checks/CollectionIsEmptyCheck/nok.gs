package checks.CollectionIsEmptyCheck

class nok {
  function sampleFunc() {
    var someList = new ArrayList<Integer>()

    if(someList.size() == 0){
      return;
    }

    if(0 == someList.Count){
      return;
    }

    var someString = "cde"

    if(someString.length() == 0){
      return;
    }

    if(0 == someString.length){
      return;
    }
  }
}
