Returning `null` instead of an actual array or collection forces callers of the method to explicitly test for nullity, making them more complex and less readable.

Moreover, in many cases, `null` is used as a synonym for empty.

## Noncompliant Code Example ##

    public function getResults() : List<Result> {
      return null                              // Noncompliant
    }
    
    public function getResults() : Result[] {
      return null                              // Noncompliant
    }
    
    public function foo(arg : String) {
      var results = getResults()
    
      if (results != null) {                   // Nullity test required to prevent NPE
        for (result in results) {
          /* ... */
        }
      }
    }

## Compliant Solution ##

    public static function getResults() : List<Result> {
      return Collections.emptyList()           // Compliant
    }
    
    public static function getResults() : Result[] {
      return {}
    }
    
    public static function foo(arg : String) {
      for (result in getResults()) {
        /* ... */
      }
    }

## See ##

 *  [CERT, MSC19-C.][CERT_ MSC19-C.] \- For functions that return an array, prefer returning an empty array over a null value
 *  [CERT, MET55-J.][CERT_ MET55-J.] \- Return an empty array or collection instead of a null value for methods that return an array or collection


[CERT_ MSC19-C.]: https://www.securecoding.cert.org/confluence/x/AgG7AQ
[CERT_ MET55-J.]: https://www.securecoding.cert.org/confluence/x/zwHEAw