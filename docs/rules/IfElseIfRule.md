This rule applies whenever an `if` statement is followed by one or more `else if` statements; the final `else if` should be followed by an `else` statement.

The requirement for a final `else` statement is defensive programming.

The `else` statement should either take appropriate action or contain a suitable comment as to why no action is taken. This is consistent with the requirement to have a final `default` clause in a `switch` statement.

## Noncompliant Code Example ##

    if (x == 0) {
      doSomething();
    } else if (x == 1) {
      doSomethingElse();
    }

## Compliant Solution ##

    if (x == 0) {
      doSomething();
    } else if (x == 1) {
      doSomethingElse();
    } else {
      throw new IllegalStateException();
    }

## See ##

 *  MISRA C:2004, 14.10 - All if...else if constructs shall be terminated with an else clause.
 *  MISRA C++:2008, 6-4-2 - All if...else if constructs shall be terminated with an else clause.
 *  MISRA C:2012, 15.7 - All if...else if constructs shall be terminated with an else statement
 *  [CERT, MSC01-C.][CERT_ MSC01-C.] \- Strive for logical completeness
 *  [CERT, MSC57-J.][CERT_ MSC57-J.] \- Strive for logical completeness


[CERT_ MSC01-C.]: https://www.securecoding.cert.org/confluence/x/YgE
[CERT_ MSC57-J.]: https://www.securecoding.cert.org/confluence/x/PQHRAw