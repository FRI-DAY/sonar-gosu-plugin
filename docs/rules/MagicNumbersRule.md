A magic number is a number that comes out of nowhere, and is directly used in a statement. Magic numbers are often used, for instance to limit the number of iterations of a loops, to test the value of a property, etc.

Using magic numbers may seem obvious and straightforward when you're writing a piece of code, but they are much less obvious and straightforward at debugging time.

That is why magic numbers must be demystified by first being assigned to clearly named variables before being used.

`-1`, `0` and `1` are not considered magic numbers.

## Noncompliant Code Example ##

    public static void doSomething() {
    	for(i in 0..4){                 // Noncompliant, 4 is a magic number
    		...
    	}
    }

## Compliant Solution ##

    public static final int NUMBER_OF_CYCLES = 4;
    public static void doSomething() {
      for(i in 0..NUMBER_OF_CYCLES){
        ...
      }
    }

## Exceptions ##

This rule ignores `hashCode` methods.