package checks.CollectionIsEmptyRule

class ok {

  function sampleFunc(list : ArrayList<Integer>) {
    if(list.size() < 0){ //imposible, just for tests
      return;
    }

    if(list.size() >= 0){
      return;
    }

    if(list.size() <= 0){
      return;
    }

    if(list.isEmpty()){
      return;
    }

    if(list.Empty){
      return;
    }

    if(list.size() == 1){
      return;
    }

    if(1 == list.size()){
      return;
    }

    if(0 == intFunc()){
      return;
    }

    if(list.myCount == 0)
      return;

    var someString = "cde"

    if(someString.length() == 1){
      return;
    }

    if(someString.isEmpty()){
      return;
    }

  }

  function intFunc() : int{
    return 0
  }
}
