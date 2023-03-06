Unused parameters are misleading. Whatever the values passed to such parameters, the behavior will be the same.

## Noncompliant Code Example ##

    function doSomething(a : int, b : int) {     // "b" is unused
      compute(a)
    }

## Compliant Solution ##

    function doSomething(a : int) {
      compute(a)
    }

## Exceptions ##

The rule will not raise issues for unused parameters:

 *  in overridden methods
 *  in non-private methods that only `throw` or that have empty bodies
 *  in overridable methods (non-final, or not member of a final class, non-static, non-private), if the parameter is documented with a proper javadoc.
 *  in annotated methods, unless the annotation is `@SuppressWarning("unchecked")` or `@SuppressWarning("rawtypes")`, in which case the annotation will be ignored (disabled by default)

    @Override
    function doSomething(a : int, b : int) {     // no issue reported on b
      compute(a)
    }
    
    public function foo(s : String) {
      // designed to be extended but noop in standard case
    }
    
    protected function bar(s : String) {
      //open-closed principle
    }
    
    public function qix(s : String) {
      throw new UnsupportedOperationException("This method should be implemented in subclasses")
    }
    
    /**
     * @param s This string may be use for further computation in overriding classes
     */
    protected function foobar(a : int, s : String) { // no issue, method is overridable and unused parameter has proper javadoc
      compute(a)
    }

## See ##

 *  MISRA C++:2008, 0-1-11 - There shall be no unused parameters (named or unnamed) in nonvirtual functions.
 *  MISRA C:2012, 2.7 - There should be no unused parameters in functions
 *  [CERT, MSC12-C.][CERT_ MSC12-C.] \- Detect and remove code that has no effect or is never executed


[CERT_ MSC12-C.]: https://www.securecoding.cert.org/confluence/x/NYA5