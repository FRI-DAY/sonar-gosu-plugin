As a best practice, Guidewire recommends that you convert public variables to properties. Properties separate the interface of an object from the implementation of its storage and retrieval. Although Gosu supports public variables for compatibility with other languages, Guidewire strongly recommends public properties backed by private variables instead of public variables

## Noncompliant Code Example ##

    public class Greeter {
      public var FirstName : String // Do not declare a public variable.
    }

## Compliant Solution ##

    public class Greeter {
      private var _firstName : String as FirstName // Declare a public property as a private variable.