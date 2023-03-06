The functions declared in an `interface` are `abstract` by default. Any variables are automatically `private`. There is no need to explicitly declare them so.

Similarly, the `final` modifier is redundant on any method of a `final` class.

Properties, functions and top-level types (outer Gosu classes, interfaces, enumerations) are `public` by default

## Noncompliant Code Example ##

    public class Vehicle {
      private var _startDate: Date as StartDate
    
      public function getDate() {
          /* ... */
      }
    }

## Compliant Solution ##

    class Vehicle {
      var _startDate: Date as StartDate
    
      function getDate() {
          /* ... */
      }
    }

## See ##

 *  [Code Style Guide][] \- Default Modifiers


[Code Style Guide]: https://friday-docs.k8s.blue.fridev.de/backend/code-style-guide/#default-modifiers