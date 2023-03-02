package checks.NestedStatementsRule

class nok {

  function sampleFunc() {
    var sth : List<String>
    for(i in sth){
      for(j in i){
        if(i.length() > 0){
          if(j.equals("j")){
            return;
          }
        }
      }
    }
  }

  function nestedWhileFunc() {
    var abc = true

    while(abc){
     do {
       while (true){
        if (!abc){
          //Raise issue
        }
       }
     } while (!abc)
    }
  }

  function nestedSwitch(x : int) {
    var sth : List<String>

    foreach (i in sth){
      switch (x){
        case 1:
          if (i.length() > 0){
            if(sth.size() > 2){
              //DoSth()
            }
          } else if (i.length() < 0){
            try{
              //DoSth()
            } catch (e : Exception){
              //DoSth()
            }
          }
      }
    }
  }

}
