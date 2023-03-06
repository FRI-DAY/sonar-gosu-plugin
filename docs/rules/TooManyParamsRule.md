A long parameter list can indicate that a new structure should be created to wrap the numerous parameters or that the function is doing too many things.

## Noncompliant Code Example ##

With a maximum number of 4 parameters:

    public doSomething(param1: int, param2: int, param3: int, param4: String, param5: long) {
    ...
    }

## Compliant Solution ##

    public doSomething(param1: int, param2: int, param3: int, param4: String) {
    ...
    }

## Exceptions ##

 *  Overridden methods