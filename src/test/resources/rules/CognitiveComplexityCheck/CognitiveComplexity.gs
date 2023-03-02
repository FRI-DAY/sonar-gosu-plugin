package checks.CognitiveComplexityCheck

class CognitiveComplexity {
  function sumOfPrimes(max : int) : int {
    var total = 0;

    for (i in 1..max) { //+1
      for (j in i..max) { //+2
        if (i % j == 0) { //+3
          continue
        }
      }
      total += i
    }

    var r = \-> {          //Increase nested level
      if (1 > 2 && 2 < 3) { //+2
        //NotImplementedYet
      }
    }

    try {
      var abc = 13
    } catch (a : ExceptionInInitializerError) { //+1
      //Nothing
    } catch (b : NoSuchFieldException) {
      if (1 < 2) {                         //+2
        //DoNothing
      }
      //Nothing
    } catch (e : Exception) {
      throw new AssertionError()
    }

    var localVar = true && false || false

    return total
  }
}